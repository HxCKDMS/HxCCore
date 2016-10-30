package hxckdms.hxccore.asm;

import hxckdms.hxccore.api.event.EmoteEvent;
import hxckdms.hxccore.api.event.LivingSwingEvent;
import hxckdms.hxccore.api.event.PlayerTeleportEvent;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

public class HxCHooks {
    public static ITextComponent onEmoteEvent(ICommandSender sender, ITextComponent component) {
        EmoteEvent event = new EmoteEvent(sender, component);

        if (MinecraftForge.EVENT_BUS.post(event)) return null;
        else if(component == event.getComponent()) return new TextComponentTranslation("chat.type.emote", sender.getDisplayName(), component);
        else return event.getComponent();
    }

    public static boolean onLivingSwingEvent(EntityLivingBase entityLiving, ItemStack itemStack, EnumHand hand) {
        return MinecraftForge.EVENT_BUS.post(new LivingSwingEvent(entityLiving, itemStack, hand));
    }

    public static boolean onPlayerTeleportEvent(EntityPlayer player, double newX, double newY, double newZ) {
        return MinecraftForge.EVENT_BUS.post(new PlayerTeleportEvent(player, newX, newY, newZ, player.dimension));
    }

    public static boolean onPlayerTeleportEvent(EntityPlayer player, double newX, double newY, double newZ, int newDimension) {
        return MinecraftForge.EVENT_BUS.post(new PlayerTeleportEvent(player, newX, newY, newZ, newDimension));
    }

    public static void onSpreadPlayersTeleport(Entity entity, double x, double y, double z) {
        if (entity instanceof EntityPlayer) {
            if (!MinecraftForge.EVENT_BUS.post(new PlayerTeleportEvent((EntityPlayer) entity, x, y, z, entity.dimension))) entity.setPosition(x, y, z);
        } else entity.setPosition(x, y, z);
    }
}
