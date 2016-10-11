package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.ISubCommand;
import hxckdms.hxccore.registry.command.CommandRegistry;
import hxckdms.hxccore.registry.command.HxCCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

@HxCCommand(defaultPermission = 4, mainCommand = CommandRegistry.CommandHxC.class, isEnabled = true)
public class CommandDrain implements ISubCommand {

    @Override
    public String getCommandName() {
        return "drain";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;

            final int r = args.size() >= 1 ? CommandBase.parseInt(args.get(0)) : 8;
            final Fluid fluid = args.size() >= 2 ? FluidRegistry.getFluid(args.get(1)) : null;

            final int x = (int) Math.round(player.posX);
            final int y = (int) Math.round(player.posY);
            final int z = (int) Math.round(player.posZ);

            final Predicate<BlockPos> predicate = pos -> fluid == null ?
                    player.worldObj.getBlockState(pos).getMaterial().isLiquid() || FluidRegistry.lookupFluidForBlock(player.worldObj.getBlockState(pos).getBlock()) != null :
                    player.worldObj.getBlockState(pos).getBlock() == fluid.getBlock();

            long nano = System.nanoTime();

            StreamSupport.stream(BlockPos.getAllInBox(new BlockPos(x - r, y - r, z - r), new BlockPos(x + r, y + r, z + r)).spliterator(), false).filter(predicate).forEach(player.worldObj::setBlockToAir);
            nano = System.nanoTime() - nano;


            TextComponentTranslation msg = fluid == null ? new TextComponentTranslation("commands.drain.successful.all", Integer.toString(r), String.format("%.1f", nano * 1e-9)) :
                    new TextComponentTranslation("commands.drain.successful.single", new TextComponentTranslation(fluid.getBlock().getUnlocalizedName() + ".name"), Integer.toString(r), String.format("%.1f", nano * 1e-9));

            msg.getStyle().setColor(TextFormatting.GREEN);

            player.addChatMessage(msg);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return Collections.singletonList(String.valueOf(8));
        else if (args.size() == 2) return new ArrayList<>(FluidRegistry.getRegisteredFluids().keySet());
        else return null;
    }
}