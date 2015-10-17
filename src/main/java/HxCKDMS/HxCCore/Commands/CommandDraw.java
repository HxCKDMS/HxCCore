package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Utils.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//DISABLED UNTIL I FINISH TWEAKING!!!
@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = false)
public class CommandDraw implements ISubCommand {
    @Override
    public String getCommandName() {
        return "Draw";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{8, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws PlayerNotFoundException, WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("Draw"), player);
            String[] args2 = new String[]{args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10]};
            if (CanSend) {
                try {
                    switch (args[1].toLowerCase()) {
                        case ("2dellipsoid"):
                            draw2DEllipsoid(player, args2);
                            break;
                        //                    case ("3dellipsoid"):
                        //                    draw3DElllipsoid(player, args2);
                        //                        break;
                        case ("2dsquircle"):
                            draw2DSquircle(player, args2);
                            break;
                        case ("3dsquircle"):
                            draw3DSquircle(player, args2);
                            break;
                        case ("circle"):
                            drawCircle(player, args2);
                            break;
                        case ("sphere"):
                            drawSphere(player, args2);
                            break;
                        default:
                            break;
                    }
                } catch (Exception ignored) {}
            }  else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.permission"));
        }  else throw new WrongUsageException(StatCollector.translateToLocal("commands." + getCommandName() + ".usage"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        String[] booleans = new String[]{Boolean.FALSE.toString(), Boolean.TRUE.toString()};

        if (args.length == 2) return Arrays.asList("Sphere", "Circle", "2DEllipsoid", "2DSquircle", "3DSquircle");
        else if (args.length == 3 || args.length == 4 || args.length == 5) return Collections.singletonList("~");
        else if (args.length == 6) return Collections.singletonList(Integer.toString(8));
        else if (args.length == 7)
            return CommandsHandler.getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys().toString().replace('[', ' ').replace(']', ' ').trim().split(", "));
        else if (args.length == 8) return CommandsHandler.getListOfStringsMatchingLastWord(args, booleans);
        return null;
    }

    public void draw2DEllipsoid(EntityPlayerMP player, String[] args) throws NumberInvalidException {
        BlockPos pos = CommandsHandler.parseBlockPos(player, args, 1, true);
        int radius = Integer.parseInt(args[3]);
        String unlocalizedName = args[4];
        boolean hollow = Boolean.parseBoolean(args[5]);
        Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
        double n = Double.parseDouble(args[6]);
        double updateAmount = args.length >= 8 ? Double.parseDouble(args[7]) : 0.05D;

        System.out.println(updateAmount);
        long cNano = System.nanoTime();
        WorldHelper.draw2DEllipsoid(player.worldObj, block.getBlockState(), pos, radius, hollow, updateAmount, n);
        ChatComponentText chatComponentText = new ChatComponentText(String.format("Successfully drew a %1$s 2D Ellipsoid at x: %2$d, y: %3$d, z: %4$d with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", pos.getX(), pos.getY(), pos.getZ(), radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000));
        chatComponentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
        player.addChatMessage(chatComponentText);
    }

    public void draw2DSquircle(EntityPlayerMP player, String[] args) throws NumberInvalidException {
        BlockPos pos = CommandsHandler.parseBlockPos(player, args, 1, true);
        int radius = Integer.parseInt(args[3]);
        String unlocalizedName = args[4];
        boolean hollow = Boolean.parseBoolean(args[5]);
        Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
        double updateAmount = args.length == 7 ? Double.parseDouble(args[6]) : 0.05D;

        long cNano = System.nanoTime();
        WorldHelper.draw2DEllipsoid(player.worldObj, block.getBlockState(), pos, radius, hollow, updateAmount, 4);
        ChatComponentText chatComponentText = new ChatComponentText(String.format("Successfully drew a %1$s squircle at x: %2$d, y: %3$d, z: %4$d with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", pos.getX(), pos.getY(), pos.getZ(), radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000));
        chatComponentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
        player.addChatMessage(chatComponentText);
    }

    public void draw3DSquircle(EntityPlayerMP player, String[] args) throws NumberInvalidException {
        BlockPos pos = CommandsHandler.parseBlockPos(player, args, 1, true);
        int radius = Integer.parseInt(args[3]);
        String unlocalizedName = args[4];
        boolean hollow = Boolean.parseBoolean(args[5]);
        Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
        double updateAmount = args.length == 7 ? Double.parseDouble(args[6]) : 0.05D;

        long cNano = System.nanoTime();
        WorldHelper.draw3DEllipsoid(player.worldObj, block.getBlockState(), pos, radius, hollow, updateAmount, 4);
        ChatComponentText chatComponentText = new ChatComponentText(String.format("Successfully drew a %1$s 3D squircle at x: %2$d, y: %3$d, z: %4$d with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", pos.getX(), pos.getY(), pos.getZ(), radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000));
        chatComponentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
        player.addChatMessage(chatComponentText);
    }


    public void drawCircle(EntityPlayerMP player, String[] args) throws NumberInvalidException {
        BlockPos pos = CommandsHandler.parseBlockPos(player, args, 1, true);
        int radius = Integer.parseInt(args[3]);
        String unlocalizedName = args[4];
        boolean hollow = Boolean.parseBoolean(args[5]);
        Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
        double updateAmount = args.length == 7 ? Double.parseDouble(args[6]) : 0.05D;

        long cNano = System.nanoTime();
        WorldHelper.draw2DEllipsoid(player.worldObj, block.getBlockState(), pos, radius, hollow, updateAmount, 2);
        ChatComponentText chatComponentText = new ChatComponentText(String.format("Successfully drew a %1$s circle at x: %2$d, y: %3$d, z: %4$d with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", pos.getX(), pos.getY(), pos.getZ(), radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000));
        chatComponentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
        player.addChatMessage(chatComponentText);
    }


    public void drawSphere(EntityPlayerMP player, String[] args) throws NumberInvalidException {
        BlockPos pos = CommandsHandler.parseBlockPos(player, args, 1, true);
        int radius = args[3].isEmpty() ? 8 : Integer.parseInt(args[3]);
        String unlocalizedName = args[4].isEmpty() ? "minecraft:stone" : args[4];
        boolean hollow = !args[5].isEmpty() && Boolean.parseBoolean(args[5]);

        Block block = (Block) Block.blockRegistry.getObject(unlocalizedName);
        double updateAmount = args.length == 7 ? Double.parseDouble(args[6]) : 0.05D;

        long cNano = System.nanoTime();
        WorldHelper.draw3DEllipsoid(player.worldObj, block.getBlockState(), pos, radius, hollow, updateAmount, 2);
        ChatComponentText chatComponentText = new ChatComponentText(String.format("Successfully drew a %1$s sphere at x: %2$d, y: %3$d, z: %4$d with a radius of %5$d with block: %6$s in %7$d seconds.", hollow ? "hollow" : "filled", pos.getX(), pos.getY(), pos.getZ(), radius, unlocalizedName, (System.nanoTime() - cNano) / 1000000000));
        chatComponentText.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
        player.addChatMessage(chatComponentText);
    }
}