package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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
    public String getCommandName() {
        return "playerInfo";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) getPlayerInfo(sender, (EntityPlayerMP) sender);
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
                getPlayerInfo(sender, target);
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }

    private static void getPlayerInfo(ICommandSender sender, EntityPlayerMP player) {

        sender.addChatMessage(new ChatComponentTranslation("Player: %1$s.", getPlayerNameChatStyled(player)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("NickName: %1$s", ColorHelper.handleNick(player, false)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("Coordinates: %1$s", getPlayerCoordinatesChatStyled(player)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("Dimension: %1$s", ColorHelper.handleDimension(player)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("GameMode: %1$s", ColorHelper.handleGameMode(player)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("Operator: %1$s", getOperatorChatStyled(player)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("Permission: %1$s", ColorHelper.handlePermission(player)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("God mode: %1$s", getGodModeChatStyled(player)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("Flying: %1$s", getFlyingChatStyled(player)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("IP address: %1$s", getIPAddressChatStyled(player)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        sender.addChatMessage(new ChatComponentTranslation("Ping: %1$s", getPingChatStyled(player)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
    }

    private static ChatComponentTranslation getPlayerNameChatStyled(EntityPlayerMP player) {
        ChatComponentTranslation textComponent = new ChatComponentTranslation(player.getDisplayName());
        textComponent.getChatStyle().setColor(EnumChatFormatting.AQUA);
        return textComponent;
    }

    private static ChatComponentTranslation getPlayerCoordinatesChatStyled(EntityPlayerMP player) {
        ChatComponentTranslation textComponentX = new ChatComponentTranslation(posFormat.format(player.posX));
        textComponentX.getChatStyle().setColor(EnumChatFormatting.AQUA);
        ChatComponentTranslation textComponentY = new ChatComponentTranslation(posFormat.format(player.posY));
        textComponentY.getChatStyle().setColor(EnumChatFormatting.AQUA);
        ChatComponentTranslation textComponentZ = new ChatComponentTranslation(posFormat.format(player.posZ));
        textComponentZ.getChatStyle().setColor(EnumChatFormatting.AQUA);

        return new ChatComponentTranslation("X: %1$s, Y: %2$s, Z: %3$s", textComponentX, textComponentY, textComponentZ);
    }

    private static ChatComponentTranslation getOperatorChatStyled(EntityPlayerMP player) {
        boolean isOperator = Arrays.asList(player.mcServer.getConfigurationManager().func_152603_m().func_152685_a()).contains(player.getCommandSenderName());
        ChatComponentTranslation textComponent = new ChatComponentTranslation(isOperator ? "True" : "False");
        textComponent.getChatStyle().setColor(isOperator ? EnumChatFormatting.GOLD : EnumChatFormatting.GRAY);
        return textComponent;
    }

    private static ChatComponentTranslation getGodModeChatStyled(EntityPlayerMP player) {
        boolean godEnabled = HxCPlayerInfoHandler.getBoolean(player, "GodMode");
        ChatComponentTranslation textComponent = new ChatComponentTranslation(godEnabled ? "True" : "False");
        textComponent.getChatStyle().setColor(godEnabled ? EnumChatFormatting.GOLD : EnumChatFormatting.GRAY);
        return textComponent;
    }

    private static ChatComponentTranslation getFlyingChatStyled(EntityPlayerMP player) {
        boolean flightEnabled = HxCPlayerInfoHandler.getBoolean(player, "AllowFlying");
        ChatComponentTranslation textComponent = new ChatComponentTranslation(flightEnabled ? "True" : "False");
        textComponent.getChatStyle().setColor(flightEnabled ? EnumChatFormatting.GOLD : EnumChatFormatting.GRAY);
        return textComponent;
    }

    private static ChatComponentTranslation getIPAddressChatStyled(EntityPlayerMP player) {
        ChatComponentTranslation textComponent = new ChatComponentTranslation(player.getPlayerIP());
        textComponent.getChatStyle().setColor(EnumChatFormatting.AQUA);
        return textComponent;
    }

    private static ChatComponentTranslation getPingChatStyled(EntityPlayerMP player) {
        ChatComponentTranslation textComponent = new ChatComponentTranslation(Integer.toString(player.ping));
        textComponent.getChatStyle().setColor(player.ping <= 75 ? EnumChatFormatting.GREEN : player.ping > 200 ? EnumChatFormatting.RED : EnumChatFormatting.GOLD);
        return textComponent;
    }
}
