package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Entity.HxCFakePlayer;
import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public class EventPowerTool {
    byte cooldown = 0;
    @SubscribeEvent
    public void event(LivingEvent.LivingUpdateEvent event) {
        if (cooldown != 0) cooldown--;
        if (event.entityLiving.getHeldItem() != null && event.entityLiving.isSwingInProgress && event.entityLiving.swingProgressInt == 1) {
            ItemStack stack = event.entityLiving.getHeldItem();
            if (stack.hasTagCompound() && !stack.getTagCompound().getString("powertool").isEmpty() && cooldown <= 0) {
                if (event.entityLiving instanceof EntityPlayerMP)
                    HxCCore.server.getCommandManager().executeCommand((EntityPlayerMP)event.entityLiving, stack.getTagCompound().getString("powertool"));
                else if (Configurations.allowMobsPowertool)
                    HxCCore.server.getCommandManager().executeCommand(new HxCFakePlayer(HxCCore.server.worldServerForDimension(event.entityLiving.dimension), event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, event.entityLiving.rotationYawHead, event.entityLiving.rotationPitch), stack.getTagCompound().getString("powertool"));
                cooldown = 2;
            }
        }
    }
}
