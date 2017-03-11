package hxckdms.hxccore.commands;

import com.sun.management.OperatingSystemMXBean;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.CommandRegistry;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.MathHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandRules extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getCommandName() {
        return "rules";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        CommandRegistry.CommandConfig.rules.forEach((rule, color) ->
                sender.addChatMessage(new ChatComponentTranslation(rule).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.getValueByName(color))))
        );
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return Collections.emptyList();
    }
}
