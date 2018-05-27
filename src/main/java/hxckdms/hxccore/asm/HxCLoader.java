package hxckdms.hxccore.asm;

import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
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

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        try {
            checkAndDownloadDependencies((File) data.get("mcLocation"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        RuntimeDeobf = (Boolean) data.get("runtimeDeobfuscationEnabled");

        try {
            int major = Integer.parseInt(System.getProperty("java.version").split("\\.")[1]);
            if (major < 8) throw new RuntimeException("Old java version detected, please download the latest version of java from http://java.com/en/download/manual.jsp");
        } catch (NumberFormatException ignored) {}
    }

    @Override
    public String getAccessTransformerClass() {
        return HxCAccessTransformer.class.getCanonicalName();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void checkAndDownloadDependencies(File mcDir) throws IOException {
        URL depListURL = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCLib/master/configAPIStable.txt");
        InputStream depLinkInputStream = depListURL.openStream();
        BufferedReader depLinkReader = new BufferedReader(new InputStreamReader(depLinkInputStream));
        URL depDownload = new URL(depLinkReader.readLine());
        depLinkReader.close();
        depLinkInputStream.close();

        File file = new File(mcDir, "/mods/1.12.2");
        file.mkdirs();
        String fileName = depDownload.toString().split("/")[depDownload.toString().split("/").length - 1];
        File dependency = new File(file, fileName);
        if (dependency.exists()) return;

        if (dependency.createNewFile()) {
            File[] files = file.listFiles(pathname -> pathname.isFile() && pathname.getName().matches("ConfigurationAPI-\\d+\\.\\d+\\.jar") && !pathname.getName().equalsIgnoreCase(fileName));
            if (files != null) Arrays.stream(files).forEach(File::delete);
        }

        BufferedOutputStream depOutStream = new BufferedOutputStream(new FileOutputStream(dependency));
        BufferedInputStream depInStream = new BufferedInputStream(depDownload.openStream());
        int in;
        while ((in = depInStream.read()) != -1) depOutStream.write(in);
        depOutStream.close();
        depInStream.close();

        ((LaunchClassLoader) this.getClass().getClassLoader()).addURL(dependency.toURI().toURL());
    }
}
