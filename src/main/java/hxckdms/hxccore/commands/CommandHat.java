package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandHat extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getCommandName() {
        return "hat";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (player.getHeldItem() == null) throw new WrongUsageException("commands.hat.usage");
            ItemStack helmet = player.inventory.armorItemInSlot(3);
            ItemStack hat = new ItemStack(player.getHeldItem().getItem(), 1, player.getHeldItem().getItemDamage());
            hat.readFromNBT(player.getHeldItem().writeToNBT(new NBTTagCompound()));
            if ((--player.getHeldItem().stackSize) <= 0) player.inventory.setInventorySlotContents(player.inventory.currentItem, null);

            player.inventory.armorInventory[3] = hat;
            player.inventory.addItemStackToInventory(helmet);

            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.hat.self", hat.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
        } else
            throw new CommandException("commands.error.playerOnly");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return Collections.emptyList();
    }
}
