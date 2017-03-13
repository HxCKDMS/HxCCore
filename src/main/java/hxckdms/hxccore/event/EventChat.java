package hxckdms.hxccore.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import hxckdms.hxccore.HxCCore;
import hxckdms.hxccore.api.event.EmoteEvent;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.CommandRegistry;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.PermissionHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;

import java.util.Arrays;
import java.util.EventListener;

public class EventChat implements EventListener {
    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
        if (mutedPlayers.getBoolean(event.player.getUniqueID().toString())) {
            event.player.addChatMessage(ServerTranslationHelper.getTranslation(event.player, "chat.error.muted").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            event.setCanceled(true);
        }

        event.component = ColorHelper.handleChat(event.message, event.player);

        if (Configuration.herobrineMessages) {
            if (event.message.toLowerCase().contains("herobrine") || event.message.toLowerCase().contains("my lord")) {
                HxCPlayerInfoHandler.setBoolean(event.player, "Herobrine", true);
                GlobalVariables.server.getConfigurationManager().sendChatMsg(ColorHelper.handleMessage("<&4Herobrine&f> &4What is your request mortal?", 'f'));
            } else if (HxCPlayerInfoHandler.getBoolean(event.player, "Herobrine") && (event.message.toLowerCase().contains("die") || event.message.toLowerCase().contains("kill") || event.message.toLowerCase().contains("misery") || event.message.toLowerCase().contains("suffer") || event.message.toLowerCase().contains("torment"))) {
                event.player.attackEntityFrom(new DamageSource("command_hxc_kill." + event.player.worldObj.rand.nextInt(35)) {
                    @Override
                    public IChatComponent func_151519_b(EntityLivingBase entityLivingBaseIn) {
                        return ServerTranslationHelper.getTranslation(event.player, "death.attack." + damageType, entityLivingBaseIn.getCommandSenderName());
                    }
                }.setDamageAllowedInCreativeMode().setDamageIsAbsolute().setDamageBypassesArmor(), Float.MAX_VALUE);
            } else if (HxCPlayerInfoHandler.getBoolean(event.player, "Herobrine")) {
                GlobalVariables.server.getConfigurationManager().sendChatMsg(ColorHelper.handleMessage("<&4Herobrine&f> &4Mortals annoy me.", 'f'));
                HxCPlayerInfoHandler.setBoolean(event.player, "Herobrine", false);
            }
        }
    }

    @SubscribeEvent
    public void onEmoteEvent(EmoteEvent event) {
        if (event.getSender() instanceof EntityPlayerMP) {
            NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
            if (mutedPlayers.getBoolean(((EntityPlayerMP) event.getSender()).getUniqueID().toString())) {
                event.getSender().addChatMessage(ServerTranslationHelper.getTranslation(event.getSender(), "chat.error.muted").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                event.setCanceled(true);
            }
        }

        event.setComponent(new ChatComponentTranslation("* ").appendSibling(ColorHelper.handleNick((EntityPlayerMP) event.getSender(), false)).appendText(" ").appendSibling(Configuration.coloredChatMinimumPermissionLevel <= PermissionHandler.getPermissionLevel(event.getSender()) ? ColorHelper.handleMessage(event.getMessage(), 'f') : event.getComponent()));
    }

    @SubscribeEvent
    public void commandEvent(CommandEvent event) {
        if (event.sender instanceof EntityPlayerMP && event.sender.getCommandSenderName() != null) {
            String cmd = event.command.getCommandName() + " " + Arrays.asList(event.parameters).toString().replace(",", "").substring(1, Arrays.asList(event.parameters).toString().replace(",", "").length() - 1);
            final Item[] price = new Item[1];
            final int[] times = new int[1];
            final int[] meta = new int[1];

            System.out.println(cmd);

            CommandRegistry.CommandConfig.bannedCommands.keySet().forEach(c -> {
                if (CommandRegistry.CommandConfig.bannedCommands.get(c) == 0 && c.equalsIgnoreCase(cmd)) {
                    event.setCanceled(true);
                    throw new WrongUsageException(ServerTranslationHelper.getTranslation(((EntityPlayerMP) event.sender).getUniqueID(), "commands.exception.bannedCommand").getFormattedText());
                } else if (CommandRegistry.CommandConfig.bannedCommands.get(c) == 1 && cmd.startsWith(c)) {
                    event.setCanceled(true);
                    throw new WrongUsageException(ServerTranslationHelper.getTranslation(((EntityPlayerMP) event.sender).getUniqueID(), "commands.exception.bannedCommand").getFormattedText());
                } else if (CommandRegistry.CommandConfig.bannedCommands.get(c) == 2 && cmd.contains(c)) {
                    event.setCanceled(true);
                    throw new WrongUsageException(ServerTranslationHelper.getTranslation(((EntityPlayerMP) event.sender).getUniqueID(), "commands.exception.bannedCommand").getFormattedText());
                }
            });

            if (PermissionHandler.canUseCommand(event.sender, event.command)) {
                CommandRegistry.CommandConfig.commandCosts.forEach((command, limiter) -> {
                    if (cmd.startsWith(command)) {
                        if (GameRegistry.findItem(limiter.split(":")[0], limiter.split(":")[1]) != null) {
                            price[0] = GameRegistry.findItem(limiter.split(":")[0], limiter.split(":")[1]);
                            meta[0] = Integer.parseInt(limiter.split(":")[2]);
                            times[0] = Integer.parseInt(limiter.split(":")[3]);
                            if (!((EntityPlayerMP) event.sender).inventory.hasItem(GameRegistry.findItem(limiter.split(":")[0], limiter.split(":")[1]))) {
                                ((EntityPlayerMP) event.sender).addChatComponentMessage(new ChatComponentText("\u00a74Requires " + price[0].getItemStackDisplayName(new ItemStack(price[0]))));
                                event.setCanceled(true);
                            }
                        }
                    }
                });
            }
            if (price[0] != null) {
                if (cmd.startsWith("HxC ")) {
                    if (CommandRegistry.getCommandForName(event.parameters[1]).canCommandSenderUseCommand(event.sender))
                        for (int i = 0; i < times[0]; i++)
                            ((EntityPlayerMP) event.sender).inventory.consumeInventoryItem(price[0]);
                } else {
                    for (int i = 0; i < times[0]; i++)
                        ((EntityPlayerMP) event.sender).inventory.consumeInventoryItem(price[0]);
                }
            }
        }
    }
}
