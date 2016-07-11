package HxCKDMS.HxCCore.api.Utils;

import HxCKDMS.HxCCore.Events.EventPlayerNetworkCheck;

import java.util.HashMap;
import java.util.UUID;

public class TestUtil {
    static HashMap<String, String> translations = new HashMap<>();
    
    static {
        translations.put("commands.help.info", "show all commands with usage and explanation.");
        translations.put("commands.heal.info", "heal yourself or another player.");
        translations.put("commands.kill.info", "kill yourself or another player.");
        translations.put("commands.god.info", "put yourself or another player in god mode.");
        translations.put("commands.fly.info", "grand the power of flight to your self or another player.");
        translations.put("commands.feed.info", "feed your self or another player.");
        translations.put("commands.burn.info", "burn your self or another player.");
        translations.put("commands.extinguish.info", "extinguish your self or another player.");
        translations.put("commands.smite.info", "spawn a lightning bolt on target.");
        translations.put("commands.setHome.info", "set a home (waypoint) at your location.");
        translations.put("commands.delhome.info", "delete specified home (waypoint).");
        translations.put("commands.delwarp.info", "delete specified warp (waypoint).");
        translations.put("commands.cannon.info", "fires tnt or cannon at specified velocity.");
        translations.put("commands.updatecheck.info", "checks for updates.");
        translations.put("commands.override.info", "override the protect command");
        translations.put("commands.thaw.info", "melt snow and ice in radius around player.");
        translations.put("commands.reloadconfigs.info", "reload the configs.");
        translations.put("commands.powertool.info", "bind command to tool for execute on tool use.");
        translations.put("commands.rename.info", "rename held item.");
        translations.put("commands.tp.info", "teleport player to another player cross dimension.");
        translations.put("commands.updatecheck.info", "check for updates");
        translations.put("commands.changepermission.info", "temporarily renames/creates a permission class.");
        translations.put("commands.home.info", "return to a home (waypoint).");
        translations.put("commands.setWarp.info", "set a server warp (waypoint) at your location.");
        translations.put("commands.warp.info", "return to a server warp (waypoint).");
        translations.put("commands.repair.info", "repair currently held item.");
        translations.put("commands.repairAll.info", "repair all items in target player's inventory.");
        translations.put("commands.modlist.info", "list all mods installed.");
        translations.put("commands.nick.info", "rename yourself.");
        translations.put("commands.serverinfo.info", "get info about the server.");
        translations.put("commands.setperms.info", "set permmision levels for players on your server.");
        translations.put("commands.spawn.info", "send player to spawn.");
        translations.put("commands.tpa.info", "repair all items in your inventory.");
        translations.put("commands.kit.info", "spawn/create/give specified set of items to/from the inventory to yourself/config/players");
        translations.put("commands.broadcast.info", "sends a message to all players as [SERVER]");
        translations.put("commands.color.info", "sets your default chat colour");
        translations.put("commands.clientinfo.info", "gives you information on players targeted with this command, ping/ip/gamemode");
        translations.put("commands.back.info", "return you to the last location you teleported from, or died at");
        translations.put("commands.setwarp.info", "create a server-wide warp to your current location");
        translations.put("commands.exterminate.info", "kill all entities matching type excludes tamed unless targeted");
        translations.put("commands.drain.info", "drain nearby fluids of specified type");
        translations.put("commands.path.info", "create a path of specified size where ever you walk");
        translations.put("commands.jump.info", "teleport to location your cursor is pointing at");
        translations.put("commands.hat.info", "set item in hand as your hat/helmet");
        translations.put("commands.sethome.info", "set a home to your current location");
        translations.put("commands.afk.info", "go to/return from afk mode");
        translations.put("commands.mute.info", "prevent specified player from communicating");
        translations.put("commands.repairall.info", "repair all items in specified player's inventory");
        translations.put("commands.protect.info", "protects specified region from player interaction");
        
        translations.put("commands.help.usage", "help");
        translations.put("commands.heal.usage", "heal [player]");
        translations.put("commands.kill.usage", "kill [player]");
        translations.put("commands.god.usage", "god [player]");
        translations.put("commands.fly.usage", "fly [player]");
        translations.put("commands.feed.usage", "feed [player]");
        translations.put("commands.burn.usage", "burn [player]");
        translations.put("commands.extinguish.usage", "extinguish [player]");
        translations.put("commands.smite.usage", "smite [player]");
        translations.put("commands.setHome.usage", "setHome [home]");
        translations.put("commands.delhome.usage", "delhome <name>");
        translations.put("commands.delwarp.usage", "delwarp <name>");
        translations.put("commands.cannon.usage", "cannon <velocity> [kitty]");
        translations.put("commands.override.usage", "override [true/false]");
        translations.put("commands.thaw.usage", "thaw [radius]");
        translations.put("commands.reloadconfigs.usage", "reloadconfigs");
        translations.put("commands.powertool.usage", "powertool [command]");
        translations.put("commands.rename.usage", "rename <name>");
        translations.put("commands.tp.usage", "tp [player] <to player>");
        translations.put("commands.updatecheck.usage", "updatecheck");
        translations.put("commands.changepermission.usage", "changepermission <permlevel> <newname> [color]");
        translations.put("commands.home.usage", "home [home]");
        translations.put("commands.setWarp.usage", "setWarp [warp]");
        translations.put("commands.warp.usage", "warp [warp]");
        translations.put("commands.repair.usage", "repair");
        translations.put("commands.repairAll.usage", "repairAll [player]");
        translations.put("commands.modlist.usage", "modlist");
        translations.put("commands.nick.usage", "nick <nick>");
        translations.put("commands.serverinfo.usage", "serverinfo");
        translations.put("commands.setperms.usage", "setperms <player> <permlevel#>");
        translations.put("commands.spawn.usage", "spawn [player]");
        translations.put("commands.tpa.usage", "tpa <accept, deny, player>");
        translations.put("commands.list.usage", "list <homes, warps>");
        translations.put("commands.kit.usage", "kit <spawn, edit, give, create, remove> <kitname> [player]");
        translations.put("commands.broadcast.usage", "broadcast <message>");
        translations.put("commands.color.usage", "color <a-f/0-9>");
        translations.put("commands.clientinfo.usage", "clientinfo [player]");
        translations.put("commands.back.usage", "back");
        translations.put("commands.setwarp.usage", "setwarp [warpname]");
        translations.put("commands.exterminate.usage", "exterminate [hostile/passive/tamed/all/item/xp]");
        translations.put("commands.drain.usage", "drain [range#] [fluidname]");
        translations.put("commands.path.usage", "path [block] [size]");
        translations.put("commands.jump.usage", "jump [range]");
        translations.put("commands.hat.usage", "hat");
        translations.put("commands.sethome.usage", "sethome [homename] [visibletofriends/true/false]");
        translations.put("commands.afk.usage", "afk");
        translations.put("commands.mute.usage", "mute [player]");
        translations.put("commands.repairall.usage", "repairall [player]");
        translations.put("commands.protect.usage", "protect <regionName> <add/transfer/create/delete>");

        translations.put("commands.exception.playeronly", "This command can only be executed by players.");
        translations.put("commands.exception.home.outOfHomes", "You have run out of homes.");

        translations.put("commands.exception.noWarps", "This server does not have any warps.");
        translations.put("commands.exception.noHomes", "You do not have any homes.");
        translations.put("commands.exception.bannedCommand", "This command was banned in the HxCCore Config!");
        translations.put("commands.exception.noHomes", "You do not have any homes.");

        translations.put("commands.alert.attemptedUse", "Player %1$s has tried to execute command /%2$s");

        translations.put("commands.exception.permission", "You may not excecute this command.");
        translations.put("commands.exception.warp", "A warp with this name doesn't exist!");
        translations.put("commands.exception.home", "A home with this name doesn't exist!");

        translations.put("commands.kit.spawn.failure", "You do not have permission to use kit %1$s, or it doesn't exist.");
        translations.put("commands.kit.spawn.success", "You have were given kit %1$s.");
        translations.put("commands.kit.create.failure", "You have not met the requirements to create a kit!");
        translations.put("commands.kit.create.success", "You have successfully created kit %1$s.");
        translations.put("commands.kit.remove.failure", "A kit by the name of %1$s doesn't exist.");
        translations.put("commands.kit.remove.success", "You have successfully removed kit %1$s.");
        translations.put("commands.kit.edit.failure", "You have failed to edit kit %1$s.");
        translations.put("commands.kit.edit.success", "Successfully edited kit %1$s.");
        translations.put("commands.kit.give.failure", "Failed to deliver kit %1$s to %2$s.");
        translations.put("commands.kit.give.success", "You have successfully given kit %1$s to %2$s.");
        translations.put("commands.kit.recieve.success", "You were successfully given kit %1$s.");

        translations.put("death.attack.command_kill.0", "%1$s forgot how to breathe.");
        translations.put("death.attack.command_kill.1", "%1$s has received divine punishment!");
        translations.put("death.attack.command_kill.2", "%1$s died of ebola.");
        translations.put("death.attack.command_kill.3", "%1$s stepped on a lego.");
        translations.put("death.attack.command_kill.4", "%1$s has tripped on a cactus.");
        translations.put("death.attack.command_kill.5", "%1$s fell ill with instant death syndrome.");
        translations.put("death.attack.command_kill.6", "%1$s has tripped over air.");
        translations.put("death.attack.command_kill.7", "%1$s has broke his neck.");
        translations.put("death.attack.command_kill.8", "%1$s has spontaneously combusted.");
        translations.put("death.attack.command_kill.9", "%1$s has spontaneously exploded.");
        translations.put("death.attack.command_kill.10", "%1$s has been atomicly dismantled.");
        translations.put("death.attack.command_kill.11", "%1$s has been removed from existence.");
        translations.put("death.attack.command_kill.12", "%1$s has blew out their sphincter!");
        translations.put("death.attack.command_kill.13", "%1$s has fell into a micro-blackhole.");
        translations.put("death.attack.command_kill.14", "%1$s was annihilated by DrZed.");
        translations.put("death.attack.command_kill.15", "%1$s's luck ran out.");
        translations.put("death.attack.command_kill.16", "%1$s has been slapped into the next universe.");
        translations.put("death.attack.command_kill.17", "%1$s died of butt cancer.");
        translations.put("death.attack.command_kill.18", "%1$s choked on a potato.");
        translations.put("death.attack.command_kill.19", "%1$s done borked physics!");
        translations.put("death.attack.command_kill.20", "%1$s angered a walrus.");
        translations.put("death.attack.command_kill.21", "%1$s fell ontop of a porcupine.");
        translations.put("death.attack.command_kill.22", "%1$s was attacked by a swan.");
        translations.put("death.attack.command_kill.23", "%1$s was killed by a squid.");
        translations.put("death.attack.command_kill.24", "%1$s hit the cactus too hard then blew up swimming in lava.....");
        translations.put("death.attack.command_kill.25", "%1$s slipped on a banana peel.");
        translations.put("death.attack.command_kill.26", "%1$s sat on a cactus.");
        translations.put("death.attack.command_kill.27", "%1$s done did.");
        translations.put("death.attack.command_kill.28", "%1$s had one job...");
        translations.put("death.attack.command_kill.29", "You had one job %1$s and you failed...");
        translations.put("death.attack.command_kill.30", "Rest in potatoes %1$s...");
        translations.put("death.attack.command_kill.31", "Rest in pasta %1$s...");
        translations.put("death.attack.command_kill.32", "RIP %1$s...");
        translations.put("death.attack.command_kill.33", "%1$s...");
        translations.put("death.attack.command_kill.34", "%1$s failed...");
        translations.put("death.attack.command_kill.35", "%1$s turned to dust...");
        translations.put("death.attack.command_kill.36", "%1$s became fertilizer...");
        translations.put("death.attack.command_kill.37", "%1$s decomposed instantly...");
        translations.put("death.attack.command_kill.38", "%1$s prayed to the wrong deity...");
        translations.put("death.attack.command_kill.39", "%1$s asked for it...");
        translations.put("death.attack.command_kill.40", "%1$s vanished...");
        translations.put("death.attack.command_kill.41", "%1$s did a magic trick...");
        translations.put("death.attack.command_kill.42", "%1$s forgot how to exist...");
        translations.put("death.attack.command_kill.43", "%1$s is no longer being observed...");
        translations.put("death.attack.command_kill.44", "%1$s decided to reset...");
        translations.put("death.attack.command_kill.45", "%1$s was replaced with a corpse...");
    
    }
    
    public String getTranslation(UUID uuid, String line) {
        if (EventPlayerNetworkCheck.hasPlayerMod.contains(uuid))
            return line;
        return translations.get(line);
       /*
        try {
            BufferedReader readIn = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
                    .getResourceAsStream("assets/" + mod.toLowerCase() + "/lang/en_US.lang"), "UTF-8"));
            final String[] returnVal = {"Error"};
            readIn.lines().forEach(l -> {
                if (l.contains(line)) {
                    returnVal[0] = l.replaceFirst((line + "="), "");
                }
            });
            readIn.close();
            return returnVal[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
        */
    }
}
