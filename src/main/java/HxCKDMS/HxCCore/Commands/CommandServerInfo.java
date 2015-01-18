package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.List;

public class CommandServerInfo implements ISubCommand {
    public static CommandServerInfo instance = new CommandServerInfo();

    @Override
    public String getCommandName() {
        return "serverInfo";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        double totalMem = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        double usedMem = totalMem - (Runtime.getRuntime().freeMemory() / 1024 / 1024);
        double memPercentage = usedMem / totalMem * 100;
        EnumChatFormatting TPSColor;

        EnumChatFormatting MemColor = memPercentage >= 75 ? EnumChatFormatting.RED : memPercentage <= 25 ? EnumChatFormatting.GREEN : EnumChatFormatting.GOLD;

        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "Memory: " + MemColor + (int)usedMem + "MB/ " + EnumChatFormatting.BLUE + (int)totalMem + "MB."));

        double MeanTickTime = mean(HxCCore.server.tickTimeArray) * 1.0E-6D;
        double MeanTPS = Math.min(1000.0/MeanTickTime, 20);

        TPSColor = MeanTPS >= 18 ? EnumChatFormatting.GREEN : MeanTPS < 16 ? EnumChatFormatting.RED : EnumChatFormatting.GOLD;

        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Server TPS: " + TPSColor + MeanTPS + EnumChatFormatting.AQUA + "."));

        for(WorldServer worldServer : DimensionManager.getWorlds()){
            double WorldTickTime = mean(HxCCore.server.worldTickTimes.get(worldServer.provider.getDimensionId())) * 1.0E-6D;
            double WorldTPS = Math.min(1000.0/WorldTickTime, 20);
            String DimName = worldServer.provider.getDimensionName();
            EnumChatFormatting DimColor = DimName.equalsIgnoreCase("the end") ? EnumChatFormatting.YELLOW : DimName.equalsIgnoreCase("nether") ? EnumChatFormatting.RED : DimName.equalsIgnoreCase("overworld") ? EnumChatFormatting.GREEN : EnumChatFormatting.WHITE;

            TPSColor = WorldTPS >= 18 ? EnumChatFormatting.GREEN : MeanTPS < 16 ? EnumChatFormatting.RED : EnumChatFormatting.GOLD;

            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "DIM: " + DimColor + worldServer.provider.getDimensionName() + EnumChatFormatting.AQUA + ", TPS: " + TPSColor + WorldTPS + EnumChatFormatting.AQUA + ", entities: " + worldServer.loadedEntityList.size() + ", loaded chunks: " + worldServer.getChunkProvider().getLoadedChunkCount() + "."));
        }
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
}
