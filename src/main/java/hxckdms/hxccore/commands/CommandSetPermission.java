package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.ISubCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.command.CommandRegistry;
import hxckdms.hxccore.registry.command.HxCCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.LinkedList;
import java.util.List;

@HxCCommand(defaultPermission = 5, mainCommand = CommandRegistry.CommandHxC.class, isEnabled = true)
public class CommandSetPermission implements ISubCommand {
    @Override
    public String getCommandName() {
        return "SetPermission";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args, boolean isPlayer) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;

            EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
            int level = CommandBase.parseInt(args.get(1));

            GlobalVariables.permissionData.setInteger(target.getUniqueID().toString(), level);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
