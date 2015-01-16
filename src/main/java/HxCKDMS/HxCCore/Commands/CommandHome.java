package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Utils.Teleporter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.List;


public class CommandHome implements ISubCommand {
    public static CommandHome instance = new CommandHome();

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP)sender;
        String UUID = player.getUniqueID().toString();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

        String hName = args.length == 1 ? "default" : args[1];
        NBTTagCompound homeDir = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");
        if(!homeDir.hasKey(hName)){
            throw new WrongUsageException("the home named: '" + hName + "' does not exist.");
        }
        NBTTagCompound home = homeDir.getCompoundTag(hName);
        Teleporter.transferPlayerToDimension(player, home.getInteger("dim"), player.mcServer.getConfigurationManager(), home.getInteger("x"), home.getInteger("y"), home.getInteger("z"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
