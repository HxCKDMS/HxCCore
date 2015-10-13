package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Kits;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = false)
public class CommandKit implements ISubCommand {
    public static CommandBroadcast instance = new CommandBroadcast();

    @Override
    public String getCommandName() {
        return "Kit";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{1, 1, 1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("Kit"), player);
            if (CanSend) {
                if (args[1].equalsIgnoreCase("spawn") && args.length == 3) {
                    if (Kits.canGetKit(PermissionsHandler.permLevel(player), args[2])) {
                        for (ItemStack stack : Kits.getItems(args[2])) {
                            player.inventory.addItemStackToInventory(stack);
                        }
                        player.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("commands.kit.spawn.success", args[2])));
                    } else throw new WrongUsageException(StatCollector.translateToLocalFormatted("commands.kit.spawn.failure", args[2]));
                }
            }
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("spawn", "create", "remove", "edit", "give");
        } else if (args.length == 3) {
            return Arrays.asList((String[])Kits.Kits.keySet().toArray());
        }
        return null;
    }
}
