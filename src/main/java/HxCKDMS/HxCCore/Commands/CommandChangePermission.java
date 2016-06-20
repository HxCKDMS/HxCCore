package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.List;
import java.util.Objects;

import static HxCKDMS.HxCCore.lib.References.*;

@HxCCommand(defaultPermission = 5, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandChangePermission implements ISubCommand {
    public static CommandChangePermission instance = new CommandChangePermission();

    @Override
    public String getCommandName() {
        return "ChangePermission";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{2, 2, -1};
    }

    @Override
    @SuppressWarnings("all")
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            if (CanSend) {
                int a = Integer.parseInt(args[1]);
                if (!(a > References.PERM_NAMES.length+1)) {
                    Configurations.Permissions.remove(References.PERM_NAMES[a]);
                    References.PERM_NAMES[a] = args[2];
                    if (args.length == 4 && !Objects.equals(args[3], ""))
                        References.PERM_COLOURS[a] = args[3].charAt(0);
                    Configurations.Permissions.put(References.PERM_NAMES[a], String.valueOf(References.PERM_COLOURS[a]));
                } else {
                    Configurations.Permissions.put(References.PERM_NAMES[a], String.valueOf(References.PERM_COLOURS[a]));
                    String[] StorageA = References.PERM_NAMES;
                    char[] StorageB = References.PERM_COLOURS;
                    References.PERM_NAMES = new String[StorageA.length+1];
                    References.PERM_COLOURS = new char[StorageB.length+1];
                    for (int i = 0; i < References.PERM_NAMES.length; i++) {
                        PERM_NAMES[i] = (String) Configurations.Permissions.keySet().toArray()[i];
                        String[] temp = Configurations.Permissions.get(PERM_NAMES[i]).replaceAll(" ", "").split(",");
                        PERM_COLOURS[i] = temp[0].charAt(0);
                        HOMES[i] = Integer.parseInt(temp[1]);
                        PROTECT_SIZE[i] = Long.parseLong(temp[2]);
                    }
                }
                ((EntityPlayerMP) sender).addChatComponentMessage(new ChatComponentText("The changes made by this command are temporary."));
            } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return References.COLOR_CHARS_STRING;
    }
}
