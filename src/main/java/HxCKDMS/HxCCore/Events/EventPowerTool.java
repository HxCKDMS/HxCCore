package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public class EventPowerTool {
    byte cooldown = 0;
    @SubscribeEvent
    public void event(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP && cooldown != 0) cooldown--;
        if (event.entityLiving instanceof EntityPlayerMP && event.entityLiving.getHeldItem() != null && event.entityLiving.isSwingInProgress && event.entityLiving.swingProgress < 0.1) {
            EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
            ItemStack stack = player.getHeldItem();
            if (stack.hasTagCompound() && !stack.getTagCompound().getString("powertool").isEmpty() && cooldown <= 0) {
                HxCCore.server.getCommandManager().executeCommand(player, stack.getTagCompound().getString("powertool"));
                cooldown = 2;
            }
        }
    }
}
