package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
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
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandVanish extends AbstractSubCommand {
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
                boolean isInvisible = player.isInvisible();
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, !isInvisible ? "commands.vanish.all.disappear" : "commands.vanish.all.appear").setStyle(new Style().setColor(!isInvisible ? TextFormatting.DARK_AQUA : TextFormatting.RED)));
                HxCPlayerInfoHandler.setBoolean(player, "VanishedFromAll", !isInvisible);
                player.setInvisible(!isInvisible);
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
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
                    target.connection.sendPacket(new SPacketEntityMetadata(player.getEntityId(), player.getDataManager(), true));
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

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}
