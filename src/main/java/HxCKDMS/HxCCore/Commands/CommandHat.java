package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

@HxCCommand(defaultPermission = 1, mainCommand = CommandsHandler.class)
public class CommandHat implements ISubCommand {
    public static CommandHat instance = new CommandHat();

    @Override
    public String getCommandName() {
        return "Hat";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if(sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.commands.get("Hat"), player);
            if (CanSend) {
                ItemStack helm = player.inventory.armorItemInSlot(3);
                ItemStack hat = player.getHeldItem();
                player.inventory.setInventorySlotContents(39, hat);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, helm);
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
