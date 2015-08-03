package HxCKDMS.HxCCore.Asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({"HxCKDMS.HxCCore.Asm"})
@IFMLLoadingPlugin.SortingIndex(1001)
public class HxCLoader implements IFMLLoadingPlugin {
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
        return HxCAccessTransformer.class.getName();
    }
}
