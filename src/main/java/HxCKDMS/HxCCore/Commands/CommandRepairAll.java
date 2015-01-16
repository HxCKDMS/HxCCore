package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

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
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args)
    {
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
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return null;
    }
}
