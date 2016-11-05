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
public class CommandThaw extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "thaw";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        final int r = args.size() < 1 ? CommandBase.parseInt(sender, args.get(0)) : 8;

        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    if (player.worldObj.getBlock((int) player.posX + x, (int) player.posY - 1, (int) player.posZ + z) == Blocks.ice) player.worldObj.setBlock((int) player.posX + x, (int) player.posY - 1, (int) player.posZ + z, Blocks.water, 0, 3);
                    else if (player.worldObj.getBlock((int) player.posX + x, (int) player.posY - 1, (int) player.posZ + z) == Blocks.snow_layer) player.worldObj.setBlockToAir((int) player.posX + x, (int) player.posY - 1, (int) player.posZ + z);
                }
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? Collections.singletonList(Integer.toString(8)) : Collections.emptyList();
    }
}
