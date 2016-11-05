package hxckdms.hxccore.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

@Cancelable
public class LivingSwingEvent extends LivingEvent {
    private ItemStack itemStack;

    public LivingSwingEvent(EntityLivingBase entity, ItemStack itemStack) {
        super(entity);
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
