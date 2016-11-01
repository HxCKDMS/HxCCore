package HxCKDMS.HxCCore.Contributors;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CapesThread implements Runnable {
    public static HashMap<String, String> capes = new HashMap<>();
    private static final File rpack = new File(HxCCore.mcRootDir.getAbsolutePath(), "/resourcepacks/hxccapes/");
    private static final File capeDir = new File(rpack, "assets/hxccore/textures/capes/");

    @Override
    public void run() {
        loadFile();
        downloadCapes();
    }

    private void loadFile() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCLib/master/HxCCapes");
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            loadAll(bufferedReader);
        } catch (Exception e) {
            LogHelper.error("Can not resolve HxCCapes", References.MOD_NAME);
            if (Configurations.DebugMode) {
                e.printStackTrace();
            }
        }
    }

    private void loadAll(BufferedReader reader) {
        if (reader != null) {
            try {
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    if (!inputLine.startsWith("//")) {
                        String[] str = inputLine.split("=");
                        capes.put(str[0], str[1]);
                    }
                }
            } catch (Exception e) {
                LogHelper.error("Something went wrong in loading HxCLib report this to DrZed on github @ http://github.com/HxCLib/issues", References.MOD_NAME);
                if (Configurations.DebugMode) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void downloadCapes() {
        if (!rpack.exists()) {
            try {
                File mcmeta = new File(rpack, "pack.mcmeta");
                rpack.mkdirs();
                capeDir.mkdirs();
                mcmeta.createNewFile();
                List<String> lines = Collections.singletonList("{\"pack\":{\"pack_format\":1,\"description\":\"HxCCapes Pack\"}}");
                Path file = mcmeta.toPath();
                Files.write(file, lines, Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        capes.forEach((player, url) -> {
            try {
                File cape = new File(capeDir, player.toLowerCase() + ".png");
                if (!cape.exists())
                    FileUtils.copyURLToFile(new URL(url), cape);
            } catch (IOException e) {
                if (Configurations.DebugMode) {
                    e.printStackTrace();
                }
            }
        });
    }
}
