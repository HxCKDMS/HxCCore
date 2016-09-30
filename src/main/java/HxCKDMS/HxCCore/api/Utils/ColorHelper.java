package HxCKDMS.HxCCore.api.Utils;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.api.Handlers.NickHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("unchecked")
public class ColorHelper {
    private static Map<Character, EnumChatFormatting> chatThingies;

    static {
        try {
            Field field = EnumChatFormatting.class.getDeclaredField("formattingCodeMapping");
            field.setAccessible(true);

            chatThingies = (Map<Character, EnumChatFormatting>) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }

    private static ChatComponentTranslation color(String message, char defaultColor) {

        char currentColor = defaultColor;
        HashSet<Character> currentEffects = new HashSet<>();
        LinkedList<String> words = new LinkedList<>(Arrays.asList(message.split(" ")));
        ChatComponentTranslation text = new ChatComponentTranslation("");

        StringBuilder cBuilder = new StringBuilder();

        for (String word : words) {
            LinkedList<Character> characters = new LinkedList<>(Arrays.asList(ArrayUtils.toObject(word.toCharArray())));

            Character prevChar = null;
            for (Character character : characters) {
                if (prevChar != null) {
                    if (prevChar == '%' && chatThingies.get(character) != null) {
                        EnumChatFormatting formatting = chatThingies.get(character);

                        if (cBuilder.length() != 0) {
                            ChatComponentTranslation subText = new ChatComponentTranslation(cBuilder.toString());
                            ChatStyle subStyle = new ChatStyle();
                            subStyle.setColor(chatThingies.get(currentColor))
                                    .setBold(currentEffects.contains(EnumChatFormatting.BOLD.getFormattingCode()))
                                    .setItalic(currentEffects.contains(EnumChatFormatting.ITALIC.getFormattingCode()))
                                    .setObfuscated(currentEffects.contains(EnumChatFormatting.OBFUSCATED.getFormattingCode()))
                                    .setStrikethrough(currentEffects.contains(EnumChatFormatting.STRIKETHROUGH.getFormattingCode()))
                                    .setUnderlined(currentEffects.contains(EnumChatFormatting.UNDERLINE.getFormattingCode()));

                            subText.setChatStyle(subStyle);
                            text.appendSibling(subText);

                            cBuilder = new StringBuilder();
                        }

                        if (formatting.isColor()) {
                            if (!Configurations.bannedColorCharacters.contains(character)) currentColor = character;
                            prevChar = null;
                            continue;
                        } else if (formatting.isFancyStyling()) {
                            if (!Configurations.bannedColorCharacters.contains(character)) currentEffects.add(character);
                            prevChar = null;
                            continue;
                        } else if (formatting == EnumChatFormatting.RESET) {
                            currentColor = 'f';
                            currentEffects = new HashSet<>();
                            prevChar = null;
                            continue;
                        }
                    } else cBuilder.append(prevChar == '%' ? "%%" : prevChar);
                }

                //last
                prevChar = character;
            }
            if (prevChar != null) cBuilder.append(prevChar == '%' ? "%%" : prevChar);
            cBuilder.append(' ');
        }

        ChatComponentTranslation subText = new ChatComponentTranslation(cBuilder.toString());
        ChatStyle subStyle = new ChatStyle();
        subStyle.setColor(chatThingies.get(currentColor))
                .setBold(currentEffects.contains(EnumChatFormatting.BOLD.getFormattingCode()))
                .setItalic(currentEffects.contains(EnumChatFormatting.ITALIC.getFormattingCode()))
                .setObfuscated(currentEffects.contains(EnumChatFormatting.OBFUSCATED.getFormattingCode()))
                .setStrikethrough(currentEffects.contains(EnumChatFormatting.STRIKETHROUGH.getFormattingCode()))
                .setUnderlined(currentEffects.contains(EnumChatFormatting.UNDERLINE.getFormattingCode()));

        subText.setChatStyle(subStyle);
        text.appendSibling(subText);
        return text;
    }

    public static ChatComponentTranslation handleChat(String message, EntityPlayerMP player) {
        UUID UUID = player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        if (!CustomPlayerData.exists()) throw new NullPointerException();

        String playerColor = NBTFileIO.getString(CustomPlayerData, "Color");
        char defaultColor = playerColor.isEmpty() ? 'f' : playerColor.charAt(0);

        return new ChatComponentTranslation(Configurations.formats.get("ChatFormat").replace("HEADER", "%1$s").replace("MESSAGE", "%2$s"), NickHandler.getMessageHeader(player), color(message, defaultColor));
    }

    public static String handleSign(String text) {
        return color(text, EnumChatFormatting.BLACK.getFormattingCode()).getFormattedText();
    }
}