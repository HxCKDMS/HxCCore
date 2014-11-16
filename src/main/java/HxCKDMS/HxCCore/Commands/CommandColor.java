package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.File;
import java.util.List;

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

        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
        String color = args.length == 1 ? "f" : args[1];
        NBTFileIO.setString(CustomPlayerData, "Color", color);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
