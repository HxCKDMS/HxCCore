package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.ISubCommand;
import com.sun.management.OperatingSystemMXBean;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.List;

public class CommandServerInfo implements ISubCommand {
    public static CommandServerInfo instance = new CommandServerInfo();

    private EnumChatFormatting defaultColor = EnumChatFormatting.BLUE;
    private EnumChatFormatting TPSDefaultColor = EnumChatFormatting.AQUA;

    @Override
    public String getName() {
        return "serverInfo";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws PlayerNotFoundException {
        EntityPlayerMP player = (EntityPlayerMP) sender;
        boolean CanSend = PermissionsHandler.canUseCommand(Config.PermLevels[16], player);
        if (CanSend) {
            sender.addChatMessage(new ChatComponentText(defaultColor + String.format("CPU usage: %1$s", getCPUUsageStyled())));
            sender.addChatMessage(new ChatComponentText(defaultColor + String.format("Memory usage: %1$s.", getMemoryUsageStyled())));
            sender.addChatMessage(new ChatComponentText(defaultColor + String.format("Server TPS: %1$s.", getServerTPSStyled())));

            for (WorldServer worldServer : DimensionManager.getWorlds())
                sender.addChatMessage(new ChatComponentText(defaultColor + String.format("DIM: %1$s, TPS: %2$s, entities: %3$s, loaded chunks: %4$s.", getDimensionStyled(worldServer), getWorldTPSStyled(worldServer), TPSDefaultColor.toString() + worldServer.loadedEntityList.size() + defaultColor, TPSDefaultColor.toString() + worldServer.getChunkProvider().getLoadedChunkCount() + defaultColor)));
        } else sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    private static long mean(long[] values) {
        long sum = 1;
        for(long l : values){
            sum+=l;
        }

        return sum / values.length;
    }

    private String getCPUUsageStyled(){
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        DecimalFormat CPUUsageFormat = new DecimalFormat("###.#");

        double CPUPercentage = osBean.getSystemCpuLoad() * 100;
        EnumChatFormatting CPUUsageColor = CPUPercentage >= 75 ? EnumChatFormatting.RED : CPUPercentage <= 50 ? EnumChatFormatting.GREEN : EnumChatFormatting.GOLD;

        return CPUUsageColor + CPUUsageFormat.format(CPUPercentage) + defaultColor + "%";
    }

    private String getMemoryUsageStyled(){
        double totalMem = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        double usedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;
        double memPercentage = usedMem / totalMem * 100;

        EnumChatFormatting MemColor = memPercentage >= 75 ? EnumChatFormatting.RED : memPercentage <= 25 ? EnumChatFormatting.GREEN : EnumChatFormatting.GOLD;

        return MemColor.toString() + (int) usedMem + "MB/ " + defaultColor + (int) totalMem + "MB";
    }

    private DecimalFormat TPSFormat = new DecimalFormat("###.###");

    private String getServerTPSStyled(){
        double MeanTickTime = mean(HxCCore.server.tickTimeArray) * 1.0E-6D;
        double MeanTPS = Math.min(1000.0/MeanTickTime, 20);

        EnumChatFormatting TPSColor = MeanTPS >= 18 ? EnumChatFormatting.GREEN : MeanTPS < 16 ? EnumChatFormatting.RED : EnumChatFormatting.GOLD;

        return TPSColor + TPSFormat.format(MeanTPS) + defaultColor;
    }

    private String getWorldTPSStyled(WorldServer worldServer){
        double WorldTickTime = mean(HxCCore.server.worldTickTimes.get(worldServer.provider.getDimensionId())) * 1.0E-6D;
        double WorldTPS = Math.min(1000.0/WorldTickTime, 20);

        EnumChatFormatting TPSColor = WorldTPS >= 18 ? EnumChatFormatting.GREEN : WorldTPS < 16 ? EnumChatFormatting.RED : EnumChatFormatting.GOLD;

        return TPSColor + TPSFormat.format(WorldTPS) + defaultColor;
    }

    private String getDimensionStyled(WorldServer worldServer){
        String DimName = worldServer.provider.getDimensionName();
        EnumChatFormatting DimColor = DimName.equalsIgnoreCase("the end") ? EnumChatFormatting.YELLOW : DimName.equalsIgnoreCase("nether") ? EnumChatFormatting.RED : DimName.equalsIgnoreCase("overworld") ? EnumChatFormatting.GREEN : EnumChatFormatting.WHITE;

        return DimColor + StringUtils.capitalize(DimName) + defaultColor;
    }
}
