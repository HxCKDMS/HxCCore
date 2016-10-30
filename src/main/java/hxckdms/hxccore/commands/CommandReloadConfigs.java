package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandReloadConfigs extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "reloadConfigs";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        GlobalVariables.mainConfig.initConfiguration();
        GlobalVariables.commandConfig.initConfiguration();
        GlobalVariables.kitConfig.initConfiguration();
        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.reloadConfigs.reloaded").setStyle(new Style().setColor(TextFormatting.GRAY)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }
}
