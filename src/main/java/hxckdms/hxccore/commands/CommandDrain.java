package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandDrain extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "drain";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;

            final int r = args.size() >= 1 ? CommandBase.parseInt(sender, args.get(0)) : 8;
            final Fluid fluid = args.size() >= 2 ? FluidRegistry.getFluid(args.get(1)) : null;

            final int x = (int) Math.round(player.posX);
            final int y = (int) Math.round(player.posY);
            final int z = (int) Math.round(player.posZ);

            long nano = System.nanoTime();

            for (int xOffset = -r; xOffset <= r; xOffset++) {
                for (int yOffset = -r; yOffset <= r; yOffset++) {
                    for (int zOffset = -r; zOffset <= r; zOffset++) {
                        if (fluid == null ? player.worldObj.getBlock(x + xOffset, y + yOffset, z + zOffset).getMaterial().isLiquid() || FluidRegistry.lookupFluidForBlock(player.worldObj.getBlock(x + xOffset, y + yOffset, z + zOffset)) != null : player.worldObj.getBlock(x + xOffset, y + yOffset, z + zOffset) == fluid.getBlock())
                            player.worldObj.setBlockToAir(x + xOffset, y + yOffset, z + zOffset);
                    }
                }
            }
            nano = System.nanoTime() - nano;

            ChatComponentTranslation msg = fluid == null ? ServerTranslationHelper.getTranslation(player, "commands.drain.successful.all", Integer.toString(r), String.format("%.1f", nano * 1e-9)) :
                    ServerTranslationHelper.getTranslation(player, "commands.drain.successful.single", new ChatComponentTranslation(fluid.getBlock().getUnlocalizedName() + ".name"), Integer.toString(r), String.format("%.1f", nano * 1e-9));

            msg.getChatStyle().setColor(EnumChatFormatting.GREEN);

            player.addChatMessage(msg);
        } else throw new CommandException(ServerTranslationHelper.getTranslation(sender, "commands.exception.playersOnly").getUnformattedText());
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        if (args.size() == 1) return Collections.singletonList(String.valueOf(8));
        else if (args.size() == 2) return new ArrayList<>(FluidRegistry.getRegisteredFluids().keySet());
        else return Collections.emptyList();
    }
}
