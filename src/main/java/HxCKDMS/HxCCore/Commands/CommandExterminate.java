package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandExterminate implements ISubCommand {
    public static CommandExterminate instance = new CommandExterminate();
    //TODO: Add safety check for owned and named entities....
    @Override
    public String getCommandName() {
        return "Exterminate";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{1, 1, 1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            if (CanSend) {
                int tmp = 0;
                List<Entity> ents = HxCCore.server.worldServerForDimension(0).loadedEntityList;
                if (args.length >= 2) {
                    for (Entity ent : ents) {
                        if (args[1].contains("hostile") && (ent instanceof EntityMob || ent instanceof EntitySlime)) {
                            ent.setDead();
                            tmp += 1;
                        }
                        else if (args[1].contains("item") && ent instanceof EntityItem) {
                            ent.setDead();
                            tmp += 1;
                        }
                        else if (args[1].contains("xp") && ent instanceof EntityXPOrb) {
                            ent.setDead();
                            tmp += 1;
                        }
                        else if (args[1].contains("passive") && ent instanceof EntityAnimal && !(ent instanceof EntityTameable)) {
                            ent.setDead();
                            tmp += 1;
                        }
                        else if (args[1].contains("tame") && ent instanceof EntityTameable) {
                            ent.setDead();
                            tmp += 1;
                        }
                        else if (args[1].contains("protected") && ent.getEntityData().getString("name").equals("protected")) {
                            ent.setDead();
                            tmp += 1;
                        }
                        else if (ent.getCommandSenderName().toLowerCase().contains(args[1].toLowerCase()) && !(ent instanceof EntityTameable)) {
                            ent.setDead();
                            tmp += 1;
                        }
                    }
                }
                else
                    for (Entity ent : ents)
                        if (!(ent instanceof EntityPlayer) && !(ent instanceof EntityTameable)) {
                            ent.setDead();
                            tmp += 1;
                        }
                player.addChatMessage(new ChatComponentText("You have exterminated " + tmp + " entities!"));
            } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return Arrays.asList(new String[]{"hostiles", "items", "passives", "exp", "tamed"});
    }
}
