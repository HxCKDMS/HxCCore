package HxCKDMS.HxCCore.Handlers;

import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.Reference;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class KeyInputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(Keybindings.BloodDestruction.isPressed()){
            LogHelper.info("BloodDestruction Activated!", Reference.MOD_NAME);
        }
    }
}