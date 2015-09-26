package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;

import java.util.List;

//@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class)
public class CommandMakeItRain implements ISubCommand {
    public static CommandMakeItRain instance = new CommandMakeItRain();

    @Override
    public String getCommandName() {
        return "Mir";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.commands.get("Mir"), player);
            int speed = 2;
            boolean isKitty = false;
            if (args.length >= 2) { speed = Integer.parseInt(args[1]);}
            if (args.length == 3 && args[2].equalsIgnoreCase("kitty")) {isKitty = true;}
            if (CanSend) {
//                Vec3 vec = player.getLookVec();
//                double x = vec.xCoord, y = vec.yCoord, z = vec.zCoord;
//                EntityTNTPrimed tnt = new EntityTNTPrimed(player.worldObj, player.posX, player.posY+player.height, player.posZ, player);
//                EntityOcelot kitty = new EntityOcelot(player.worldObj);
//                kitty.setPosition(player.posX, player.posY+player.height, player.posZ);
//                kitty.setVelocity(x * speed, y * speed, z * speed);
//                tnt.setVelocity(x * speed, y * speed, z * speed);
//                player.worldObj.spawnEntityInWorld(isKitty ? kitty : tnt);
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
