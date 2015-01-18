package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import java.io.File;
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
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            int SenderPermLevel = Permissions.getInteger(player.getName());
            boolean isopped = HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile());
            if (SenderPermLevel >= 3 || isopped) {
                for(int j = 0; j < 36; j++) {
                    ItemStack Inv = player.inventory.getStackInSlot(j);
                    if (Inv != null && Inv.isItemStackDamageable())
                    {
                        Inv.setItemDamage(0);
                    }
                }
                for(int j = 0; j < 4; j++) {
                    ItemStack Armor = player.getCurrentArmor(j);
                    if (Armor != null && Armor.isItemStackDamageable())
                    {
                        Armor.setItemDamage(0);
                    }
                }
            } else {
                sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
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
