package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.List;

@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandSetWarp implements ISubCommand {
    public static CommandSetWarp instance = new CommandSetWarp();

    @Override
    public String getCommandName() {
        return "SetWarp";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("SetWarp"), player);
            if (CanSend) {
                File HxCWorldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");

                NBTTagCompound warp = NBTFileIO.getNbtTagCompound(HxCWorldData, "warp");
                NBTTagCompound warpDir = new NBTTagCompound();

                String wName = args.length == 1 ? "default" : args[1];

                int x = (int)Math.round(player.posX);
                int y = (int)Math.round(player.posY);
                int z = (int)Math.round(player.posZ);
                int dim = player.dimension;

                ChatComponentText msg = new ChatComponentText("Warp (" + wName + ") has been set to coordinates: X(" + x + ") Y(" + y + ") Z(" + z + ") Dimension(" + dim + ").");
                msg.getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE);
                player.addChatMessage(msg);

                warpDir.setInteger("x", x);
                warpDir.setInteger("y", y);
                warpDir.setInteger("z", z);
                warpDir.setInteger("dim", dim);

                warp.setTag(wName, warpDir);

                NBTFileIO.setNbtTagCompound(HxCWorldData, "warp", warp);
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
