package hxckdms.hxccore.event;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import hxckdms.hxccore.libraries.GlobalVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    public void addCapeToTextures(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.ClientTickEvent.Phase.START && Minecraft.getMinecraft().world != null) {
             Minecraft.getMinecraft().world.playerEntities.stream().filter(iPlayer -> GlobalVariables.playerCapes.containsKey(iPlayer.getUniqueID().toString()))
                    .filter(iPlayer -> ((AbstractClientPlayer) iPlayer).getPlayerInfo() != null)
                    .forEach(iPlayer -> ((AbstractClientPlayer) iPlayer).getPlayerInfo().playerTextures.put(MinecraftProfileTexture.Type.CAPE, new ResourceLocation("hxccapes", iPlayer.getUniqueID().toString())));

       }
    }
}
