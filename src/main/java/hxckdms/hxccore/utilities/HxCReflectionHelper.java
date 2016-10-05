package hxckdms.hxccore.utilities;

import net.minecraft.launchwrapper.Launch;

import java.lang.reflect.Field;

public class HxCReflectionHelper {
    public static Field getDeclaredField(Class<?> clazz, String unObfuscatedFieldName, String obfuscatedFieldName) throws NoSuchFieldException {
        return clazz.getDeclaredField((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") ? unObfuscatedFieldName : obfuscatedFieldName);
    }
}
