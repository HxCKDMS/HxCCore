package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.List;

import static HxCKDMS.HxCCore.lib.References.*;

@SuppressWarnings({"unchecked", "unused"})
@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandAFK implements ISubCommand {
    public static CommandAFK instance = new CommandAFK();
    //TODO: See if there is a better way to code this??? Players who're AFK CAN'T MOVE and Can't DIE... (Make config to make it so only player damage is canceled)
    //TODO: make a time delay between excecutions of the command... and add a delay for the damage prevention so it can't be used as a damage prevention
    @Override
    public String getCommandName() {
        return "AFK";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayer player = (EntityPlayer)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("AFK"), player);
            if (CanSend) {
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
                String tmp = player.getDisplayName() + CC + NBTFileIO.getString(CustomPlayerData, "Color");
                ChatComponentText AFK = new ChatComponentText(tmp + " has gone AFK.");
                ChatComponentText Back = new ChatComponentText(tmp + " is no longer AFK.");
                IAttributeInstance ps = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed);
                AttributeModifier SpeedDeBuff = new AttributeModifier(SpeedUUID, "AFKDeBuff", -1000, 1);
                IAttributeInstance pd = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage);
                AttributeModifier DMGDeBuff = new AttributeModifier(DMdeBuffUUID, "AFKDeBuff", -1000000000, 1);
                boolean AFKStatus;
                try {
                    AFKStatus = NBTFileIO.getBoolean(CustomPlayerData, "AFK");
                    NBTFileIO.setBoolean(CustomPlayerData, "AFK", !AFKStatus);
                } catch (Exception ignored) {
                    AFKStatus = false;
                    NBTFileIO.setBoolean(CustomPlayerData, "AFK", true);
                }
                if (Configurations.afkExtras) {
                    NBTFileIO.setBoolean(CustomPlayerData, "god", !AFKStatus);
                    player.setInvisible(!AFKStatus);
                    if (!AFKStatus) {
                        ps.applyModifier(SpeedDeBuff);
                        pd.applyModifier(DMGDeBuff);
                        player.addPotionEffect(new PotionEffect(Potion.digSlowdown.getId(), 100000, 254, true));
                    } else {
                        ps.removeModifier(SpeedDeBuff);
                        pd.removeModifier(DMGDeBuff);
                        player.removePotionEffect(Potion.digSlowdown.getId());
                    }
                }
                List<EntityPlayerMP> list = HxCCore.server.getConfigurationManager().playerEntityList;
                for (EntityPlayerMP p : list)
                    p.addChatMessage(AFKStatus ? Back : AFK);
            } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2)
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        return null;
    }
}
