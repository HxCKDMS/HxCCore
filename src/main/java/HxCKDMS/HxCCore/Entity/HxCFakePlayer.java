package HxCKDMS.HxCCore.Entity;

import HxCKDMS.HxCCore.network.NetHandlerFakePlayServer;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.UUID;

@SuppressWarnings("unused")
public class HxCFakePlayer extends FakePlayer {
    public boolean sneaking = false;
    public ItemStack prevItemStack = null;
    static String name = "[HxC]";
    
    private static GameProfile gameProfile = new GameProfile(UUID.fromString("a4a626f2-aca9-11e4-89d3-123b93f75cba"), "[HxC]");
    
    public HxCFakePlayer(WorldServer world) {
        super(world, gameProfile);
        playerNetServerHandler = new NetHandlerFakePlayServer(FMLCommonHandler.instance().getMinecraftServerInstance(), this);
        this.addedToChunk = false;
    }
    
    public static boolean isBlockBreakable(HxCFakePlayer fakePlayer, World world, int x, int y, int z){
        Block block = world.getBlock(x, y , z);
        if(fakePlayer == null){
            return block.getBlockHardness(world, x, y, z) >= 0;
        }else{
            return block.getPlayerRelativeBlockHardness(fakePlayer, world, x, y, z) >= 0;
        }
    }
    
    public void setItemInHand(ItemStack itemInHand){
        this.inventory.setInventorySlotContents(0, itemInHand);
        this.inventory.currentItem = 0;
    }
    
    public void setItemInHand(int slot){
        this.inventory.currentItem = slot;
    }

    @Override
    public double getDistanceSq(double x, double y, double z) {
        return 0D;
    }

    @Override
    public double getDistance(double x, double y, double z) {
        return 0D;
    }
    
    @Override
    public boolean isSneaking(){
        return sneaking;
    }
    
    @Override
    public void onUpdate(){
        ItemStack itemStack1 = prevItemStack;
        ItemStack itemStack2 = getHeldItem();
        if (!ItemStack.areItemStacksEqual(itemStack2, itemStack1)) {
            if (itemStack1 != null) {
                getAttributeMap().removeAttributeModifiers(itemStack1.getAttributeModifiers());
            }
            if (itemStack2 != null) {
                getAttributeMap().applyAttributeModifiers(itemStack2.getAttributeModifiers());
            }
            name = "[HxC]" + (itemStack2 != null ? " using " + itemStack2.getDisplayName() : "");
        }
        prevItemStack = itemStack2 == null ? null : itemStack2.copy();
        theItemInWorldManager.updateBlockRemoving();
        if (itemInUse != null) {
            tickItemInUse(itemStack1);
        }
    }

    public void tickItemInUse(ItemStack updateItem) {
        if (updateItem != null && prevItemStack == itemInUse) {
            itemInUseCount = ForgeEventFactory.onItemUseTick(this, itemInUse, itemInUseCount);
            if (itemInUseCount <= 0) {
                onItemUseFinish();
            } else {
                itemInUse.getItem().onUsingTick(itemInUse, this, itemInUseCount);
                if (itemInUseCount <= 25 && itemInUseCount % 4 == 0) {
                    updateItemUse(updateItem, 5);
                }
                if (--itemInUseCount == 0 && !worldObj.isRemote) {
                    onItemUseFinish();
                }
            }
        }else{
            clearItemInUse();
        }
    }

    @Override
    protected void updateItemUse(ItemStack itemStack, int integer) {
        if(itemStack.getItemUseAction().equals(EnumAction.drink))
            this.playSound("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        
        if(itemStack.getItemUseAction().equals(EnumAction.eat))
            this.playSound("random.eat", 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    public String getDisplayName() {
        return getCommandSenderName();
    }

    @Override
    public void addChatComponentMessage(IChatComponent chatComponent) {
        
    }

    @Override
    public void addChatMessage(IChatComponent chatComponent) {
        
    }
}
