package hxckdms.hxccore.event;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import hxckdms.hxccore.libraries.GlobalVariables;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EventListener;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class EventCapeRender implements EventListener {
    @SubscribeEvent
    public void addCapeToTextures(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerSP) {
            EntityPlayerSP player = (EntityPlayerSP) event.getEntityLiving();
            player.worldObj.playerEntities.stream().map(iPlayer -> iPlayer.getUniqueID().toString()).filter(GlobalVariables.playerCapes::containsKey)
                    .forEach(uuid -> player.connection.getPlayerInfo(UUID.fromString(uuid)).playerTextures.put(MinecraftProfileTexture.Type.CAPE, new ResourceLocation("hxccapes", uuid)));
        }
    }
}
