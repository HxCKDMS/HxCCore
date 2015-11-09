package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.List;

@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandRepairAll implements ISubCommand {
    public static CommandRepairAll instance = new CommandRepairAll();

    EntityPlayer target;
    @Override
    public String getCommandName()
    {
        return "RepairAll";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, 1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws PlayerNotFoundException, WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("RepairAll"), player);
            if (CanSend) {
                if (args.length == 2) target = CommandsHandler.getPlayer(sender, args[1]); else target = player;
                repairItems(target);
                sender.addChatMessage(new ChatComponentText("\u00A7bAll of " + target.getDisplayName() + "'s items have been repaired."));
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else {
            if (args.length == 2) repairItems(target);
            else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
        }
    }

    public void repairItems (EntityPlayer target) {
        for(int j = 0; j < 36; j++) {
            ItemStack Inv = target.inventory.getStackInSlot(j);
            if (Inv != null && Inv.isItemStackDamageable()) Inv.setItemDamage(0);
        }
        for(int j = 0; j < 4; j++) {
            ItemStack Armor = target.getCurrentArmor(j);
            if (Armor != null && Armor.isItemStackDamageable()) Armor.setItemDamage(0);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
