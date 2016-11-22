package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@HxCCommand
public class CommandSetWarp extends AbstractSubCommand<CommandHxC> {
    private static DecimalFormat posFormat = new DecimalFormat("#.###");

    {
        permissionLevel = 4;
    }

    @Override
    public String getName() {
        return "setWarp";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
            case 1:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    String name = args.size() == 0 ? "default" : args.get(0);

                    setWarp(player.posX, player.posY, player.posZ, player.dimension, name);
                    sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.set", name,  posFormat.format(player.posX), posFormat.format(player.posY), posFormat.format(player.posZ), player.dimension).setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
                }
                break;
            case 4:
            case 5:
                boolean named = args.size() == 4;
                String name = named ? "default" : args.get(0);

                double x = CommandBase.parseCoordinate(sender instanceof EntityPlayerMP ? ((EntityPlayerMP) sender).posX : sender.getPosition().getX(), args.get(named ? 0 : 1), false).getResult();
                double y = CommandBase.parseCoordinate(sender instanceof EntityPlayerMP ? ((EntityPlayerMP) sender).posY : sender.getPosition().getY(), args.get(named ? 1 : 2), false).getResult();
                double z = CommandBase.parseCoordinate(sender instanceof EntityPlayerMP ? ((EntityPlayerMP) sender).posZ : sender.getPosition().getZ(), args.get(named ? 2 : 3), false).getResult();
                int dimension = CommandBase.parseInt(args.get(named ? 3 : 4));
                if (Arrays.stream(DimensionType.values()).noneMatch(type -> type.getId() == dimension)) throw new TranslatedCommandException(sender, "commands.error.invalid.dimension");

                setWarp(x, y, z, dimension, name);
                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.set", name, posFormat.format(x), posFormat.format(y), posFormat.format(z), dimension).setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
                break;
        }
    }

    private static void setWarp(double x, double y, double z, int dimension, String name) {
        NBTTagCompound warps = GlobalVariables.customWorldData.hasTag("warps") ? GlobalVariables.customWorldData.getTagCompound("warps") : new NBTTagCompound();
        NBTTagCompound warp = new NBTTagCompound();

        warp.setDouble("x", x);
        warp.setDouble("y", y);
        warp.setDouble("z", z);
        warp.setInteger("dimension", dimension);
        warps.setTag(name, warp);

        GlobalVariables.customWorldData.setTagCompound("warps", warps);
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (sender instanceof EntityPlayerMP) {
            if (args.size() == 1 || args.size() == 2 || args.size() == 3) return Collections.singletonList("~");
            else if (args.size() == 4) return Collections.singletonList(Integer.toString(((EntityPlayerMP) sender).dimension));
        }
        if (args.size() == 1) return Arrays.stream(DimensionType.values()).map(type -> Integer.toString(type.getId())).collect(Collectors.toList());
        else return Collections.emptyList();
    }
}
