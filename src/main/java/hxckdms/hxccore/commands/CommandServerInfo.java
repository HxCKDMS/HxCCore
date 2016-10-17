package hxckdms.hxccore.commands;

import com.sun.management.OperatingSystemMXBean;
import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.MathHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandServerInfo extends AbstractSubCommand {
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
        TextComponentTranslation textComponent = new TextComponentTranslation(
                "CPU usage: %1$s.\n" +
                "Memory usage: %2$s.\n" +
                "Server TPS: %3$s.",
                getCPUUsageStyled(),
                getMemoryUsageStyled(),
                getServerTPSStyled());
        textComponent.getStyle().setColor(TextFormatting.BLUE);

        Arrays.stream(DimensionManager.getWorlds()).forEachOrdered(worldServer -> textComponent.appendSibling(new TextComponentTranslation("\nDIM: %1$s, TPS: %2$s, entities: %3$s, loaded chunks: %4$s.", ColorHelper.handleDimension(worldServer), getWorldTPSStyled(worldServer), worldServer.loadedEntityList.size(), worldServer.getChunkProvider().getLoadedChunkCount()).setStyle(new Style().setColor(TextFormatting.AQUA))));

        sender.addChatMessage(textComponent);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }

    private static TextComponentTranslation getCPUUsageStyled(){
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        double CPUPercentage = osBean.getSystemCpuLoad() * 100D;
        TextComponentTranslation textComponent = new TextComponentTranslation("%s%%", CPULoadFormat.format(CPUPercentage));
        textComponent.getStyle().setColor(CPUPercentage >= 75D ? TextFormatting.RED : CPUPercentage <= 50 ? TextFormatting.GREEN : TextFormatting.GOLD);
        return textComponent;
    }

    private static TextComponentTranslation getMemoryUsageStyled(){
        double totalMem = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        double usedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;
        double memPercentage = usedMem / totalMem * 100;

        TextFormatting MemColor = memPercentage >= 75 ? TextFormatting.RED : memPercentage <= 25 ? TextFormatting.GREEN : TextFormatting.GOLD;

        return new TextComponentTranslation("%sMB/ %sMB", new TextComponentTranslation(Long.toString(Math.round(usedMem))).setStyle(new Style().setColor(MemColor)), Long.toString(Math.round(totalMem)));
    }

    private static TextComponentTranslation getServerTPSStyled(){
        double meanTickTime = MathHelper.mean(GlobalVariables.server.tickTimeArray) * 1.0E-6D;
        double meanTPS = Math.min(1000.0 / meanTickTime, 20);

        TextFormatting TPSColor = meanTPS >= 18 ? TextFormatting.GREEN : meanTPS < 12 ? TextFormatting.RED : TextFormatting.GOLD;

        TextComponentTranslation textComponent = new TextComponentTranslation(TPSFormat.format(meanTPS));
        textComponent.getStyle().setColor(TPSColor);
        return textComponent;
    }

    private static TextComponentTranslation getWorldTPSStyled(WorldServer worldServer){
        double worldTickTime = MathHelper.mean(GlobalVariables.server.worldTickTimes.get(worldServer.provider.getDimension())) * 1.0E-6D;
        double worldTPS = Math.min(1000.0 / worldTickTime, 20);

        TextFormatting TPSColor = worldTPS >= 18 ? TextFormatting.GREEN : worldTPS < 16 ? TextFormatting.RED : TextFormatting.GOLD;

        TextComponentTranslation textComponent = new TextComponentTranslation(TPSFormat.format(worldTPS));
        textComponent.getStyle().setColor(TPSColor);
        return textComponent;
    }
}
