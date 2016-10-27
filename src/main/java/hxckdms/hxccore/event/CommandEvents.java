package hxckdms.hxccore.event;

import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.EventListener;
import java.util.HashMap;
import java.util.UUID;

import static hxckdms.hxccore.libraries.GlobalVariables.permissionData;
import static net.minecraft.entity.Entity.FLAGS;

public class CommandEvents implements EventListener {
    @SubscribeEvent
    public void eventLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!permissionData.hasTag(event.player.getUniqueID().toString())) permissionData.setInteger(event.player.getUniqueID().toString(), 1);
    }

    @SubscribeEvent
    public void eventFly(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

            if (HxCPlayerInfoHandler.getBoolean(player, "AllowFlying")) {
                player.capabilities.allowFlying = true;
                player.sendPlayerAbilities();
            }
        }
    }

    @SubscribeEvent
    public void eventGod(LivingAttackEvent event) {
        if (!event.getSource().damageType.contains("command_hxc_kill") && event.getEntityLiving() instanceof EntityPlayerMP && HxCPlayerInfoHandler.getBoolean((EntityPlayer) event.getEntityLiving(), "GodMode")) {
            event.getEntityLiving().heal(20);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void eventVanish(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
            NBTTagList vanishedList = HxCPlayerInfoHandler.getTagList(player, "VanishedFromList");

            if (vanishedList != null) {
                for (int i = 0; i < vanishedList.tagCount(); i++) {
                    UUID uuid = UUID.fromString(vanishedList.getStringTagAt(i));

                    if (GlobalVariables.server.getPlayerList().getPlayerList().parallelStream().noneMatch(playerMP -> playerMP.getUniqueID().equals(uuid))) continue;
                    EntityPlayerMP target = GlobalVariables.server.getPlayerList().getPlayerByUUID(uuid);

                    EntityDataManager dataManager = new EntityDataManager(player);
                    dataManager.register(FLAGS, (byte) 0);
                    dataManager.set(FLAGS, (byte) (1 << 5));

                    target.connection.sendPacket(new SPacketEntityMetadata(player.getEntityId(), dataManager, true));
                }
            } else
                if (HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll") && !player.isInvisible()) player.setInvisible(true);
        }
    }

    public static HashMap<EntityPlayerMP, TPARequest> TPAList = new HashMap<>();

    @SubscribeEvent
    public void eventTPA(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (EntityPlayerMP key : TPAList.keySet())
                if (TPAList.compute(key, (iKey, value) -> value.handleCompute(iKey)) == null) break;
        }
    }

    public static class TPARequest {
        EntityPlayerMP requester;
        int timeRemaining;

        public TPARequest(EntityPlayerMP requester, int timeRemaining) {
            this.requester = requester;
            this.timeRemaining = timeRemaining;
        }

        public EntityPlayerMP getRequester() {
            return requester;
        }

        private TPARequest handleCompute(EntityPlayerMP target) {
            if ((--timeRemaining) == 0) target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.TPA.hasExpired", requester.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED)));
            else if (timeRemaining % 20 == 0 && timeRemaining <= 200) target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.TPA.willExpire", requester.getDisplayName(), timeRemaining / 20).setStyle(new Style().setColor(TextFormatting.YELLOW)));

            return timeRemaining == 0 ? null : this;
        }
    }
}
