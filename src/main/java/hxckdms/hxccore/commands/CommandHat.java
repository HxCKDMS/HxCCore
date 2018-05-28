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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandHat extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getName() {
        return "hat";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (player.getHeldItemMainhand().isEmpty()) throw new WrongUsageException("commands.hat.usage");
            ItemStack helmet = player.inventory.armorInventory.get(3);
            ItemStack hat = new ItemStack(player.getHeldItemMainhand().getItem(), 1, player.getHeldItemMainhand().getItemDamage());
            hat.deserializeNBT(player.getHeldItem(EnumHand.MAIN_HAND).writeToNBT(new NBTTagCompound()));
            player.getHeldItemMainhand().shrink(1);

            player.inventory.armorInventory.set(3, hat);
            player.inventory.addItemStackToInventory(helmet);

            sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.hat.self", hat.getDisplayName()).setStyle(new Style().setColor(TextFormatting.BLUE)));
        } else
            throw new CommandException("commands.error.playerOnly");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }
}
