package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandWarp extends AbstractSubCommand {
    @Override
    public String getCommandName() {
        return "warp";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 1:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    String name = args.get(0);
                    NBTTagCompound warps = GlobalVariables.customWorldData.hasTag("warps") ? GlobalVariables.customWorldData.getTagCompound("warps") : new NBTTagCompound();
                    if (!warps.getKeySet().contains(name)) throw new CommandException(ServerTranslationHelper.getTranslation(sender, "commands.error.invalid.warp").getUnformattedText());
                    NBTTagCompound warp = warps.getCompoundTag(name);

                    if (GlobalVariables.server.worldServerForDimension(warp.getInteger("dimension")).getBlockState(new BlockPos(warp.getInteger("x"), warp.getInteger("y") + 1, warp.getInteger("z"))).getBlock() != Blocks.AIR && !player.capabilities.isCreativeMode)
                        throw new CommandException(ServerTranslationHelper.getTranslation(sender, "commands.error.teleport.nonAir").getUnformattedText());

                    TeleportHelper.teleportEntityToDimension(player, warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z"), warp.getInteger("dimension"));
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.teleport.self", name).setStyle(new Style().setColor(TextFormatting.BLUE)));
                }
                break;
            case 2:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                String name = args.get(1);
                NBTTagCompound warps = GlobalVariables.customWorldData.hasTag("warps") ? GlobalVariables.customWorldData.getTagCompound("warps") : new NBTTagCompound();
                if (!warps.getKeySet().contains(name)) throw new CommandException(ServerTranslationHelper.getTranslation(sender, "commands.error.invalid.warp").getUnformattedText());
                NBTTagCompound warp = warps.getCompoundTag(name);

                if (GlobalVariables.server.worldServerForDimension(warp.getInteger("dimension")).getBlockState(new BlockPos(warp.getInteger("x"), warp.getInteger("y") + 1, warp.getInteger("z"))).getBlock() != Blocks.AIR && !target.capabilities.isCreativeMode)
                    throw new CommandException(ServerTranslationHelper.getTranslation(sender, "commands.error.teleport.nonAir").getUnformattedText());

                TeleportHelper.teleportEntityToDimension(target, warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z"), warp.getInteger("dimension"));
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.teleport.other.sender", target.getDisplayName(), name).setStyle(new Style().setColor(TextFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.warp.teleport.other.target", sender.getDisplayName(), name).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames());
        else if (args.size() == 2) return new ArrayList<>(GlobalVariables.customWorldData.getTagCompound("warps").getKeySet());
        else return Collections.emptyList();
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}
