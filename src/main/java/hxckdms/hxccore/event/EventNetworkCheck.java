package hxckdms.hxccore.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.network.NetHandlerPlayServer;

import java.util.EventListener;

import static hxckdms.hxccore.libraries.Constants.PACKET_CHANNEL_NAME;
import static hxckdms.hxccore.libraries.GlobalVariables.doesPlayerHaveMod;

public class EventNetworkCheck implements EventListener {
    @SubscribeEvent
    public void networkChannelRegistrationEvent(FMLNetworkEvent.CustomPacketRegistrationEvent event) {
        if (event.handler instanceof NetHandlerPlayServer && event.operation.equals("REGISTER") && event.registrations.contains(PACKET_CHANNEL_NAME))
            doesPlayerHaveMod.add(((NetHandlerPlayServer) event.handler).playerEntity.getUniqueID());
    }
}
