package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandChill extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "chill";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        final int r = args.size() >= 1 ? CommandBase.parseInt(sender, args.get(0)) : 32;
        final int ry = args.size() >= 2 ? CommandBase.parseInt(sender, args.get(1)) : 4;

        for (int x = -r; x <= r; x++) {
            for (int y = -ry; y <= ry; y++) {
                for (int z = -r; z <= r; z++) {
                    if (player.worldObj.getBlock((int) player.posX + x, (int) player.posY + y, (int) player.posZ + z) == Blocks.fire)
                        player.worldObj.setBlockToAir((int) player.posX + x, (int) player.posY + y, (int) player.posZ + z);
                }
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? Collections.singletonList(Integer.toString(32)) : args.size() == 2 ? Collections.singletonList(Integer.toString(4)) : Collections.emptyList();
    }
}
