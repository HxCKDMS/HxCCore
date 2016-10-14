package hxckdms.hxccore.event;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.EventListener;

import static hxckdms.hxccore.libraries.Constants.PACKET_CHANNEL_NAME;
import static hxckdms.hxccore.libraries.GlobalVariables.doesPlayerHaveMod;

public class EventNetworkCheck implements EventListener {
    @SubscribeEvent
    public void networkChannelRegistrationEvent(FMLNetworkEvent.CustomPacketRegistrationEvent event) {
        if (event.getHandler() instanceof NetHandlerPlayServer && event.getOperation().equals("REGISTER") && event.getRegistrations().contains(PACKET_CHANNEL_NAME))
            doesPlayerHaveMod.add(((NetHandlerPlayServer) event.getHandler()).playerEntity.getUniqueID());
    }
}
