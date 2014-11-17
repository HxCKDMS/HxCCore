package HxCKDMS.HxCCore.Commands;

import java.io.File;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.renderers.RenderHxCPlayer;

public class CommandColor implements ISubCommand {
    public static CommandColor instance = new CommandColor();

    @Override
    public String getCommandName() {
        return "color";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        EntityPlayerMP player = (EntityPlayerMP)sender;
        String UUID = player.getUniqueID().toString();

        //File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
        char color = args.length == 1 ? 'f' : args[1].toCharArray()[0];
        if ((color < 'a' || color > 'f') && (color < '0' || color > '9')) color = 'f';
        File colorData = new File(HxCCore.HxCCoreDir, "HxCColorData.dat");
        NBTFileIO.setString(colorData, UUID, String.valueOf(color));
        RenderHxCPlayer.nameColors.put(UUID, color);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
