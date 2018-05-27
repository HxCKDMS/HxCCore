package hxckdms.hxccore.api.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class LivingSwingEvent extends LivingEvent {
    private final EnumHand hand;

    public LivingSwingEvent(EntityLivingBase entity, EnumHand hand) {
        super(entity);
        this.hand = hand;
    }

    public EnumHand getHand() {
        return hand;
    }

    public ItemStack getItemStack() {
        return getEntityLiving().getHeldItem(getHand());
    }
}
