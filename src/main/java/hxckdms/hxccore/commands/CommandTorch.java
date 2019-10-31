package hxckdms.hxccore.commands;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandTorch extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "torch";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP))
            throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        final int r = args.size() >= 1 ? CommandBase.parseInt(sender, args.get(0)) : 16;
        final int ry = args.size() >= 2 ? CommandBase.parseInt(sender, args.get(1)) : 2;


        if (Loader.isModLoaded("TerraFirmaCraft") || Loader.isModLoaded("terrafirmacraft")) {
            for (int x = -r; x <= r; x++) {
                for (int y = -ry; y <= ry; y++) {
                    for (int z = -r; z <= r; z++) {
                        if (player.worldObj.getBlock((int) player.posX + x, (int) player.posY + y, (int) player.posZ + z) == GameRegistry.findBlock("terrafirmacraft", "TorchOff"))
                            player.worldObj.setBlock((int) player.posX + x, (int) player.posY + y, (int) player.posZ + z, GameRegistry.findBlock("terrafirmacraft", "Torch"));
                    }
                }
            }
        }

        List<ItemBlock> lightSources = getLightSources(player);

        if (!lightSources.isEmpty()) {
            ItemBlock curLight = lightSources.remove(0);
            for (int x = -r; x <= r; x++) {
                for (int y = -ry; y <= ry; y++) {
                    for (int z = -r; z <= r; z++) {
                        if (player.inventory.hasItem(curLight) || (!lightSources.isEmpty() && player.inventory.hasItem(curLight = lightSources.remove(0)))) {
                            if (player.worldObj.isAirBlock((int) player.posX + x, (int) player.posY + y, (int) player.posZ + z) && isNearGround(player.worldObj, (int) player.posX + x, (int) player.posY + y, (int) player.posZ + z) && isTorchSafe(player.worldObj, (int) player.posX + x, (int) player.posY + y, (int) player.posZ + z) && removeLightSource(player, curLight)) {
                                player.worldObj.setBlock((int) player.posX + x, (int) player.posY + y, (int) player.posZ + z, curLight.field_150939_a);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? Collections.singletonList(Integer.toString(32)) : args.size() == 2 ? Collections.singletonList(Integer.toString(4)) : Collections.emptyList();
    }

    public static boolean isNearGround(World world, int x, int y, int z) {
        if (world.isAirBlock(x, y - 1, z) && world.isAirBlock(x, y - 2, z))
            return false;
        return true;
    }

    public static List<ItemBlock> getLightSources(EntityPlayerMP player) {
        List<ItemBlock> lights = new ArrayList<>();
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (itemStack != null && itemStack.getMaxStackSize() > 1 && (itemStack.getItem() instanceof ItemBlock && (((ItemBlock) itemStack.getItem()).field_150939_a.getLightValue() > 7))) {
                lights.add((ItemBlock) itemStack.getItem());
            }
        }
        return lights;
    }

    public boolean removeLightSource(EntityPlayerMP player, ItemBlock light) {
        return player.inventory.consumeInventoryItem(light);
    }

    public static boolean isTorchSafe(World world, int x, int y, int z) {
        ArrayList<Block> blocks = getLightBlocksWithinAABB(world, getAreaBoundingBox(x, y, z, 5));
        if (Loader.isModLoaded("TerraFirmaCraft") || Loader.isModLoaded("terrafirmacraft") && blocks.contains(GameRegistry.findBlock("terrafirmacraft", "Torch")))
            return false;
        if (world.isAirBlock(x, y -1, z) && world.isAirBlock(x, y, z - 1) && world.isAirBlock(x, y, z + 1) && world.isAirBlock(x - 1, y, z) && world.isAirBlock(x + 1, y, z))
            return false;
        return blocks.isEmpty();
    }

    public static ArrayList<Block> getLightBlocksWithinAABB(World world, AxisAlignedBB box) {
        ArrayList<Block> blocks = new ArrayList<>();

        for(int x = (int) box.minX; x <= box.maxX; x++) {
            for(int y = (int) box.minY; y <= box.maxY; y++) {
                for(int z = (int) box.minZ; z <= box.maxZ; z++) {
                    Block block = world.getBlock(x, y, z);
                    if(block != null && block.getLightValue() >= 8)
                        blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public static AxisAlignedBB getAreaBoundingBox(int x, int y, int z, int radius) {
        return AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius,
                x + 0.99 + radius, y + 0.99 + radius, z + 0.99 + radius);
    }
}
