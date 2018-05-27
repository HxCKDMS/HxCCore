package hxckdms.hxccore.event;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import hxckdms.hxccore.libraries.GlobalVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public void onDrawDebugText(RenderGameOverlayEvent.Text event) {
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo && Minecraft.getMinecraft().getConnection() != null) {
            event.getRight().add("");
            event.getRight().add("Ping: " + Minecraft.getMinecraft().getConnection().getPlayerInfo(Minecraft.getMinecraft().player.getUniqueID()).getResponseTime());
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public void addCapeToTextures(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerSP) {

            EntityPlayerSP player = (EntityPlayerSP) event.getEntityLiving();
            player.world.playerEntities.stream().filter(iPlayer -> GlobalVariables.playerCapes.containsKey(iPlayer.getUniqueID().toString()))
                    .filter(iPlayer -> ((AbstractClientPlayer) iPlayer).getPlayerInfo() != null)
                    .forEach(iPlayer -> ((AbstractClientPlayer) iPlayer).getPlayerInfo().playerTextures.put(MinecraftProfileTexture.Type.CAPE, new ResourceLocation("hxccapes", iPlayer.getUniqueID().toString())));

        }
    }
}
