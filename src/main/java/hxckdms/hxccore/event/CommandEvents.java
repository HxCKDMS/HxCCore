package hxckdms.hxccore.event;

import hxckdms.hxccore.api.event.LivingSwingEvent;
import hxckdms.hxccore.api.event.PlayerTeleportEvent;
import hxckdms.hxccore.commands.CommandProtect;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.entity.HxCFakePlayer;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CommandEvents implements EventListener {

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

            if (vanishedList != null || HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll")) {
                ArrayList<EntityPlayerMP> targets = new ArrayList<>();

                if (HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll")) targets.addAll(((EntityPlayerMP) event.getEntityLiving()).mcServer.getPlayerList().getPlayers());
                else if (vanishedList != null)
                    for (int i = 0; i < vanishedList.tagCount(); i++) {
                        UUID uuid = UUID.fromString(vanishedList.getStringTagAt(i));
                        targets.addAll(((EntityPlayerMP) event.getEntityLiving()).mcServer.getPlayerList().getPlayers().parallelStream().filter(playerMP -> playerMP.getUniqueID().equals(uuid)).collect(Collectors.toList()));
                    }

                for (EntityPlayerMP target : targets) {
                    if (target == player) continue;
                    target.connection.sendPacket(new SPacketDestroyEntities(player.getEntityId()));
                    target.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, player));
                }
            }
        }
    }

    @SubscribeEvent
    public void eventAFK(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
            boolean isAFK = HxCPlayerInfoHandler.getBoolean(player, "AFK");
            if (isAFK) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void eventPath(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
            if (HxCPlayerInfoHandler.getBoolean(player, "PathEnabled")) {
                Block block = Block.getBlockFromName(HxCPlayerInfoHandler.getString(player, "PathMaterial"));

                if (block == null)
                    return;

                int metadata = HxCPlayerInfoHandler.getInteger(player, "PathMetaData");
                int pathSize = HxCPlayerInfoHandler.getInteger(player, "PathSize");
                Predicate<BlockPos> blockPredicate;

                switch (HxCPlayerInfoHandler.getString(player, "Override")) {
                    case "air":
                        blockPredicate = pos -> player.world.isAirBlock(pos);
                        break;
                    case "fluid":
                        blockPredicate = pos -> player.world.isAirBlock(pos) || player.world.getBlockState(pos).getMaterial().isLiquid() || FluidRegistry.lookupFluidForBlock(player.world.getBlockState(pos).getBlock()) != null;
                        break;
                    default:
                        blockPredicate = pos -> true;
                        break;
                }

                StreamSupport.stream(BlockPos.getAllInBox(new BlockPos(player.posX - pathSize, player.posY - 1, player.posZ - pathSize), new BlockPos(player.posX + pathSize, player.posY - 1, player.posZ + pathSize)).spliterator(), false)
                        .filter(blockPredicate)
                        .forEach(pos -> player.world.setBlockState(pos, block.getStateFromMeta(metadata)));
            }
        }
    }

    @SubscribeEvent
    public void eventProtection_1(PlayerInteractEvent event) {
        if (event.getWorld().isRemote) return;

        if (Configuration.cancelAllEventsInProtection && !CommandProtect.isPlayerAllowedToEdit(event.getEntityPlayer(), event.getPos(), event.getEntityPlayer().dimension)) {
            event.getEntityPlayer().sendMessage(ServerTranslationHelper.getTranslation(event.getEntityPlayer(), "world.protect.noEditAllowed"));
            event.setCanceled(true);
        }
        if (event instanceof PlayerInteractEvent.RightClickBlock  || event instanceof PlayerInteractEvent.LeftClickBlock || event instanceof PlayerInteractEvent.EntityInteract) {
            if (!CommandProtect.isPlayerAllowedToEdit(event.getEntityPlayer(), event.getPos(), event.getEntityPlayer().dimension)) {
                event.getEntityPlayer().sendMessage(ServerTranslationHelper.getTranslation(event.getEntityPlayer(), "world.protect.noEditAllowed"));
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void eventPowerTool_1(ItemTooltipEvent event) {
        if (event.getItemStack().getTagCompound() != null && event.getItemStack().getTagCompound().hasKey("powerToolCommands")) {
            NBTTagList commands = event.getItemStack().getTagCompound().getTagList("powerToolCommands", 8);

            for (int i = 0; i < commands.tagCount(); i++) event.getToolTip().add(commands.getStringTagAt(i));
        }
    }


    @SuppressWarnings("Duplicates")
    @SubscribeEvent
    public void eventPowerTool_2(LivingSwingEvent event) {
        if (!event.getEntityLiving().world.isRemote && event.getItemStack() != null && event.getItemStack().getTagCompound() != null && event.getItemStack().getTagCompound().hasKey("powerToolCommands")) {
            NBTTagList commandList = event.getItemStack().getTagCompound().getTagList("powerToolCommands", 8);

            for (int i = 0; i < commandList.tagCount(); i++) {
                String commandString = commandList.getStringTagAt(i);
                String[] splitCommandString = commandString.split("\\s");

                ICommand command = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().getCommands().get(splitCommandString[0].substring(1));
                String[] args = new String[splitCommandString.length - 1];
                System.arraycopy(splitCommandString, 1, args, 0, splitCommandString.length - 1);

                boolean hasAt = false;

                for (int j = 0; j < args.length; j++) {
                    if (!hasAt) hasAt = args[j].contains("@");
                    try {
                        for (Entity entity : EntitySelector.matchEntities(event.getEntityLiving(), args[j], Entity.class)) {
                            args[j] = entity.getCachedUniqueIdString();

                            try {
                                if (event.getEntityLiving() instanceof EntityPlayerMP) {
                                    command.execute(((EntityPlayerMP) event.getEntityLiving()).mcServer, event.getEntityLiving(), args);
                                } else if (Configuration.allowMobsPowerTool) {
                                    HxCFakePlayer fakePlayer = new HxCFakePlayer(DimensionManager.getWorld(event.getEntityLiving().dimension), event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, event.getEntityLiving().rotationYaw, event.getEntityLiving().rotationPitch);
                                    fakePlayer.setCustomNameTag(event.getEntityLiving().getName());
                                    fakePlayer.setUniqueId(event.getEntityLiving().getUniqueID());
                                    command.execute(fakePlayer.mcServer, fakePlayer, args);
                                    fakePlayer.setDead();
                                }
                            } catch (CommandException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (CommandException e) {
                        e.printStackTrace();
                    }
                }

                if (!hasAt) {
                    try {
                        if (event.getEntityLiving() instanceof EntityPlayerMP) {
                            command.execute(((EntityPlayerMP) event.getEntityLiving()).mcServer, event.getEntityLiving(), args);
                        } else if (Configuration.allowMobsPowerTool) {
                            HxCFakePlayer fakePlayer = new HxCFakePlayer(DimensionManager.getWorld(event.getEntityLiving().dimension), event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, event.getEntityLiving().rotationYaw, event.getEntityLiving().rotationPitch);
                            fakePlayer.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(event.getEntityLiving().getMaxHealth());
                            fakePlayer.setCustomNameTag(event.getEntityLiving().getName());
                            fakePlayer.setUniqueId(event.getEntityLiving().getUniqueID());
                            command.execute(fakePlayer.mcServer, fakePlayer, args);
                            event.getEntityLiving().setPositionAndUpdate(fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ);
                            event.getEntityLiving().setHealth(fakePlayer.getHealth());
                            fakePlayer.setDead();
                        }
                    } catch (CommandException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void eventBack_1(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            NBTTagCompound backCompound = new NBTTagCompound();
            backCompound.setDouble("x", player.posX);
            backCompound.setDouble("y", player.posY);
            backCompound.setDouble("z", player.posZ);
            backCompound.setInteger("dimension", player.dimension);

            HxCPlayerInfoHandler.setTagCompound(player, "backLocation", backCompound);
        }
    }

    @SubscribeEvent
    public void eventBack_2(PlayerTeleportEvent event) {
        NBTTagCompound backCompound = new NBTTagCompound();
        backCompound.setDouble("x", event.getEntityPlayer().posX);
        backCompound.setDouble("y", event.getEntityPlayer().posY);
        backCompound.setDouble("z", event.getEntityPlayer().posZ);
        backCompound.setInteger("dimension", event.getEntityPlayer().dimension);

        HxCPlayerInfoHandler.setTagCompound(event.getEntityPlayer(), "backLocation", backCompound);
    }

    public static final HashMap<EntityPlayerMP, TPARequest> TPAList = new HashMap<>();

    @SubscribeEvent
    public void eventTPA(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (EntityPlayerMP key : TPAList.keySet())
                if (TPAList.compute(key, (iKey, value) -> value != null ? value.handleCompute(iKey) : null) == null) break;
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

        @Nullable
        private TPARequest handleCompute(EntityPlayerMP target) {
            if ((--timeRemaining) == 0) target.sendMessage(ServerTranslationHelper.getTranslation(target, "commands.TPA.hasExpired", requester.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED)));
            else if (timeRemaining % 20 == 0 && timeRemaining <= 200) target.sendMessage(ServerTranslationHelper.getTranslation(target, "commands.TPA.willExpire", requester.getDisplayName(), timeRemaining / 20).setStyle(new Style().setColor(TextFormatting.YELLOW)));

            return timeRemaining == 0 ? null : this;
        }
    }
}
