package HxCKDMS.HxCCore.Proxy;

import HxCKDMS.HxCCore.Contributors.CapesThread;
import HxCKDMS.HxCCore.lib.References;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

import static HxCKDMS.HxCCore.Contributors.CapesThread.capes;

public class ClientProxy implements IProxy {
    Minecraft mc;
    private final Thread capeThread = new Thread(new CapesThread());
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        mc = Minecraft.getMinecraft();

        capeThread.setName("HxCKDMS Capes Download thread");
        capeThread.start();

        List objs = new ArrayList<>(mc.getResourcePackRepository().getRepositoryEntries());

        mc.getResourcePackRepository().getRepositoryEntriesAll().forEach(ent -> {
            if (!objs.contains(ent) && ((ResourcePackRepository.Entry)ent).getResourcePack().getPackName().equals("HxCCapes Pack") || ((ResourcePackRepository.Entry)ent).getResourcePack().getPackName().equals("hxccapes")) {
                objs.add(ent);
            }
        });
        mc.getResourcePackRepository().func_148527_a(objs);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new CapeRenderer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    public class CapeRenderer {
        @SubscribeEvent
        public void renderPost(RenderPlayerEvent.Post event){
            if(capes.containsKey(event.entityPlayer.getCommandSenderName())) {
                ((AbstractClientPlayer) event.entityPlayer).func_152121_a(MinecraftProfileTexture.Type.CAPE, new ResourceLocation(References.MOD_ID.toLowerCase(), "textures/capes/" + event.entityPlayer.getCommandSenderName().toLowerCase() + ".png"));
            }
        }
    }

}
