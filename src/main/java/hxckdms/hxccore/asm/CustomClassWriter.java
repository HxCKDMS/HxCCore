package hxckdms.hxccore.asm;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassWriter;

import java.net.URLClassLoader;

public class CustomClassWriter extends ClassWriter {

    CustomClassWriter(int flags) {
        super(flags);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2)
    {

        type1 = FMLDeobfuscatingRemapper.INSTANCE.unmap(type1);
        type2 = FMLDeobfuscatingRemapper.INSTANCE.unmap(type2);

        Class<?> c, d;
        ClassLoader classLoader = Launch.classLoader.getClass().getClassLoader();
        try {
            c = Class.forName(type1.replace('/', '.'), false, classLoader);
            d = Class.forName(type2.replace('/', '.'), false, classLoader);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }

        if (c.isAssignableFrom(d)) {
            return FMLDeobfuscatingRemapper.INSTANCE.map(type1);
        }

        if (d.isAssignableFrom(c)) {
            return FMLDeobfuscatingRemapper.INSTANCE.map(type2);
        }

        if (c.isInterface() || d.isInterface()) {
            return "java/lang/Object";
        } else {
            do {
                c = c.getSuperclass();
            } while (!c.isAssignableFrom(d));
            return FMLDeobfuscatingRemapper.INSTANCE.map(c.getName().replace('.', '/'));
        }
    }
}
