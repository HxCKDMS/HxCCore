package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Utils.ColorHelper;
import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EventListener;
import java.util.UUID;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
public class EventChat implements EventListener {

    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        try {
            event.component = ColorHelper.handleChat(event.message, event.player);
        } catch (Exception unhandled) {
            event.setCanceled(true);
        }

        UUID UUID = event.player.getUniqueID();
        File worldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");
        if (!worldData.exists()) worldData.mkdirs();
        try {
            NBTTagCompound mutes = NBTFileIO.getNbtTagCompound(worldData, "mutedPlayers");
            if (mutes.getBoolean(UUID.toString())) {
                event.player.addChatMessage(new ChatComponentText("\u00a76You're muted, attempting to speak is futile."));
                event.setCanceled(true);
            }
        } catch (Exception ignored) {}
    }


    @SubscribeEvent
    public void commandEvent(CommandEvent event) {
        if (event.sender instanceof EntityPlayerMP && event.sender.getCommandSenderName() != null) {
            if (event.command.getCommandName().equals("gamerule")) {
                HxCCore.updateGamerules();
            }
            String cmd = event.command.getCommandName() + " " + Arrays.asList(event.parameters).toString().replace(",", "").substring(1, Arrays.asList(event.parameters).toString().replace(",", "").length() - 1);
            final Item[] price = new Item[1];
            final int[] times = new int[1];

            if (!PermissionsHandler.hasHighestPermissions((EntityPlayerMP) event.sender)) {
                CommandsConfig.CommandCosts.forEach((command, limiter) -> {
                    if (cmd.startsWith(command)) {
                        String[] t = limiter.split("/");
                        if (GameRegistry.findItem(t[1].split(":")[0], t[1].split(":")[1]) != null) {
                            price[0] = GameRegistry.findItem(t[1].split(":")[0], t[1].split(":")[1]);
                            times[0] = Integer.parseInt(t[0]);
                            if (!((EntityPlayerMP) event.sender).inventory.hasItem(GameRegistry.findItem(t[1].split(":")[0], t[1].split(":")[1]))) {
                                ((EntityPlayerMP) event.sender).addChatComponentMessage(new ChatComponentText("\u00a74Requires " + price[0].getItemStackDisplayName(new ItemStack(price[0]))));
                                event.setCanceled(true);
                            }
                        }
                    }
                });
            }

            CommandsConfig.VanillaPermissionOverwrites.forEach((command, level) -> {
                if (command.equalsIgnoreCase(cmd))
                    event.setCanceled(!PermissionsHandler.canUseCommand(level, (EntityPlayerMP) event.sender));
            });

            CommandsConfig.BannedCommands.keySet().forEach(c -> {
                if (CommandsConfig.BannedCommands.get(c) == 0 && c.equalsIgnoreCase(cmd)) {
                    event.setCanceled(true);
                    throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.bannedCommand"));
                } else if (CommandsConfig.BannedCommands.get(c) == 1 && cmd.startsWith(c)) {
                    event.setCanceled(true);
                    throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.bannedCommand"));
                } else if (CommandsConfig.BannedCommands.get(c) == 2 && cmd.contains(c)) {
                    event.setCanceled(true);
                    throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.bannedCommand"));
                }
            });

            if (price[0] != null) {
                if (cmd.startsWith("HxC ")) {
                    if (PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(CommandsHandler.getSubName(event.parameters[0])), (EntityPlayerMP) event.sender))
                        for (int i = 0; i < times[0]; i++)
                            ((EntityPlayerMP) event.sender).inventory.consumeInventoryItem(price[0]);
                } else {
                    for (int i = 0; i < times[0]; i++)
                        ((EntityPlayerMP) event.sender).inventory.consumeInventoryItem(price[0]);
                }
            }

            if (HxCCore.instance.HxCRules.get("LogCommands").equals("true"))
                if (!CommandsConfig.IgnoredCommands.contains(event.command.getCommandName().toLowerCase())) {
                    String time = "[" + String.valueOf(Calendar.getInstance().getTime()) + "] : ";
                    HxCCore.instance.logCommand(time + event.sender.getCommandSenderName() + " tried to execute command [/" + cmd + "]");
                }

            if (HxCCore.instance.HxCRules.get("ReportCommands").equals("true"))
                CommandsConfig.ReportedCommands.forEach(c -> {
                    if (cmd.startsWith(c) && !PermissionsHandler.getPlayersWithPermissionLevel(Configurations.Permissions.size() - 1).contains(event.sender)) {
                        ChatComponentText t = new ChatComponentText(event.sender.getCommandSenderName() + " has attempted to use command /" + cmd);
                        t.getChatStyle().setColor(EnumChatFormatting.GOLD);
                        PermissionsHandler.getPlayersWithPermissionLevel(Configurations.Permissions.size() - 1).forEach(p -> p.addChatMessage(t));
                    }
                });
        }
    }
}