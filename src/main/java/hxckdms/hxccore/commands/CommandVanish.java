package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandVanish extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getName() {
        return "vanish";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new CommandException(ServerTranslationHelper.getTranslation(sender, "commands.error.playersOnly").getUnformattedText());
        EntityPlayerMP player = (EntityPlayerMP) sender;

        switch (args.size()) {
            case 0:
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, !HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll") ? "commands.vanish.all.disappear" : "commands.vanish.all.appear").setStyle(new Style().setColor(!HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll") ? TextFormatting.DARK_AQUA : TextFormatting.RED)));
                HxCPlayerInfoHandler.setBoolean(player, "VanishedFromAll", !HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll"));

                if (!HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll")) {
                    GlobalVariables.server.getPlayerList().getPlayerList().stream().filter(target -> target != player).forEach(target -> target.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, player)));
                    GlobalVariables.server.getPlayerList().getPlayerList().stream().filter(target -> target != player).forEach(target -> target.connection.sendPacket(new SPacketSpawnPlayer(player)));
                }

                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                if (target == player) throw new TranslatedCommandException(sender, "commands.error.vanish.noSelf");

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
                    target.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, player));
                    target.connection.sendPacket(new SPacketSpawnPlayer(player));

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.vanish.single.appear", target.getDisplayName()).setStyle(new Style().setColor(TextFormatting.DARK_AQUA)));
                } else {
                    vanishList.appendTag(new NBTTagString(target.getUniqueID().toString()));
                    HxCPlayerInfoHandler.setTagList(player, "VanishedFromList", vanishList);

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.vanish.single.disappear", target.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED)));
                }
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
