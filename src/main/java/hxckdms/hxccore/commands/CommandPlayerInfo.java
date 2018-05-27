package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandPlayerInfo extends AbstractSubCommand<CommandHxC> {
    private static DecimalFormat posFormat = new DecimalFormat("#.###");

    {
        permissionLevel = 4;
    }

    @Override
    public String getName() {
        return "playerInfo";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) getPlayerInfo(sender, (EntityPlayerMP) sender);
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(server, sender, args.get(0));
                getPlayerInfo(sender, target);
                break;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[0]), server.getOnlinePlayerNames()) : Collections.emptyList();
    }

    private static void getPlayerInfo(ICommandSender sender, EntityPlayerMP player) {

        sender.sendMessage(new TextComponentTranslation("Player: %1$s.", getPlayerNameStyled(player)).setStyle(new Style().setColor(TextFormatting.BLUE)));
        sender.sendMessage(new TextComponentTranslation("NickName: %1$s", ColorHelper.handleNick(player, false)).setStyle(new Style().setColor(TextFormatting.BLUE)));
        sender.sendMessage(new TextComponentTranslation("Coordinates: %1$s", getPlayerCoordinatesStyled(player)).setStyle(new Style().setColor(TextFormatting.BLUE)));
        sender.sendMessage(new TextComponentTranslation("Dimension: %1$s", ColorHelper.handleDimension(player)).setStyle(new Style().setColor(TextFormatting.BLUE)));
        sender.sendMessage(new TextComponentTranslation("GameMode: %1$s", ColorHelper.handleGameMode(player)).setStyle(new Style().setColor(TextFormatting.BLUE)));
        sender.sendMessage(new TextComponentTranslation("Operator: %1$s", getOperatorStyled(player)).setStyle(new Style().setColor(TextFormatting.BLUE)));
        sender.sendMessage(new TextComponentTranslation("Permission: %1$s", ColorHelper.handlePermission(player)).setStyle(new Style().setColor(TextFormatting.BLUE)));
        sender.sendMessage(new TextComponentTranslation("God mode: %1$s", getGodModeStyled(player)).setStyle(new Style().setColor(TextFormatting.BLUE)));
        sender.sendMessage(new TextComponentTranslation("Flying: %1$s", getFlyingStyled(player)).setStyle(new Style().setColor(TextFormatting.BLUE)));
        sender.sendMessage(new TextComponentTranslation("IP address: %1$s", getIPAddressStyled(player)).setStyle(new Style().setColor(TextFormatting.BLUE)));
        sender.sendMessage(new TextComponentTranslation("Ping: %1$s", getPingStyled(player)).setStyle(new Style().setColor(TextFormatting.BLUE)));
    }

    private static ITextComponent getPlayerNameStyled(EntityPlayerMP player) {
        ITextComponent textComponent = player.getDisplayName();
        textComponent.getStyle().setColor(TextFormatting.AQUA);
        return textComponent;
    }

    private static TextComponentTranslation getPlayerCoordinatesStyled(EntityPlayerMP player) {
        TextComponentTranslation textComponentX = new TextComponentTranslation(posFormat.format(player.posX));
        textComponentX.getStyle().setColor(TextFormatting.AQUA);
        TextComponentTranslation textComponentY = new TextComponentTranslation(posFormat.format(player.posY));
        textComponentY.getStyle().setColor(TextFormatting.AQUA);
        TextComponentTranslation textComponentZ = new TextComponentTranslation(posFormat.format(player.posZ));
        textComponentZ.getStyle().setColor(TextFormatting.AQUA);

        return new TextComponentTranslation("X: %1$s, Y: %2$s, Z: %3$s", textComponentX, textComponentY, textComponentZ);
    }

    private static TextComponentTranslation getOperatorStyled(EntityPlayerMP player) {
        boolean isOperator = Arrays.asList(player.mcServer.getPlayerList().getOppedPlayerNames()).contains(player.getName());
        TextComponentTranslation textComponent = new TextComponentTranslation(isOperator ? "True" : "False");
        textComponent.getStyle().setColor(isOperator ? TextFormatting.GOLD : TextFormatting.GRAY);
        return textComponent;
    }

    private static TextComponentTranslation getGodModeStyled(EntityPlayerMP player) {
        boolean godEnabled = HxCPlayerInfoHandler.getBoolean(player, "GodMode");
        TextComponentTranslation textComponent = new TextComponentTranslation(godEnabled ? "True" : "False");
        textComponent.getStyle().setColor(godEnabled ? TextFormatting.GOLD : TextFormatting.GRAY);
        return textComponent;
    }

    private static TextComponentTranslation getFlyingStyled(EntityPlayerMP player) {
        boolean flightEnabled = HxCPlayerInfoHandler.getBoolean(player, "AllowFlying");
        TextComponentTranslation textComponent = new TextComponentTranslation(flightEnabled ? "True" : "False");
        textComponent.getStyle().setColor(flightEnabled ? TextFormatting.GOLD : TextFormatting.GRAY);
        return textComponent;
    }

    private static TextComponentTranslation getIPAddressStyled(EntityPlayerMP player) {
        TextComponentTranslation textComponent = new TextComponentTranslation(player.getPlayerIP());
        textComponent.getStyle().setColor(TextFormatting.AQUA);
        return textComponent;
    }

    private static TextComponentTranslation getPingStyled(EntityPlayerMP player) {
        TextComponentTranslation textComponent = new TextComponentTranslation(Integer.toString(player.ping));
        textComponent.getStyle().setColor(player.ping <= 75 ? TextFormatting.GREEN : player.ping > 200 ? TextFormatting.RED : TextFormatting.GOLD);
        return textComponent;
    }
}
