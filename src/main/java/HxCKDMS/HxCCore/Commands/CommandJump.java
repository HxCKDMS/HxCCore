package HxCKDMS.HxCCore.Commands;


import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;

import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandJump implements ISubCommand {
    public static CommandJump instance = new CommandJump();

    @Override
    public String getCommandName() {
        return "Jump";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("Jump"), player);
            if (CanSend) {
                Vec3 vec3 = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
                Vec3 vec31 = player.getLook(1.0f);
                Vec3 vec32 = vec3.addVector(vec31.xCoord * 200, vec31.yCoord * 200, vec31.zCoord * 200);
                MovingObjectPosition rayTrace = player.worldObj.rayTraceBlocks(vec3, vec32);
                if (rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                    player.playerNetServerHandler.setPlayerLocation(rayTrace.blockX, rayTrace.blockY, rayTrace.blockZ, player.rotationYawHead, player.rotationPitch);
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}