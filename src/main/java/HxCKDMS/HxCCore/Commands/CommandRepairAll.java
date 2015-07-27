package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandRepairAll implements ISubCommand
{
    public static CommandRepairAll instance = new CommandRepairAll();

    EntityPlayer target;
    @Override
    public String getCommandName()
    {
        return "repairAll";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.PermLevels.get(11), player);
            if (CanSend) {
                if (args.length == 2) target = CommandBase.getPlayer(sender, args[1]); else target = player;
                repairItems(target);
                sender.addChatMessage(new ChatComponentText("\u00A7bAll of " + target.getDisplayName() + "'s items have been repaired."));
            } else sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
        } else {
            if (args.length == 2) repairItems(target);
            else sender.addChatMessage(new ChatComponentText("\u00A74This command without parameters can only be executed by a player."));
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
