package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import scala.actors.threadpool.Arrays;

import java.util.List;

@SuppressWarnings("unchecked")
public class CommandExterminate implements ISubCommand {
    public static CommandExterminate instance = new CommandExterminate();

    @Override
    public String getCommandName() {
        return "exterminate";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Broadcast"), player);
            if (CanSend) {
                int tmp = 0;
                List<Entity> ents = player.worldObj.getLoadedEntityList();
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
                        else if (args[1].contains("passive") && ent instanceof EntityAnimal) {
                            ent.setDead();
                            tmp += 1;
                        }
                    }
                }
                else
                for (Entity ent : ents) {
                    if (!(ent instanceof EntityPlayer)) {
                        ent.setDead();
                        tmp += 1;
                    }
                }
                player.addChatMessage(new ChatComponentText("You have exterminated " + tmp + " entities!"));
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return Arrays.asList(new String[]{"hostiles", "items", "passives", "exp"});
    }
}
