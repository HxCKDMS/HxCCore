package HxCKDMS.HxCCore.Handlers;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class Keybinds {
    public static KeyBinding test;
    @SideOnly(Side.CLIENT)
    public static void init() {
        // bind = new Keybinding("key.name", defaultKEY, "key.categories.HxCCore");
        test = new KeyBinding("key.test", Keyboard.KEY_O, "key.categories.HxCCore");
        // Register both KeyBindings to the ClientRegistry
        ClientRegistry.registerKeyBinding(test);
    }
}