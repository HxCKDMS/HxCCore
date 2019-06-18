package hxckdms.hxccore.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import hxckdms.hxccore.configs.Configuration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.List;
import java.util.stream.Stream;

public class EventClearLag {
    private int delay = 0;
    @SubscribeEvent
    public void onLag(TickEvent.ServerTickEvent event) {
        boolean enabled = MinecraftServer.getServer().getEntityWorld().getGameRules().getGameRuleBooleanValue("HxC_ClearLag");
        delay++;
        if (enabled && delay >= Configuration.updateDelay * 20) {
            delay = 0;
            for (WorldServer worldServer : DimensionManager.getWorlds()){
                List<Entity> entities = (List<Entity>) worldServer.loadedEntityList;
                Stream<Entity> ents = entities.stream().filter(e -> e instanceof EntityLiving);
                ents.forEach(e -> {
                    if (entities.stream().filter(a -> a.getCommandSenderName().equalsIgnoreCase(e.getCommandSenderName())).count() > (Math.max(Configuration.maxEntitiesOfOneType, 90))) {
//                        if (Configuration.clearExcessEntities.equalsIgnoreCase("clear"))
//                            entities.stream().filter(a -> a.getCommandSenderName().equalsIgnoreCase(e.getCommandSenderName())).forEach(Entity::setDead);
                        if (Configuration.clearExcessEntities.equalsIgnoreCase("cull") && worldServer.rand.nextBoolean())
                            e.setDead();
                    }
                });
            }
        }
    }
}
