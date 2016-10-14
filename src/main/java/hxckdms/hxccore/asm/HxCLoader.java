package hxckdms.hxccore.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.10.2")
@IFMLLoadingPlugin.TransformerExclusions({"hxckdms.hxccore.asm"})
@IFMLLoadingPlugin.SortingIndex(1001)
public class HxCLoader implements IFMLLoadingPlugin {
    static boolean RuntimeDeobf = false;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                HxCTransformer.class.getCanonicalName()
        };
    }

    @Override
    public String getModContainerClass() {
        return HxCContainer.class.getCanonicalName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        RuntimeDeobf = (Boolean) data.get("runtimeDeobfuscationEnabled");

        String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

        String realArch = arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64") ? "64" : "32";

        System.out.println(System.getProperty("java.version"));

        try {
            int major = Integer.parseInt(System.getProperty("java.version").split("\\.")[1]);
            if (major < 8) throw new RuntimeException("Old java version detected, please download the latest " + realArch + "-bit version of java from http://java.com/en/download/manual.jsp");
        } catch (NumberFormatException ignored) {}
    }

    @Override
    public String getAccessTransformerClass() {
        return HxCAccessTransformer.class.getCanonicalName();
    }
}
