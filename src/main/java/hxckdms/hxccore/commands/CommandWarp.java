package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
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
import java.util.*;

@HxCCommand
public class CommandWarp extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getCommandName() {
        return "warp";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    String name = "default";
                    warpPlayer(sender, (EntityPlayerMP) sender, name);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.teleport.self", name).setStyle(new Style().setColor(TextFormatting.BLUE)));
                }
                break;
            case 1:
                if (Arrays.asList(GlobalVariables.server.getPlayerList().getAllUsernames()).contains(args.get(0))) {
                    EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                    String name = "default";

                    warpPlayer(sender, target, name);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.teleport.other.sender", target.getDisplayName(), name).setStyle(new Style().setColor(TextFormatting.GRAY)));
                    target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.warp.teleport.other.target", sender.getDisplayName(), name).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                } else if (sender instanceof EntityPlayerMP) {
                    String name = args.get(0);
                    warpPlayer(sender, (EntityPlayerMP) sender, name);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.teleport.self", name).setStyle(new Style().setColor(TextFormatting.BLUE)));
                }
                break;
            case 2:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                String name = args.get(1);

                warpPlayer(sender, target, name);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.teleport.other.sender", target.getDisplayName(), name).setStyle(new Style().setColor(TextFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.warp.teleport.other.target", sender.getDisplayName(), name).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                break;
        }
    }

    private static void warpPlayer(ICommandSender sender, EntityPlayerMP player, String name) throws TranslatedCommandException {
        NBTTagCompound warps = GlobalVariables.customWorldData.hasTag("warps") ? GlobalVariables.customWorldData.getTagCompound("warps") : new NBTTagCompound();
        if (!warps.getKeySet().contains(name)) throw new TranslatedCommandException(sender, "commands.error.invalid.warp");
        NBTTagCompound warp = warps.getCompoundTag(name);

        if (GlobalVariables.server.worldServerForDimension(warp.getInteger("dimension")).getBlockState(new BlockPos(warp.getDouble("x"), warp.getDouble("y") + 1, warp.getDouble("z"))).getBlock() != Blocks.AIR && !player.capabilities.isCreativeMode)
            throw new TranslatedCommandException(sender, "commands.error.teleport.nonAir");

        TeleportHelper.teleportEntityToDimension(player, warp.getDouble("x"), warp.getDouble("y"), warp.getDouble("z"), warp.getInteger("dimension"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames());
        else if (args.size() == 2) return new ArrayList<>(GlobalVariables.customWorldData.getTagCompound("warps").getKeySet());
        else return Collections.emptyList();
    }
}
