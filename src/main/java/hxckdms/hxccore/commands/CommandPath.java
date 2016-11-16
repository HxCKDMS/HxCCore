package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandPath extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "path";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        Block block = args.size() >= 1 ? Block.getBlockFromName(args.get(0)) : null;
        int metadata = args.size() >= 2 ? CommandBase.parseInt(args.get(1), 0) : 0;
        int pathSize = args.size() >= 3 ? CommandBase.parseInt(args.get(2), 0) : 2;
        String override = args.size() >= 4 ? args.get(3).toLowerCase() : "all";

        ItemStack stack = null;

        HxCPlayerInfoHandler.setBoolean(player, "PathEnabled", block != null);

        if (block != null) {
            stack = new ItemStack(block, 1, metadata);

            HxCPlayerInfoHandler.setString(player, "PathMaterial", block.getRegistryName().toString());
            HxCPlayerInfoHandler.setInteger(player, "PathMetaData", metadata);
            HxCPlayerInfoHandler.setInteger(player, "PathSize", pathSize);
            HxCPlayerInfoHandler.setString(player, "Override", override);
        }
        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.path." + (block != null ? "enabled" : "disabled"), stack != null ? stack.getDisplayName() : "", 1 + 2 * pathSize).setStyle(new Style().setColor(TextFormatting.BLUE)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), Block.REGISTRY.getKeys());
        else if (args.size() == 2) return Collections.singletonList("0");
        else if (args.size() == 3) return Collections.singletonList("2");
        else if (args.size() == 4) return Arrays.asList("air", "fluid", "all");
        else return Collections.emptyList();
    }
}
