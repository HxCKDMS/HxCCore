package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

import static HxCKDMS.HxCCore.lib.References.CC;

@SuppressWarnings({"unchecked", "unused"})
@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandAFK implements ISubCommand {
    public static CommandAFK instance = new CommandAFK();
    //TODO: make a time delay between excecutions of the command... and add a delay for the damage prevention so it can't be used as a damage prevention
    @Override
    public String getCommandName() {
        return "AFK";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayer player = (EntityPlayer)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            if (CanSend) {
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
                String tmp = player.getDisplayName() + CC + NBTFileIO.getString(CustomPlayerData, "Color");
                ChatComponentText AFK = new ChatComponentText(tmp + "  has gone AFK.");
                ChatComponentText Back = new ChatComponentText(tmp + "  is no longer AFK.");
                boolean AFKStatus;
                AFKStatus = NBTFileIO.getBoolean(CustomPlayerData, "AFK");
                NBTFileIO.setBoolean(CustomPlayerData, "AFK", !AFKStatus);

                List<EntityPlayerMP> list = HxCCore.server.getConfigurationManager().playerEntityList;
                for (EntityPlayerMP p : list)
                    p.addChatMessage(AFKStatus ? Back : AFK);
            } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2)
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        return null;
    }
}
