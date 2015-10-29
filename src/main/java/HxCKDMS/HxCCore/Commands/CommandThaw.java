package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;

import java.util.List;

@HxCCommand(defaultPermission = 1, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandThaw implements ISubCommand {
    public static CommandThaw instance = new CommandThaw();

    @Override
    public String getCommandName() {
        return "Thaw";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{1, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            if (CanSend) {
                int range = Integer.parseInt(args[1]);
                for (int x = (int)Math.round(player.posX) - range; x < (int)Math.round(player.posX) + range; x++) {
                    for (int y = (int)Math.round(player.posY) - 5; y < (int)Math.round(player.posY) + 5; y++) {
                        for (int z = (int)Math.round(player.posZ) - range; z < (int)Math.round(player.posZ) + range; z++) {
                            if (player.worldObj.getBlock(x, y, z) == Blocks.snow_layer)
                                player.worldObj.setBlockToAir(x, y, z);
                            else if (player.worldObj.getBlock(x, y, z) == Blocks.ice)
                                player.worldObj.setBlock(x, y, z, Blocks.water, 0, 3);
                        }
                    }
                }
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
