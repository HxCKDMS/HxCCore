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

import static HxCKDMS.HxCCore.lib.References.*;

public class ColorHelper {

    private boolean first, needsNext, end, needsLast, okUseAND;
    private Character character, activeColour;
    private List<Character> activeEffects;
    private StringBuilder messageBuilder;
    private ListIterator<Character> iterator;

    private ColorHelper (String message, Character defaultColour) {
        iterator = Arrays.asList(ArrayUtils.toObject(message.toCharArray())).listIterator();
        messageBuilder = new StringBuilder();
        activeColour = defaultColour;
        activeEffects = new ArrayList<>();
    }

    private void addAllColoursAndEffectsForSpecials() {
        if (iterator.hasNext()) {
            character = iterator.next();
            if(!character.equals(' ') && !character.equals('&')) addAllColoursAndEffects();
        } else character = ' ';
    }

    private void addAllColoursAndEffects() {
        messageBuilder.append("\u00a7").append(activeColour);
        if (!activeEffects.isEmpty()) activeEffects.forEach(a -> messageBuilder.append("\u00a7").append(a));
    }

    private void handleSpecialCharacter() throws NullPointerException {
        if (iterator.hasNext()) {
            Character nextCharacter = iterator.next();
            if (colours.contains(nextCharacter)) {
                activeColour = nextCharacter;

                addAllColoursAndEffectsForSpecials();
                if(!iterator.hasNext() && first) throw new NullPointerException("Empty message");

            } else if(effects.contains(nextCharacter)) {
                if (nextCharacter.equals('r')) activeEffects.clear();
                else activeEffects.add(nextCharacter);

                addAllColoursAndEffectsForSpecials();
                if(!iterator.hasNext() && first) throw new NullPointerException("Empty message");

            } else {
                okUseAND = true;
                character = nextCharacter;
                messageBuilder.append('&');
                needsNext = false;
                needsLast = false;
                first = false;
            }
        } else {
            messageBuilder.append('&');
            end = true;
        }
    }

    private String colour() throws NullPointerException {
        first = true; end = false; needsNext = true;

        while (iterator.hasNext() || !needsNext) {
            if (needsNext) character = iterator.next();
            needsNext = true; needsLast = true; okUseAND = false;

            if (character.equals('&')) handleSpecialCharacter();

            if (first && !character.equals('&') && !end) {
                addAllColoursAndEffects();
                messageBuilder.append(character);
                first = false;
                needsLast = false;
            } else if (character.equals('&') && !okUseAND && !end) {
                needsNext = false;
            }

            if (character.equals(' ')) {
                needsLast = false;
                if (iterator.hasNext()) {
                    Character nextCharacter = iterator.next();
                    if (nextCharacter.equals(' ') || nextCharacter.equals('&')) {
                        messageBuilder.append(' ');
                        needsNext = false;
                        character = nextCharacter;
                    } else {
                        addAllColoursAndEffects();
                        character = nextCharacter;
                        needsNext = false;
                    }
                }
            }

            if (needsLast && !character.equals('&') && !end) {
                messageBuilder.append(character);
            } else if (character.equals('&') && !okUseAND && !end) {
                needsNext = false;
            }
        }
        return messageBuilder.toString();
    }

    public static ChatComponentTranslation handleChat(String message, EntityPlayerMP player) throws NullPointerException {
        UUID UUID = player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        if (!CustomPlayerData.exists()) throw new NullPointerException();

        Character defaultColour = 'f';
        String playerColour = NBTFileIO.getString(CustomPlayerData, "color");
        if (!playerColour.equals("")) defaultColour = playerColour.charAt(0);
        message = message.replace("https://www.youtube.com/watch?v=", "http://youtu.be/").replace("http://www.youtube.com/watch?v=", "http://youtu.be/");

        message = new ColorHelper(message, defaultColour).colour();

        return new ChatComponentTranslation(Configurations.formats.get("ChatFormat").replace("HEADER", NickHandler.getMessageHeader(player)).replace("MESSAGE", message));
    }

    public static String handleSign(String text) {
        return new ColorHelper(text, '0').colour().concat("\u00a70");
    }
}