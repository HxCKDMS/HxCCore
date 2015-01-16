package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class CommandSmite implements ISubCommand {
    public static CommandSmite instance = new CommandSmite();

    @Override
    public String getName() {
        return "smite";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP)sender;

        player.worldObj.spawnEntityInWorld(new EntityLightningBolt(player.worldObj, player.posX+5, player.posY, player.posZ+5));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
