package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.File;
import java.util.List;
import java.util.Set;

@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandSetHome implements ISubCommand {
    public static CommandSetHome instance = new CommandSetHome();

    @Override
    public String getCommandName() {
        return "SetHome";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            int pl = PermissionsHandler.permLevel(player);
            if (CanSend) {
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                NBTTagCompound home = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");
                NBTTagCompound homeDir = new NBTTagCompound();
//getKeySet
                Set<String> oldhomes = home.func_150296_c();

                String hName = args.length == 1 ? "home" : args[1];

                if (References.HOMES[pl] != -1 && oldhomes.size() >= References.HOMES[pl])
                    if (!oldhomes.contains(hName))
                        throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.home.outOfHomes"));

                int x = (int)Math.round(player.posX);
                int y = (int)Math.round(player.posY);
                int z = (int)Math.round(player.posZ);
                int dim = player.dimension;

                ChatComponentText msg = new ChatComponentText("Home (" + hName + ") has been set to coordinates: X(" + x + ") Y(" + y + ") Z(" + z + ") Dimension(" + dim + ").");
                msg.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
                player.addChatMessage(msg);

                homeDir.setInteger("x", x);
                homeDir.setInteger("y", y);
                homeDir.setInteger("z", z);
                homeDir.setInteger("dim", dim);

                home.setTag(hName, homeDir);

                NBTFileIO.setNbtTagCompound(CustomPlayerData, "home", home);
            } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
