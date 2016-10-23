package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@HxCCommand
public class CommandSetWarp extends AbstractSubCommand {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
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
                    int x = (int) Math.round(player.posX);
                    int y = (int) Math.round(player.posY);
                    int z = (int) Math.round(player.posZ);
                    int dimension = player.dimension;

                    setWarp(x, y, z, dimension, name);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.set", name, x, y, z, dimension).setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
                }
                break;
            case 4:
            case 5:
                boolean named = args.size() == 4;
                String name = named ? "default" : args.get(0);
                int x = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getX(), args.get(named ? 0 : 1), false).getAmount());
                int y = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getY(), args.get(named ? 1 : 2), false).getAmount());
                int z = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getZ(), args.get(named ? 2 : 3), false).getAmount());
                int dimension = CommandBase.parseInt(args.get(named ? 3 : 4));
                if (!Arrays.stream(DimensionType.values()).anyMatch(type -> type.getId() == dimension)) throw new TranslatedCommandException(sender, "commands.error.invalid.dimension");

                setWarp(x, y, z, dimension, name);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.set", name, x, y, z, dimension).setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
                break;
        }
    }

    private static void setWarp(int x, int y, int z, int dimension, String name) {
        NBTTagCompound warps = GlobalVariables.customWorldData.hasTag("warps") ? GlobalVariables.customWorldData.getTagCompound("warps") : new NBTTagCompound();
        NBTTagCompound warp = new NBTTagCompound();

        warp.setInteger("x", x);
        warp.setInteger("y", y);
        warp.setInteger("z", z);
        warp.setInteger("dimension", dimension);
        warps.setTag(name, warp);

        GlobalVariables.customWorldData.setTagCompound("warps", warps);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (pos != null) {
            if (args.size() == 1) return Collections.singletonList(Integer.toString(pos.getX()));
            else if (args.size() == 2) return Collections.singletonList(Integer.toString(pos.getY()));
            else if (args.size() == 3) return Collections.singletonList(Integer.toString(pos.getZ()));
        }
        if (args.size() == 4) return Arrays.stream(DimensionType.values()).map(type -> Integer.toString(type.getId())).collect(Collectors.toList());
        return Collections.emptyList();
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}
