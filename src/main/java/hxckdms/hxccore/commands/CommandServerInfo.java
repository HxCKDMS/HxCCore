package hxckdms.hxccore.commands;

import com.sun.management.OperatingSystemMXBean;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.MathHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandServerInfo extends AbstractSubCommand<CommandHxC> {
    private static final DecimalFormat TPSFormat = new DecimalFormat("##.###");
    private static final DecimalFormat CPULoadFormat = new DecimalFormat("###.#");

    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "serverInfo";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        sender.addChatMessage(new ChatComponentTranslation("CPU usage: %1$s.", getCPUUsageChatStyled()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("Memory usage: %1$s.", getMemoryUsageChatStyled()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("Server TPS: %1$s.", getServerTPSChatStyled()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        Arrays.stream(DimensionManager.getWorlds()).forEachOrdered(worldServer -> sender.addChatMessage(new ChatComponentTranslation("DIM: %1$s, TPS: %2$s, entities: %3$s, loaded chunks: %4$s.", ColorHelper.handleDimension(worldServer), getWorldTPSChatStyled(worldServer), worldServer.loadedEntityList.size(), worldServer.getChunkProvider().getLoadedChunkCount()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA))));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return Collections.emptyList();
    }

    private static ChatComponentTranslation getCPUUsageChatStyled(){
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        double CPUPercentage = osBean.getSystemCpuLoad() * 100D;
        ChatComponentTranslation textComponent = new ChatComponentTranslation("%s%%", CPULoadFormat.format(CPUPercentage));
        textComponent.getChatStyle().setColor(CPUPercentage >= 75D ? EnumChatFormatting.RED : CPUPercentage <= 50 ? EnumChatFormatting.GREEN : EnumChatFormatting.GOLD);
        return textComponent;
    }

    private static ChatComponentTranslation getMemoryUsageChatStyled(){
        double totalMem = (double) Runtime.getRuntime().maxMemory() / 1024D / 1024D;
        double usedMem = ((double) Runtime.getRuntime().totalMemory() - (double) Runtime.getRuntime().freeMemory()) / 1024D / 1024D;
        double memPercentage = usedMem / totalMem * 100;

        EnumChatFormatting MemColor = memPercentage >= 75 ? EnumChatFormatting.RED : memPercentage <= 25 ? EnumChatFormatting.GREEN : EnumChatFormatting.GOLD;

        return new ChatComponentTranslation("%sMB/ %sMB", new ChatComponentTranslation(Long.toString(Math.round(usedMem))).setChatStyle(new ChatStyle().setColor(MemColor)), Long.toString(Math.round(totalMem)));
    }

    private static ChatComponentTranslation getServerTPSChatStyled(){
        double meanTickTime = MathHelper.mean(GlobalVariables.server.tickTimeArray) * 1.0E-6D;
        double meanTPS = Math.min(1000.0 / meanTickTime, 20);

        EnumChatFormatting TPSColor = meanTPS >= 18 ? EnumChatFormatting.GREEN : meanTPS < 12 ? EnumChatFormatting.RED : EnumChatFormatting.GOLD;

        ChatComponentTranslation textComponent = new ChatComponentTranslation(TPSFormat.format(meanTPS));
        textComponent.getChatStyle().setColor(TPSColor);
        return textComponent;
    }

    private static ChatComponentTranslation getWorldTPSChatStyled(WorldServer worldServer){
        double worldTickTime = MathHelper.mean(GlobalVariables.server.worldTickTimes.get(worldServer.provider.dimensionId)) * 1.0E-6D;
        double worldTPS = Math.min(1000.0 / worldTickTime, 20);

        EnumChatFormatting TPSColor = worldTPS >= 18 ? EnumChatFormatting.GREEN : worldTPS < 16 ? EnumChatFormatting.RED : EnumChatFormatting.GOLD;

        ChatComponentTranslation textComponent = new ChatComponentTranslation(TPSFormat.format(worldTPS));
        textComponent.getChatStyle().setColor(TPSColor);
        return textComponent;
    }
}
