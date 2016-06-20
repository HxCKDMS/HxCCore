package HxCKDMS.HxCCore.api.Utils;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.NickHandler;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.util.*;

public class ColorHelper {
    private static List<Character> colours = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private static List<Character> effects = Arrays.asList('l', 'n', 'o', 'k', 'm', 'r');

    private Character  activeColour;
    private List<Character> activeEffects;
    private StringBuilder messageBuilder;

    private String message;
    private Character defaultColour;

    private ColorHelper (String message, Character defaultColour) {
        this.message = message;
        this.defaultColour = defaultColour;
        messageBuilder = new StringBuilder();
        activeColour = defaultColour;
        activeEffects = new ArrayList<>();
    }

    private String colour() {
        List<String> words = Arrays.asList(message.split(" "));

        for (String word : words) {
            ListIterator<Character> iterator = Arrays.asList(ArrayUtils.toObject(word.toCharArray())).listIterator();
            StringBuilder wordBuilder = new StringBuilder();

            wordBuilder.append('\u00a7').append(activeColour);
            activeEffects.forEach(c -> wordBuilder.append('\u00a7').append(c));

            Character previousCharacter = null;
            while (iterator.hasNext()) {
                Character character = iterator.next();
                if (previousCharacter != null && colours.contains(character)) {
                    wordBuilder.append(previousCharacter == '&' ? '\u00a7' : previousCharacter);
                    if (previousCharacter == '&') activeColour = character;
                } else if (previousCharacter != null && effects.contains(character)) {
                    wordBuilder.append(previousCharacter == '&' ? '\u00a7' : previousCharacter);
                    if (previousCharacter == '&' && character == 'r') {
                        activeEffects.clear();
                        activeColour = defaultColour;
                    } else if (previousCharacter == '&') activeEffects.add(character);
                }  else if (previousCharacter != null) wordBuilder.append(previousCharacter);
                previousCharacter = character;
            }
            wordBuilder.append(previousCharacter);
            messageBuilder.append(' ').append(wordBuilder.toString());
        }

        if (message.isEmpty()) return "";
        else return messageBuilder.toString().trim();
    }

    public static ChatComponentTranslation handleChat(String message, EntityPlayerMP player) {
        UUID UUID = player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        if (!CustomPlayerData.exists()) throw new NullPointerException();

        Character defaultColour = 'f';
        String playerColour = NBTFileIO.getString(CustomPlayerData, "color");
        if (!playerColour.equals("")) defaultColour = playerColour.charAt(0);
        message = new ColorHelper(defaultColour + message, defaultColour).colour();

        return new ChatComponentTranslation(Configurations.formats.get("ChatFormat").replace("HEADER", NickHandler.getMessageHeader(player)).replace("MESSAGE", message).trim());
    }

    public static String handleSign(String text) {
        return new ColorHelper(text, '0').colour().concat("\u00a70");
    }
}