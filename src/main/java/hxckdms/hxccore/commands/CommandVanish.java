package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandVanish extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "vanish";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new CommandException(ServerTranslationHelper.getTranslation(sender, "commands.error.playersOnly").getUnformattedText());
        EntityPlayerMP player = (EntityPlayerMP) sender;

        switch (args.size()) {
            case 0:
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, !HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll") ? "commands.vanish.all.disappear" : "commands.vanish.all.appear").setChatStyle(new ChatStyle().setColor(!HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll") ? EnumChatFormatting.DARK_AQUA : EnumChatFormatting.RED)));
                HxCPlayerInfoHandler.setBoolean(player, "VanishedFromAll", !HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll"));

                if (!HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll")) {
                    ((List<EntityPlayerMP>) GlobalVariables.server.getConfigurationManager().playerEntityList).stream().filter(target -> target != player).forEach(target -> target.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(player.getCommandSenderName(), true, player.ping)));
                    ((List<EntityPlayerMP>) GlobalVariables.server.getConfigurationManager().playerEntityList).stream().filter(target -> target != player).forEach(target -> target.playerNetServerHandler.sendPacket(new S0CPacketSpawnPlayer(player)));
                    ((List<EntityPlayerMP>) GlobalVariables.server.getConfigurationManager().playerEntityList).stream().filter(target -> target != player).forEach(target -> target.addChatComponentMessage(new ChatComponentText("\u00a7e" + sender.getCommandSenderName() + " joined the game")));
                } else {
                    ((List<EntityPlayerMP>) GlobalVariables.server.getConfigurationManager().playerEntityList).stream().filter(target -> target != player).forEach(target -> target.addChatComponentMessage(new ChatComponentText("\u00a7e" + sender.getCommandSenderName() + " left the game")));
                }

                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
                NBTTagList vanishList = HxCPlayerInfoHandler.getTagList(player, "VanishedFromList");
                if (vanishList == null) vanishList = new NBTTagList();
                boolean isInvisibleToTarget = false;

                for (int i = 0; i < vanishList.tagCount(); i++) {
                    if (vanishList.getStringTagAt(i).equals(target.getUniqueID().toString())) {
                        isInvisibleToTarget = true;
                        vanishList.removeTag(i);
                        break;
                    }
                }

                if (isInvisibleToTarget) {
                    HxCPlayerInfoHandler.setTagList(player, "VanishedFromList", vanishList);
                    target.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(player.getCommandSenderName(), true, player.ping));
                    target.playerNetServerHandler.sendPacket(new S0CPacketSpawnPlayer(player));

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.vanish.single.appear", target.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA)));
                    target.addChatComponentMessage(new ChatComponentText("\u00a7e" + ((EntityPlayerMP) sender).getDisplayName() + " left the game"));
                } else {
                    vanishList.appendTag(new NBTTagString(target.getUniqueID().toString()));
                    HxCPlayerInfoHandler.setTagList(player, "VanishedFromList", vanishList);

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.vanish.single.disappear", target.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                    target.addChatComponentMessage(new ChatComponentText("\u00a7e" + ((EntityPlayerMP) sender).getDisplayName() + " joined the game"));
                }
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
