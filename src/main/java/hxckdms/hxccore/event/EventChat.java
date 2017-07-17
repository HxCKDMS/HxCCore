package hxckdms.hxccore.event;

import hxckdms.hxccore.api.event.EmoteEvent;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.CommandRegistry;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.PermissionHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Arrays;
import java.util.EventListener;

public class EventChat implements EventListener {
    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
        if (mutedPlayers.getBoolean(event.getPlayer().getUniqueID().toString())) {
            event.getPlayer().addChatMessage(ServerTranslationHelper.getTranslation(event.getPlayer(), "chat.error.muted").setStyle(new Style().setColor(TextFormatting.RED)));
            event.setCanceled(true);
        }

        event.setComponent(ColorHelper.handleChat(event.getMessage(), event.getPlayer()));

        if (Configuration.herobrineMessages) {
            if (event.getMessage().toLowerCase().contains("herobrine") || event.getMessage().toLowerCase().contains("my lord")) {
                HxCPlayerInfoHandler.setBoolean(event.getPlayer(), "Herobrine", true);
                GlobalVariables.server.getPlayerList().sendChatMsg(ColorHelper.handleMessage("<&4Herobrine&f> &4What is your request mortal?", 'f'));
            } else if (HxCPlayerInfoHandler.getBoolean(event.getPlayer(), "Herobrine") && (event.getMessage().toLowerCase().contains("die") || event.getMessage().toLowerCase().contains("kill") || event.getMessage().toLowerCase().contains("misery") || event.getMessage().toLowerCase().contains("suffer") || event.getMessage().toLowerCase().contains("torment"))) {
                event.getPlayer().attackEntityFrom(new DamageSource("command_hxc_kill." + event.getPlayer().worldObj.rand.nextInt(35)) {
                    @Override
                    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                        return ServerTranslationHelper.getTranslation(event.getPlayer(), "death.attack." + damageType, entityLivingBaseIn.getDisplayName());
                    }
                }.setDamageAllowedInCreativeMode().setDamageIsAbsolute().setDamageBypassesArmor(), Float.MAX_VALUE);
            } else if (HxCPlayerInfoHandler.getBoolean(event.getPlayer(), "Herobrine")) {
                GlobalVariables.server.getPlayerList().sendChatMsg(ColorHelper.handleMessage("<&4Herobrine&f> &4Mortals annoy me.", 'f'));
                HxCPlayerInfoHandler.setBoolean(event.getPlayer(), "Herobrine", false);
            }
        }
    }

    @SubscribeEvent
    public void onEmoteEvent(EmoteEvent event) {
        if (event.getSender() instanceof EntityPlayerMP) {
            NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
            if (mutedPlayers.getBoolean(((EntityPlayerMP) event.getSender()).getUniqueID().toString())) {
                event.getSender().addChatMessage(ServerTranslationHelper.getTranslation(event.getSender(), "chat.error.muted").setStyle(new Style().setColor(TextFormatting.RED)));
                event.setCanceled(true);
            }
        }

        event.setComponent(new TextComponentTranslation("* ").appendSibling(ColorHelper.handleNick((EntityPlayerMP) event.getSender(), false)).appendText(" ").appendSibling(Configuration.coloredChatMinimumPermissionLevel <= PermissionHandler.getPermissionLevel(event.getSender()) ? ColorHelper.handleMessage(event.getMessage(), 'f') : event.getComponent()));
    }


    @SubscribeEvent
    public void commandEvent(CommandEvent event) {
        if (event.getSender().getName() != null && event.getSender() instanceof EntityPlayerMP) {
            String cmd = event.getCommand().getCommandName() + " " + Arrays.asList(event.getParameters()).toString().replace(",", "").substring(1, Arrays.asList(event.getParameters()).toString().replace(",", "").length() - 1);
            final Item[] price = new Item[1];
            final int[] times = new int[1];
            final int[] meta = new int[1];


            CommandRegistry.CommandConfig.bannedCommands.keySet().forEach(c -> {
                if (CommandRegistry.CommandConfig.bannedCommands.get(c) == 0 && c.equalsIgnoreCase(cmd)) {
                    event.setCanceled(true);
//                    throw new WrongUsageException(ServerTranslationHelper.getTranslation(((EntityPlayerMP) event.getSender()).getUniqueID(), "commands.exception.bannedCommand").getFormattedText());
                } else if (CommandRegistry.CommandConfig.bannedCommands.get(c) == 1 && cmd.startsWith(c)) {
                    event.setCanceled(true);
//                    throw new WrongUsageException(ServerTranslationHelper.getTranslation(((EntityPlayerMP) event.getSender()).getUniqueID(), "commands.exception.bannedCommand").getFormattedText());
                } else if (CommandRegistry.CommandConfig.bannedCommands.get(c) == 2 && cmd.contains(c)) {
                    event.setCanceled(true);
//                    throw new WrongUsageException(ServerTranslationHelper.getTranslation(((EntityPlayerMP) event.getSender()).getUniqueID(), "commands.exception.bannedCommand").getFormattedText());
                }
            });

            if (PermissionHandler.canUseCommand(event.getSender(), event.getCommand())) {
                CommandRegistry.CommandConfig.commandCosts.forEach((command, limiter) -> {
                    if (cmd.startsWith(command)) {
                        if (GameRegistry.findItem(limiter.split(":")[0], limiter.split(":")[1]) != null) {
                            price[0] = GameRegistry.findItem(limiter.split(":")[0], limiter.split(":")[1]);
                            meta[0] = Integer.parseInt(limiter.split(":")[2]);
                            times[0] = Integer.parseInt(limiter.split(":")[3]);
                            if (!((EntityPlayerMP) event.getSender()).inventory.hasItemStack(new ItemStack(GameRegistry.findItem(limiter.split(":")[0], limiter.split(":")[1])))) {
                                ((EntityPlayerMP) event.getSender()).addChatComponentMessage(new TextComponentTranslation("\u00a74Requires " + price[0].getItemStackDisplayName(new ItemStack(price[0]))));
                                event.setCanceled(true);
                            }
                        }
                    }
                });
            }

            if (price[0] != null) {
                if (cmd.startsWith("HxC ")) {
                    if (CommandRegistry.getCommandForName(event.getParameters()[1]).checkPermission(GlobalVariables.server, event.getSender()))
                            ((EntityPlayerMP) event.getSender()).inventory.clearMatchingItems(price[0], meta[0], times[0], null);
                } else {
                    ((EntityPlayerMP) event.getSender()).inventory.clearMatchingItems(price[0], meta[0], times[0], null);
                }
            }
        }
    }
}
