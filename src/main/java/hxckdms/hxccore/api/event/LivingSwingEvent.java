package hxckdms.hxccore.api.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class LivingSwingEvent extends LivingEvent {
    private ItemStack itemStack;
    private final EnumHand hand;

    public LivingSwingEvent(EntityLivingBase entity, ItemStack itemStack, EnumHand hand) {
        super(entity);
        this.itemStack = itemStack;
        this.hand = hand;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public EnumHand getHand() {
        return hand;
    }
}
