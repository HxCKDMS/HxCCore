package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandRepairAll implements ISubCommand
{
    public static CommandRepairAll instance = new CommandRepairAll();

    @Override
    public String getCommandName()
    {
        return "repairAll";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args)
    {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            for(int j = 0; j < 36; j++)
            {
                ItemStack Inv = player.inventory.getStackInSlot(j);
                if (Inv != null && Inv.isItemStackDamageable())
                {
                    Inv.setItemDamage(0);
                }
            }
            for(int j = 0; j < 4; j++)
            {
                ItemStack Armor = player.getCurrentArmor(j);
                if (Armor != null && Armor.isItemStackDamageable())
                {
                    Armor.setItemDamage(0);
                }
            }
        }else{
            sender.addChatMessage(new ChatComponentText("\u00A74This command can only be executed by a player."));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return null;
    }
}
