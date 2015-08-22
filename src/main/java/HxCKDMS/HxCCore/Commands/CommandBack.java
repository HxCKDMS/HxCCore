package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Utils.Teleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.List;

@HxCCommand(defaultPermission = 1, mainCommand = CommandMain.class)
public class CommandBack implements ISubCommand {
    public static CommandBack instance = new CommandBack();

    @Override
    public String getCommandName() {
        return "Back";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Back"), player);
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            int[] tmp = NBTFileIO.getIntArray(CustomPlayerData, "back");
            if (CanSend && tmp != null && tmp.length == 4) {
                int[] nb = new int[]{(int)player.posX, (int)player.posY, (int)player.posZ, player.dimension};
                if (tmp[4] != player.dimension) Teleporter.transferPlayerToDimension(player, tmp[4], tmp[0], tmp[1], tmp[2]);
                else player.playerNetServerHandler.setPlayerLocation(tmp[0], tmp[1], tmp[2], player.cameraPitch, player.cameraYaw);
                NBTFileIO.setIntArray(CustomPlayerData, "back", nb);
            }
        } else  throw new WrongUsageException(StatCollector.translateToLocal("command.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
