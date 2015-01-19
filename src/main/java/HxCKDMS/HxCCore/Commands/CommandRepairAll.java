package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandRepairAll implements ISubCommand
{
    public static CommandRepairAll instance = new CommandRepairAll();

    EntityPlayer target;
    @Override
    public String getName()
    {
        return "repairAll";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws PlayerNotFoundException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            int SenderPermLevel = Permissions.getInteger(player.getName());
            boolean isopped = HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile());
            if (SenderPermLevel >= 3 || isopped) {
                if (args.length == 2) {
                    target = CommandBase.getPlayer(sender, args[1]);
                } else {
                    target = player;
                }
                repairItems(target);
                sender.addChatMessage(new ChatComponentText("\u00A7bAll of " + target.getDisplayName() + "'s items have been repaired."));
            } else {
                sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
            }

        }else{
            if (args.length == 2) {
                repairItems(target);
            } else {
                sender.addChatMessage(new ChatComponentText("\u00A74This command without parameters can only be executed by a player."));
            }
        }
    }

    public void repairItems (EntityPlayer target) {
        for(int j = 0; j < 36; j++) {
            ItemStack Inv = target.inventory.getStackInSlot(j);
            if (Inv != null && Inv.isItemStackDamageable())
            {
                Inv.setItemDamage(0);
            }
        }
        for(int j = 0; j < 4; j++) {
            ItemStack Armor = target.getCurrentArmor(j);
            if (Armor != null && Armor.isItemStackDamageable())
            {
                Armor.setItemDamage(0);
            }
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
