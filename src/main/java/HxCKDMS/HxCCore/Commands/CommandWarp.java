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


public class CommandWarp implements ISubCommand {
    public static CommandWarp instance = new CommandWarp();

    @Override
    public String getName() {
        return "warp";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP)sender;
        File HxCWorldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");

        String wName = args.length == 1 ? "default" : args[1];
        NBTTagCompound warpDir = NBTFileIO.getNbtTagCompound(HxCWorldData, "warp");
        if(!warpDir.hasKey(wName)){
            throw new WrongUsageException("the warp named: '" + wName + "' does not exist.");
        }
        NBTTagCompound warp = warpDir.getCompoundTag(wName);
        Teleporter.transferPlayerToDimension(player, warp.getInteger("dim"), player.mcServer.getConfigurationManager(), warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
