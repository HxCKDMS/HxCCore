package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.registry.CommandRegistry;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandRules extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getName() {
        return "rules";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        CommandRegistry.CommandConfig.rules.forEach((rule, color) ->
                sender.addChatMessage(new TextComponentTranslation(rule).setStyle(new Style().setColor(TextFormatting.getValueByName(color))))
        );
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }
}