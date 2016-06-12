package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
//@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandKit implements ISubCommand {
    public static CommandKit instance = new CommandKit();

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
        /*if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("Kit"), player);
            if (CanSend) {
                if (args[1].equalsIgnoreCase("spawn") && args.length == 3 && Kits.Kits.keySet().contains(args[2])) {
                    List<ItemStack> items = Kits.getItems(args[2]);
                    if (Kits.canGetKit(PermissionsHandler.permLevel(player), args[2]) && items != null) {
                        items.forEach(player.inventory::addItemStackToInventory);
                        player.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("commands.kit.spawn.success", args[2])));
                    } else { throw new WrongUsageException(StatCollector.translateToLocalFormatted("commands.kit.spawn.failure", args[2]));}
                }
                if (args[1].equalsIgnoreCase("create") && args.length == 4 && !Kits.Kits.keySet().contains(args[2])) {
                    List<ItemStack> items = Arrays.asList(player.inventory.mainInventory);
                    if (!items.isEmpty()) {
                        Kits.createKit(items, args[2], Integer.parseInt(args[3]));
                        player.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("commands.kit.create.success", args[2])));
                    } else { throw new WrongUsageException(StatCollector.translateToLocalFormatted("commands.kit.create.failure", args[2]));}
                }
                if (args[1].equalsIgnoreCase("remove") && args.length == 3) {
                    if (Kits.Kits.containsKey(args[2])) {
                        Kits.removeKit(args[2]);
                        player.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("commands.kit.remove.success", args[2])));
                    } else { throw new WrongUsageException(StatCollector.translateToLocalFormatted("commands.kit.remove.failure", args[2]));}
                }
                if (args[1].equalsIgnoreCase("edit") && args.length == 4) {
                    if (Kits.Kits.containsKey(args[2])) {
                        Kits.chngKitPerms(args[2], Integer.parseInt(args[3]));
                        player.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("commands.kit.edit.success", args[2])));
                    } else { throw new WrongUsageException(StatCollector.translateToLocalFormatted("commands.kit.edit.failure", args[2]));}
                }
                if (args[1].equalsIgnoreCase("give") && args.length == 4 && Kits.Kits.keySet().contains(args[2])) {
                    List<ItemStack> items = Kits.getItems(args[2]);
                    EntityPlayerMP p = CommandsHandler.getPlayer(sender, args[3]);
                    if (Kits.canGetKit(PermissionsHandler.permLevel(player), args[2]) && items != null) {
                        items.forEach(p.inventory::addItemStackToInventory);
                        p.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("commands.kit.recieve.success", args[2])));
                        player.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("commands.kit.give.success", args[2], args[3])));
                    } else { throw new WrongUsageException(StatCollector.translateToLocalFormatted("commands.kit.give.failure", args[2], args[3]));}
                }
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));*/
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2)
            return Arrays.asList("spawn", "create", "remove", "edit", "give");
        //else if (args.length == 3)
            //return Arrays.asList((String[])Kits.Kits.keySet().toArray());
        return null;
    }
}
