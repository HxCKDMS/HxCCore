package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

@HxCCommand(defaultPermission = 3, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandSmite implements ISubCommand {
    public static CommandSmite instance = new CommandSmite();

    @Override
    public String getCommandName() {
        return "Smite";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws PlayerNotFoundException, WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("Smite"), player);
            if (CanSend) {
                if (args.length == 2) {
                    EntityPlayerMP target = CommandsHandler.getPlayer(sender, args[1]);
                    smite(target.worldObj, target.getPosition());
                } else {
                    MovingObjectPosition rayTrace = player.rayTrace(100, 1.0F);
                    if (player.rayTrace(100, 1.0F).typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) smite(player.worldObj, rayTrace.entityHit.getPosition());
                    else if(player.rayTrace(100, 1.0F).typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) smite(player.worldObj, rayTrace.getBlockPos());
                    else smite(player.worldObj, new BlockPos(player.getLookVec().xCoord * 50D + player.posX, player.getLookVec().yCoord * 50D + player.posY, player.getLookVec().zCoord * 50D + player.posZ));

                }
            }  else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else {
            EntityPlayerMP target = CommandsHandler.getPlayer(sender, args[1]);
            if (args.length == 2) smite(target.worldObj, target.getPosition());
             else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
        }
    }

    public void smite(World world, BlockPos pos) {
        world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ()));
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
