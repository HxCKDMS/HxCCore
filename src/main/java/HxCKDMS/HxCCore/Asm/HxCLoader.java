package HxCKDMS.HxCCore.Asm;

import HxCKDMS.HxCCore.Contributers.CodersCheck;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({"HxCKDMS.HxCCore.Asm"})
@IFMLLoadingPlugin.SortingIndex(1001)
public class HxCLoader implements IFMLLoadingPlugin {
    public static Thread CodersCheckThread = new Thread(new CodersCheck());

    static {
        System.out.println("Hello");
        CodersCheckThread.start();
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                HxCTransformer.class.getName()
        };
    }

    @Override
    public String getModContainerClass() {
        return HxCContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
