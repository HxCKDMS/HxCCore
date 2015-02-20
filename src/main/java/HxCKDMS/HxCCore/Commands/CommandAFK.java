package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
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
    public String getCommandName() {
        return "afk";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer)sender;
            UUID SpeedUUID = UUID.fromString("fe15f828-62d7-11e4-b116-123b93f75cba");
            ChatComponentText AFK = new ChatComponentText(p.getCommandSenderName() + "\u00A73has gone AFK.");
            ChatComponentText Back = new ChatComponentText(p.getCommandSenderName() + "\u00A73is no longer AFK.");
            IAttributeInstance ps = p.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed);
            double lel = ps.getAttributeValue();
            AttributeModifier SpeedBuff = new AttributeModifier(SpeedUUID, "AFKDeBuff", (lel * -1), 1);
            String UUID = p.getUniqueID().toString();
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
            p.setInvisible(!AFKStatus);
            ps.removeModifier(SpeedBuff);
            ps.applyModifier(SpeedBuff);
            List<EntityPlayerMP> list = HxCCore.server.getConfigurationManager().playerEntityList;
            for (EntityPlayerMP player : list) {
                player.addChatMessage(AFKStatus ? Back : AFK);
            }
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
