package hxckdms.hxccore.event;

import hxckdms.hxccore.api.event.EmoteEvent;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.network.MessageNameTagSync;
import hxckdms.hxccore.utilities.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static hxckdms.hxccore.libraries.GlobalVariables.permissionData;

public class CommonEvents {
    @SubscribeEvent
    public void worldSaveEvent(WorldEvent.Save event) {
        NBTFileHandler.saveCustomNBTFiles(false);
    }

    @SubscribeEvent
    public void eventLoadFile(PlayerEvent.LoadFromFile event) {
        String uuid = event.getPlayerUUID();
        File modPlayerData = new File(GlobalVariables.modWorldDir, "HxC-" + uuid + ".dat");

        try {
            if (!modPlayerData.exists()) modPlayerData.createNewFile(); } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        HxCPlayerInfoHandler.loadPlayerInfo(UUID.fromString(uuid), new NBTFileHandler(uuid, modPlayerData));
        NBTFileHandler.loadCertainNBTFile(uuid);

        if (!permissionData.hasTag(event.getPlayerUUID()))
            permissionData.setInteger(event.getPlayerUUID(), 1);
    }

    @SubscribeEvent
    public void eventSaveFile(PlayerEvent.SaveToFile event) {
        NBTFileHandler.saveCustomNBTFiles(true);
    }

    @SubscribeEvent
    public void eventLogOut(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        NBTFileHandler.saveCustomNBTFiles(true);
        HxCPlayerInfoHandler.unloadPlayerInfo(event.player.getUniqueID());
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
        if (mutedPlayers.getBoolean(event.getPlayer().getUniqueID().toString())) {
            event.getPlayer().sendMessage(new TextComponentTranslation("chat.error.muted").setStyle(new Style().setColor(TextFormatting.RED)));
            event.setCanceled(true);
        }

        event.setComponent(ColorHelper.handleChat(event.getMessage(), event.getPlayer()));

        if (Configuration.herobrineMessages) {
            if (event.getMessage().toLowerCase().contains("herobrine") || event.getMessage().toLowerCase().contains("my lord")) {
                HxCPlayerInfoHandler.setBoolean(event.getPlayer(), "Herobrine", true);

                event.getComponent().appendText("\n").appendSibling(ColorHelper.handleMessage("<&4Herobrine&f> &4What is your request mortal?", 'f'));
            } else if (HxCPlayerInfoHandler.getBoolean(event.getPlayer(), "Herobrine") && (event.getMessage().toLowerCase().contains("die") || event.getMessage().toLowerCase().contains("kill") || event.getMessage().toLowerCase().contains("misery") || event.getMessage().toLowerCase().contains("suffer") || event.getMessage().toLowerCase().contains("torment"))) {
                event.getPlayer().attackEntityFrom(new DamageSource("command_hxc_kill." + event.getPlayer().world.rand.nextInt(35)) {
                    @Override
                    @Nonnull
                    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                        return ServerTranslationHelper.getTranslation(event.getPlayer(), "death.attack." + damageType, entityLivingBaseIn.getName());
                    }
                }.setDamageAllowedInCreativeMode().setDamageIsAbsolute().setDamageBypassesArmor(), Float.MAX_VALUE);
            } else if (HxCPlayerInfoHandler.getBoolean(event.getPlayer(), "Herobrine")) {
                event.getComponent().appendText("\n").appendSibling(ColorHelper.handleMessage("<&4Herobrine&f> &4Mortals annoy me.", 'f'));
                HxCPlayerInfoHandler.setBoolean(event.getPlayer(), "Herobrine", false);
            }
        }
    }

    @SubscribeEvent
    public void onCommandEmote(EmoteEvent event) {
        if (event.getSender() instanceof EntityPlayerMP) {
            NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
            if (mutedPlayers.getBoolean(((EntityPlayerMP) event.getSender()).getUniqueID().toString())) {
                event.getSender().sendMessage(new TextComponentTranslation("chat.error.muted").setStyle(new Style().setColor(TextFormatting.RED)));
                event.setCanceled(true);
            }
        }

        event.setComponent(new TextComponentTranslation("* ").appendSibling(ColorHelper.handleNick((EntityPlayerMP) event.getSender(), false)).appendText(" ").appendSibling(Configuration.coloredChatMinimumPermissionLevel <= PermissionHandler.getPermissionLevel(event.getSender()) ? ColorHelper.handleMessage(event.getMessage(), 'f') : event.getComponent()));
    }

    private int counter = 0;
    private static MinecraftServer server;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (server == null) server = FMLCommonHandler.instance().getMinecraftServerInstance();

        if (event.phase == TickEvent.Phase.START) {
            counter++;

            if (counter % 200 == 0)
                GlobalVariables.network.sendToAll(new MessageNameTagSync(server.getPlayerList().getPlayers()));

            if (counter % (Configuration.updateDelay * 20) == 0) {
                //long nano = System.nanoTime();

                for (WorldServer worldServer : DimensionManager.getWorlds()) {
                    clearExcessLivingEntities(worldServer.loadedEntityList);
                }


                //nano = System.nanoTime() - nano;
//
                //System.out.println(nano / 1e6D);
            }
        }
    }


    //TODO: remove 'clear' and set min mob limit and up it.
    private void clearExcessLivingEntities(List<Entity> entitiesO) {
        List<EntityLiving> entities = new ArrayList<>(entitiesO)
                .stream()
                .filter(e -> e instanceof EntityLiving)
                .map(e -> (EntityLiving) e)
                .collect(Collectors.toList());

        for (Entity entity : entities) {
            if (Configuration.clearExcessEntities && entities.stream().filter(e -> e.getClass() == entity.getClass()).count() > Configuration.maxEntitiesOfOneType) {
                while (entities.stream().filter(e -> !e.isDead).filter(e -> e.getClass() == entity.getClass()).count() > Configuration.maxEntitiesOfOneType) {
                    entities.stream().filter(e -> !e.isDead).filter(e -> e.getClass() == entity.getClass()).findAny().get().setDead();
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP && event.player.getServer() != null) {
            GlobalVariables.network.sendToAll(new MessageNameTagSync(server.getPlayerList().getPlayers()));
        }
    }

    private static final UUID HEALTH_UUID = UUID.fromString("edff168f-32d7-438b-8d29-189e9405e032");
    private static final UUID DAMAGE_UUID = UUID.fromString("17cb8d52-6376-11e4-b116-123b93f75cba");

    @SubscribeEvent
    public void applyBuffEvent(LivingEvent.LivingUpdateEvent event) {

        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

            boolean enabled = player.world.getGameRules().getBoolean("HxC_XPBuffs");

            IAttributeInstance playerHealthAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
            IAttributeInstance playerDamageAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);
            double healthBuff = Math.min(Configuration.maxBonusHealth, player.experienceLevel / Configuration.XPBuffPerLevels);

            AttributeModifier attributeModifier = playerHealthAttributes.getModifier(HEALTH_UUID);
            if (attributeModifier == null || attributeModifier.getAmount() != healthBuff) {
                if (attributeModifier != null) playerHealthAttributes.removeModifier(attributeModifier);
                if (enabled) playerHealthAttributes.applyModifier(new AttributeModifier(HEALTH_UUID, "HxCHealthBuff", healthBuff, 0));
            }
            double damageBuff = Math.min(Configuration.maxBonusDamage, player.experienceLevel / Configuration.XPBuffPerLevels);

            attributeModifier = playerDamageAttributes.getModifier(DAMAGE_UUID);
            if (attributeModifier == null || attributeModifier.getAmount() != damageBuff) {
                if (attributeModifier != null) playerDamageAttributes.removeModifier(attributeModifier);
                if (enabled) playerDamageAttributes.applyModifier(new AttributeModifier(DAMAGE_UUID, "HxCDamageBuff", damageBuff, 0));
            }

            player.sendPlayerAbilities();
            player.setPlayerHealthUpdated();
        }
    }
}
