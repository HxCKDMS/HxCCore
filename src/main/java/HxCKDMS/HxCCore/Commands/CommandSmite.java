package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

@HxCCommand(defaultPermission = 3, mainCommand = CommandsHandler.class)
public class CommandSmite implements ISubCommand {
    public static CommandSmite instance = new CommandSmite();

    @Override
    public String getCommandName() {
        return "Smite";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws PlayerNotFoundException, WrongUsageException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.commands.get("Smite"), player);
            if (CanSend) {
                if (args.length == 2) {
                    EntityPlayerMP target = CommandsHandler.getPlayer(sender, args[1]);
                    smite(target.worldObj, target.posX, target.posY, target.posZ);
                } else {
                    MovingObjectPosition rayTrace = player.rayTrace(100, 1.0F);
                    if (player.rayTrace(100, 1.0F).typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) smite(player.worldObj, rayTrace.entityHit.posX, rayTrace.entityHit.posY, rayTrace.entityHit.posZ);
                    else if(player.rayTrace(100, 1.0F).typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) smite(player.worldObj, rayTrace.blockX, rayTrace.blockY, rayTrace.blockZ);
                    else smite(player.worldObj, player.getLookVec().xCoord * 50D + player.posX, player.getLookVec().yCoord * 50D + player.posY, player.getLookVec().zCoord * 50D + player.posZ);

                }
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else {
            EntityPlayerMP target = CommandsHandler.getPlayer(sender, args[1]);
            if (args.length == 2) smite(target.worldObj, target.posX, target.posY, target.posZ);
            else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
        }
    }

    public void smite(World world, double x, double y, double z) {
        world.addWeatherEffect(new EntityLightningBolt(world, x, y, z));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
