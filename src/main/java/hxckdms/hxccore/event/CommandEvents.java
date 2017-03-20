package hxckdms.hxccore.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import hxckdms.hxccore.api.event.LivingSwingEvent;
import hxckdms.hxccore.api.event.PlayerTeleportEvent;
import hxckdms.hxccore.commands.CommandProtect;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.entity.HxCFakePlayer;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.*;
import java.util.stream.Collectors;

import static hxckdms.hxccore.libraries.GlobalVariables.permissionData;

public class CommandEvents implements EventListener {
    @SubscribeEvent
    public void eventLoadFile(PlayerEvent.LoadFromFile event) {
        if (!permissionData.hasTag(event.entityPlayer.getUniqueID().toString())) permissionData.setInteger(event.entityPlayer.getUniqueID().toString(), 1);
    }

    @SubscribeEvent
    public void eventFly(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;

            if (HxCPlayerInfoHandler.getBoolean(player, "AllowFlying")) {
                player.capabilities.allowFlying = true;
                player.sendPlayerAbilities();
            }
        }
    }

    @SubscribeEvent
    public void eventGod(LivingAttackEvent event) {
        if (!event.source.damageType.contains("command_hxc_kill") && event.entityLiving instanceof EntityPlayerMP && HxCPlayerInfoHandler.getBoolean((EntityPlayer) event.entityLiving, "GodMode")) {
            event.entityLiving.heal(20);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void eventVanish(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
            NBTTagList vanishedList = HxCPlayerInfoHandler.getTagList(player, "VanishedFromList");

            if (vanishedList != null || HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll")) {
                ArrayList<EntityPlayerMP> targets = new ArrayList<>();

                if (HxCPlayerInfoHandler.getBoolean(player, "VanishedFromAll")) targets.addAll(GlobalVariables.server.getConfigurationManager().playerEntityList);
                else if (vanishedList != null)
                    for (int i = 0; i < vanishedList.tagCount(); i++) {
                        UUID uuid = UUID.fromString(vanishedList.getStringTagAt(i));
                        targets.addAll(((List<EntityPlayerMP>) GlobalVariables.server.getConfigurationManager().playerEntityList).parallelStream().filter(playerMP -> playerMP.getUniqueID().equals(uuid)).collect(Collectors.toList()));
                    }

                for (EntityPlayerMP target : targets) {
                    if (target == player) continue;
                    target.playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(player.getEntityId()));
                    target.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(player.getCommandSenderName(), false, 0));
                }
            }
        }
    }

    @SubscribeEvent
    public void eventAFK(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
            boolean isAFK = HxCPlayerInfoHandler.getBoolean(player, "AFK");
            if (isAFK) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void eventPath(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
            if (HxCPlayerInfoHandler.getBoolean(player, "PathEnabled")) {
                Block block = Block.getBlockFromName(HxCPlayerInfoHandler.getString(player, "PathMaterial"));
                assert block != null;

                int metadata = HxCPlayerInfoHandler.getInteger(player, "PathMetaData");
                int pathSize = HxCPlayerInfoHandler.getInteger(player, "PathSize");

                for (int x = -pathSize; x <= pathSize; x++) {
                    for (int z = -pathSize; z <= pathSize; z++) {
                        switch (HxCPlayerInfoHandler.getString(player, "Override")) {
                            case "air":
                                if (player.worldObj.isAirBlock((int) player.posX + x, (int) player.posY - 1, (int) player.posZ + z))
                                    player.worldObj.setBlock((int) player.posX + x, (int) player.posY - 1, (int) player.posZ + z, block, metadata, 3);
                                break;
                            case "fluid":
                                if (player.worldObj.isAirBlock((int) player.posX + x, (int) player.posY - 1, (int) player.posZ + z) || player.worldObj.getBlock((int) player.posX + x, (int) player.posY - 1, (int) player.posZ + z).getMaterial().isLiquid() || FluidRegistry.lookupFluidForBlock(player.worldObj.getBlock((int) player.posX + x, (int) player.posY - 1, (int) player.posZ + z)) != null)
                                    player.worldObj.setBlock((int) player.posX + x, (int) player.posY - 1, (int) player.posZ + z, block, metadata, 3);
                                break;
                            default:
                                player.worldObj.setBlock((int) player.posX + x, (int) player.posY - 1, (int) player.posZ + z, block, metadata, 3);
                                break;
                        }

                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void eventProtection_1(PlayerInteractEvent event) {
        if (Configuration.cancelAllEventsInProtection && !CommandProtect.isPlayerAllowedToEdit(event.entityPlayer, event.x, event.y, event.z, event.entityPlayer.dimension)) {
            event.entityPlayer.addChatMessage(ServerTranslationHelper.getTranslation(event.entityPlayer, "world.protect.noEditAllowed"));
            event.setCanceled(true);
        }
        if (!event.world.isRemote && (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK  || event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)) {
            if (!CommandProtect.isPlayerAllowedToEdit(event.entityPlayer, event.x, event.y, event.z, event.entityPlayer.dimension)) {
                event.entityPlayer.addChatMessage(ServerTranslationHelper.getTranslation(event.entityPlayer, "world.protect.noEditAllowed"));
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void eventPowerTool_1(ItemTooltipEvent event) {
        if (event.itemStack.getTagCompound() != null && event.itemStack.getTagCompound().hasKey("powerToolCommands")) {
            NBTTagList commands = event.itemStack.getTagCompound().getTagList("powerToolCommands", 8);

            for (int i = 0; i < commands.tagCount(); i++) event.toolTip.add(commands.getStringTagAt(i));
        }
    }

    @SuppressWarnings("Duplicates")
    @SubscribeEvent
    public void eventPowerTool_2(LivingSwingEvent event) {
        if (!event.entityLiving.worldObj.isRemote && event.getItemStack() != null && event.getItemStack().getTagCompound() != null && event.getItemStack().getTagCompound().hasKey("powerToolCommands")) {
            NBTTagList commandList = event.getItemStack().getTagCompound().getTagList("powerToolCommands", 8);

            for (int i = 0; i < commandList.tagCount(); i++) {
                String commandString = commandList.getStringTagAt(i);
                String[] splitCommandString = commandString.split("\\s");

                ICommand command = ((Map<String, ICommand>) GlobalVariables.server.getCommandManager().getCommands()).get(splitCommandString[0].substring(1));
                String[] args = new String[splitCommandString.length - 1];
                System.arraycopy(splitCommandString, 1, args, 0, splitCommandString.length - 1);

                boolean hasAt = false;

                /*for (int j = 0; j < args.length; j++) {
                    if (!hasAt) hasAt = args[j].contains("@");
                    for (Entity entity : EntitySelector.matchEntities(event.getEntityLiving(), args[j], Entity.class)) {
                        args[j] = entity.getUniqueID().toString();

                        try {
                            if (event.getEntityLiving() instanceof EntityPlayerMP) {
                                command.execute(GlobalVariables.server, event.getEntityLiving(), args);
                            } else if (Configuration.allowMobsPowerTool) {
                                HxCFakePlayer fakePlayer = new HxCFakePlayer(GlobalVariables.server.worldServerForDimension(event.getEntityLiving().dimension), event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, event.getEntityLiving().rotationYaw, event.getEntityLiving().rotationPitch);
                                fakePlayer.setCustomNameTag(event.getEntityLiving().getName());
                                fakePlayer.setUniqueId(event.getEntityLiving().getUniqueID());
                                command.execute(GlobalVariables.server, fakePlayer, args);
                                fakePlayer.setDead();
                            }
                        } catch (CommandException e) {
                            e.printStackTrace();
                        }
                    }
                }*/

                if (!hasAt) {
                    try {
                        if (event.entityLiving instanceof EntityPlayerMP) {
                            command.processCommand((ICommandSender) event.entityLiving, args);
                        } else if (Configuration.allowMobsPowerTool) {
                            HxCFakePlayer fakePlayer = new HxCFakePlayer(GlobalVariables.server.worldServerForDimension(event.entityLiving.dimension), event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, event.entityLiving.rotationYaw, event.entityLiving.rotationPitch);
                            fakePlayer.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).setBaseValue(event.entityLiving.getMaxHealth());
                            fakePlayer.customName = event.entityLiving.getCommandSenderName();
                            fakePlayer.setUniqueID(event.entityLiving.getUniqueID());
                            command.processCommand(fakePlayer, args);
                            event.entityLiving.setPositionAndUpdate(fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ);
                            event.entityLiving.setHealth(fakePlayer.getHealth());
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
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
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
        backCompound.setDouble("x", event.entityPlayer.posX);
        backCompound.setDouble("y", event.entityPlayer.posY);
        backCompound.setDouble("z", event.entityPlayer.posZ);
        backCompound.setInteger("dimension", event.entityPlayer.dimension);

        HxCPlayerInfoHandler.setTagCompound(event.entityPlayer, "backLocation", backCompound);
    }

    public static final HashMap<EntityPlayerMP, TPARequest> TPAList = new HashMap<>();

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
            if ((--timeRemaining) == 0) target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.TPA.hasExpired", requester.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            else if (timeRemaining % 20 == 0 && timeRemaining <= 200) target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.TPA.willExpire", requester.getDisplayName(), timeRemaining / 20).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));

            return timeRemaining == 0 ? null : this;
        }
    }
}
