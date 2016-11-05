package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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
    public String getCommandName() {
        return "delWarp";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        String warpName = args.size() == 0 ? "default" : args.get(0);

        NBTTagCompound warps = GlobalVariables.customWorldData.getTagCompound("warps", new NBTTagCompound());
        if (!warps.hasKey(warpName)) throw new TranslatedCommandException(sender, "commands.error.invalid.warp", warpName);
        warps.removeTag(warpName);
        GlobalVariables.customWorldData.setTagCompound("warps", warps);

        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.removed", warpName).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? new ArrayList<>(GlobalVariables.customWorldData.getTagCompound("warps").func_150296_c()) : Collections.emptyList();
    }
}
