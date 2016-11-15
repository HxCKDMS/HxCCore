package hxckdms.hxccore.entity;

import com.mojang.authlib.GameProfile;
import hxckdms.hxccore.network.FakeNetHandlerPlayerServer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

@SuppressWarnings({"unused", "WeakerAccess"})
public class HxCFakePlayer extends FakePlayer {
    public boolean sneaking = false;
    public ItemStack prevItemStack = null;

    private static GameProfile gameProfile = new GameProfile(UUID.fromString("a4a626f2-aca9-11e4-89d3-123b93f75cba"), "[HxC]");

    public HxCFakePlayer(WorldServer world) {
        super(world, gameProfile);
        connection = new FakeNetHandlerPlayerServer(FMLCommonHandler.instance().getMinecraftServerInstance(), this);
        this.addedToChunk = false;
    }

    public HxCFakePlayer(WorldServer world, double x, double y, double z, float yaw, float pitch) {
        this(world);
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
    }

    public static boolean isBlockBreakable(HxCFakePlayer fakePlayer, World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        if (fakePlayer == null) {
            return blockState.getBlockHardness(world, pos) >= 0;
        } else {
            return blockState.getPlayerRelativeBlockHardness(fakePlayer, world, pos) >= 0;
        }
    }

    public void setItemInHand(ItemStack itemInHand, int slot) {
        this.inventory.setInventorySlotContents(slot, itemInHand);
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
    public boolean isSneaking() {
        return sneaking;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return true;
    }

    @Override
    public void onUpdate() {
        ItemStack itemStack1 = prevItemStack;
        ItemStack itemStack2 = getHeldItem(EnumHand.MAIN_HAND);
        if (!ItemStack.areItemStacksEqual(itemStack2, itemStack1)) {
            if (itemStack1 != null) {
                getAttributeMap().removeAttributeModifiers(itemStack1.getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
            }
            if (itemStack2 != null) {
                getAttributeMap().applyAttributeModifiers(itemStack2.getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
            }
            String name = "[HxC]" + (itemStack2 != null ? " using " + itemStack2.getDisplayName() : "");
        }
        prevItemStack = itemStack2 == null ? null : itemStack2.copy();
        interactionManager.updateBlockRemoving();
        if (itemStackMainHand != null) {
            tickItemInUse(itemStack1);
        }
    }

    public void tickItemInUse(ItemStack updateItem) {
        if (updateItem != null && prevItemStack == itemStackMainHand) {
            itemStackMainHand.stackSize = ForgeEventFactory.onItemUseTick(this, itemStackMainHand, itemStackMainHand.stackSize);
            if (itemStackMainHand.stackSize <= 0) {
                onItemUseFinish();
            } else {
                itemStackMainHand.getItem().onUsingTick(itemStackMainHand, this, itemStackMainHand.stackSize);
                if (itemStackMainHand.stackSize <= 25 && itemStackMainHand.stackSize % 4 == 0) {
                    updateItemUse(updateItem, 5);
                }
                if (--itemStackMainHand.stackSize == 0 && !world.isRemote) {
                    onItemUseFinish();
                }
            }
        } else {
            itemStackMainHand = null;
        }
    }

    @Override
    protected void updateItemUse(ItemStack itemStack, int integer) {
        if (itemStack.getItemUseAction().equals(EnumAction.DRINK))
            this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);

        if (itemStack.getItemUseAction().equals(EnumAction.EAT))
            this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    public String getDisplayNameString() {
        return getName();
    }

    @Override
    public void addChatComponentMessage(ITextComponent textComponent) {}

    @Override
    public void addChatMessage(ITextComponent textComponent) {}
}
