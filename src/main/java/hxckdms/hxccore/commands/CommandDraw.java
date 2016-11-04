package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import hxckdms.hxccore.utilities.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandDraw extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "draw";
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        int posX = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getX(), args.get(1), true).getResult());
        int posY = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getY(), args.get(2), false).getResult());
        int posZ = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getZ(), args.get(3), true).getResult());
        Block block = args.size() >= 4 ? Block.getBlockFromName(args.get(4)) : Block.getBlockFromName("minecraft:glass");
        int metadata = args.size() >= 5 ? CommandBase.parseInt(args.get(5), 0) : 0;
        assert block != null;
        int radius = args.size() >= 6 ? CommandBase.parseInt(args.get(6)) : 8;
        boolean hollow = args.size() >= 7 || CommandBase.parseBoolean(args.get(7));
        double precision = args.size() >= 8 ? CommandBase.parseDouble(args.get(8)) : 0.005D;


        switch (args.get(0).toLowerCase()) {
            case "circle":
                WorldHelper.draw2DEllipsoid(sender.getEntityWorld(), posX, posY, posZ, block, metadata, radius, hollow, precision, 2);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.draw." + (hollow ? "hollow" : "filled") + ".circle", posX, posY, posZ, new ItemStack(block, 1, metadata).getDisplayName(), radius).setStyle(new Style().setColor(TextFormatting.BLUE)));
                break;
            case "sphere":
                WorldHelper.draw3DEllipsoid(sender.getEntityWorld(), posX, posY, posZ, block, metadata, radius, hollow, precision, 2);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.draw." + (hollow ? "hollow" : "filled") + ".sphere", posX, posY, posZ, new ItemStack(block, 1, metadata).getDisplayName(), radius).setStyle(new Style().setColor(TextFormatting.BLUE)));
                break;
            case "2dsquircle":
                WorldHelper.draw2DEllipsoid(sender.getEntityWorld(), posX, posY, posZ, block, metadata, radius, hollow, precision, 4);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.draw." + (hollow ? "hollow" : "filled") + ".2DSquircle", posX, posY, posZ, new ItemStack(block, 1, metadata).getDisplayName(), radius).setStyle(new Style().setColor(TextFormatting.BLUE)));
                break;
            case "3dsquircle":
                WorldHelper.draw3DEllipsoid(sender.getEntityWorld(), posX, posY, posZ, block, metadata, radius, hollow, precision, 4);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.draw." + (hollow ? "hollow" : "filled") + ".3DSquircle", posX, posY, posZ, new ItemStack(block, 1, metadata).getDisplayName(), radius).setStyle(new Style().setColor(TextFormatting.BLUE)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return Arrays.asList("circle", "sphere", "2dsquircle", "3dsquircle");
        else if (args.size() == 2 || args.size() == 3 || args.size() == 4) return Collections.singletonList("~");
        else if (args.size() == 5) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), Block.REGISTRY.getKeys());
        else if (args.size() == 6) return Collections.singletonList("0");
        else if (args.size() == 7) return Collections.singletonList("8");
        else if (args.size() == 8) return Collections.singletonList("true");
        else if (args.size() == 9) return Collections.singletonList("0.005");
        else return Collections.emptyList();
    }
}
