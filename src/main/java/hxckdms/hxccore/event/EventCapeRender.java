package hxckdms.hxccore.event;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hxckdms.hxccore.libraries.GlobalVariables;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.EventListener;
import java.util.List;

@SideOnly(Side.CLIENT)
public class EventCapeRender implements EventListener {
    @SubscribeEvent
    public void addCapeToTextures(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayerSP) {
            EntityPlayerSP player = (EntityPlayerSP) event.entityLiving;
            ((List<AbstractClientPlayer>) player.worldObj.playerEntities).stream().filter(iPlayer -> GlobalVariables.playerCapes.containsKey(iPlayer.getUniqueID().toString()))
                    .forEach(iPlayer -> iPlayer.func_152121_a(MinecraftProfileTexture.Type.CAPE, new ResourceLocation("hxccapes", iPlayer.getUniqueID().toString())));
        }
    }
}
