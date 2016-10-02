package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.ISubCommand;
import hxckdms.hxccore.registry.command.CommandRegistry;
import hxckdms.hxccore.registry.command.HxCCommand;
import net.minecraft.command.ICommandSender;

import java.util.LinkedList;
import java.util.List;

@HxCCommand(defaultPermission = 1, mainCommand = CommandRegistry.CommandHxC.class, isEnabled = true)
public class CommandFly implements ISubCommand {
    @Override
    public String getCommandName() {
        return "fly";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0};
    }

    @Override
    public void handleCommand(ICommandSender sender, LinkedList<String> args, boolean isPlayer) {
        System.out.println("hi");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
