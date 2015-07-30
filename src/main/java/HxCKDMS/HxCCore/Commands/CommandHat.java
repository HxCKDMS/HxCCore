package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CommandHat implements ISubCommand {
    public static CommandHat instance = new CommandHat();

    @Override
    public String getCommandName() {
        return "hat";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Hat"), player);
            if (CanSend) {
                ItemStack helm = player.inventory.armorItemInSlot(3);
                ItemStack hat = player.getHeldItem();
                player.inventory.setInventorySlotContents(39, hat);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, helm);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
