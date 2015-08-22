package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.List;

@HxCCommand(defaultPermission = 3, mainCommand = CommandMain.class)
public class CommandRepair implements ISubCommand {
    public static CommandRepair instance = new CommandRepair();

    @Override
    public String getCommandName() {
        return "repair";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Repair"), player);
            if (CanSend) {
                ItemStack HeldItem = player.getHeldItem();
                HeldItem.setMetadata(0);
                sender.addChatMessage(new ChatComponentText("\u00A7b" + HeldItem.getDisplayName() + " has been repaired."));
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
