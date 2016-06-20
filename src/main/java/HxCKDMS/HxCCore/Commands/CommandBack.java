package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Utils.Teleporter;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

@HxCCommand(defaultPermission = 1, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandBack implements ISubCommand {
    public static CommandBack instance = new CommandBack();

    @Override
    public String getCommandName() {
        return "Back";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            int[] tmp = NBTFileIO.getIntArray(CustomPlayerData, "back");
            if (CanSend && tmp != null && tmp.length == 4) {
                if (tmp[3] != player.dimension) Teleporter.transferPlayerToDimension(player, tmp[3], tmp[0], tmp[1], tmp[2]);
                else player.playerNetServerHandler.setPlayerLocation(tmp[0], tmp[1], tmp[2], player.cameraPitch, player.cameraYaw);
            } else player.addChatMessage(new ChatComponentText("You don't have a back stored or do not have permission to run this command."));
        } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.playersonly"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
