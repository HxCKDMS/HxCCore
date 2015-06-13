package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class CommandAFK implements ISubCommand {
    public static CommandAFK instance = new CommandAFK();

    @Override
    public String getName() {
        return "afk";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws PlayerNotFoundException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Config.PermLevels[14], player);
            if (CanSend) {
                UUID SpeedUUID = UUID.fromString("fe15f828-62d7-11e4-b116-123b93f75cba");
                ChatComponentText AFK = new ChatComponentText(player.getDisplayName() + " \u00A73has gone AFK.");
                ChatComponentText Back = new ChatComponentText(player.getDisplayName() + " \u00A73is no longer AFK.");
                IAttributeInstance ps = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed);
                AttributeModifier SpeedBuff = new AttributeModifier(SpeedUUID, "AFKDeBuff", -100, 1);
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
                boolean AFKStatus;
                try {
                    AFKStatus = NBTFileIO.getBoolean(CustomPlayerData, "AFK");
                    NBTFileIO.setBoolean(CustomPlayerData, "AFK", !AFKStatus);
                } catch (Exception ignored) {
                    AFKStatus = false;
                    NBTFileIO.setBoolean(CustomPlayerData, "AFK", true);
                }
                NBTFileIO.setBoolean(CustomPlayerData, "god", !AFKStatus);
                player.setInvisible(!AFKStatus);
                if (!AFKStatus) ps.applyModifier(SpeedBuff);
                else ps.removeModifier(SpeedBuff);
                List<EntityPlayerMP> list = HxCCore.server.getConfigurationManager().playerEntityList;
                for (EntityPlayerMP p : list) {
                    p.addChatMessage(AFKStatus ? Back : AFK);
                }
            } else sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
