package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandPlayerInfo extends AbstractSubCommand {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "playerInfo";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) sender.addChatMessage(getPlayerInfo((EntityPlayerMP) sender));
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                sender.addChatMessage(getPlayerInfo(target));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }

    private static TextComponentTranslation getPlayerInfo(EntityPlayerMP player) {
        TextComponentTranslation textComponent = new TextComponentTranslation(
                "Player: %1$s.\n" +
                "NickName: %2$s.\n" +
                "Coordinates: %3$s.\n" +
                "Dimension: %4$s.\n" +
                "GameMode: %5$s.\n" +
                "Operator: %6$s.\n" +
                "Permission level: %7$s.\n" +
                "God mode: %8$s.\n" +
                "Flying: %9$s.\n" +
                "IP address: %10$s.\n" +
                "Ping: %11$s.",
                getPlayerNameStyled(player),
                ColorHelper.handleNick(player, false),
                getPlayerCoordinatesStyled(player),
                ColorHelper.handleDimension(player),
                ColorHelper.handleGameMode(player),
                getOperatorStyled(player),
                ColorHelper.handlePermission(player),
                getGodModeStyled(player),
                getFlyingStyled(player),
                getIPAddressStyled(player),
                getPingStyled(player));
        textComponent.getStyle().setColor(TextFormatting.BLUE);

        return textComponent;
    }

    private static TextComponentTranslation getPlayerNameStyled(EntityPlayerMP player) {
        TextComponentTranslation textComponent = new TextComponentTranslation(player.getDisplayNameString());
        textComponent.getStyle().setColor(TextFormatting.AQUA);
        return textComponent;
    }

    private static TextComponentTranslation getPlayerCoordinatesStyled(EntityPlayerMP player) {
        BlockPos pos = player.getPosition();

        TextComponentTranslation textComponentX = new TextComponentTranslation(Integer.toString(pos.getX()));
        textComponentX.getStyle().setColor(TextFormatting.AQUA);
        TextComponentTranslation textComponentY = new TextComponentTranslation(Integer.toString(pos.getY()));
        textComponentY.getStyle().setColor(TextFormatting.AQUA);
        TextComponentTranslation textComponentZ = new TextComponentTranslation(Integer.toString(pos.getZ()));
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
