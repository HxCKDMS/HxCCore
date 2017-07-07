package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
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
public class CommandDelWarp extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getName() {
        return "delWarp";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        String warpName = args.size() == 0 ? "default" : args.get(0);

        NBTTagCompound warps = GlobalVariables.customWorldData.getTagCompound("warps", new NBTTagCompound());
        if (!warps.hasKey(warpName)) throw new TranslatedCommandException(sender, "commands.error.invalid.warp", warpName);
        warps.removeTag(warpName);
        GlobalVariables.customWorldData.setTagCompound("warps", warps);

        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.removed", warpName).setStyle(new Style().setColor(TextFormatting.DARK_RED)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? new ArrayList<>(GlobalVariables.customWorldData.getTagCompound("warps").getKeySet()) : Collections.emptyList();
    }
}
