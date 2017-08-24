package hxckdms.hxccore.event;

import hxckdms.hxccore.configs.Configuration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.stream.Stream;

public class EventClearLag {
    private int delay = 0;
    @SubscribeEvent
    public void onLag(TickEvent.ServerTickEvent event) {
        delay++;
        if (delay >= Configuration.updateDelay * 20) {
            delay = 0;
            for (WorldServer worldServer : DimensionManager.getWorlds()){
                List<Entity> entities = worldServer.loadedEntityList;
                Stream<Entity> ents = entities.stream().filter(e -> e instanceof EntityLiving);
                ents.forEach(e -> {
                    if (entities.stream().filter(a -> a.getName().equalsIgnoreCase(e.getName())).count() > Configuration.maxEntitiesOfOneType) {
                        if (Configuration.clearExcessEntities.equalsIgnoreCase("clear"))
                            entities.stream().filter(a -> a.getName().equalsIgnoreCase(e.getName())).forEach(Entity::setDead);
                        else if (Configuration.clearExcessEntities.equalsIgnoreCase("cull") && worldServer.rand.nextBoolean())
                            e.setDead();
                    }
                });
            }
        }
    }
}