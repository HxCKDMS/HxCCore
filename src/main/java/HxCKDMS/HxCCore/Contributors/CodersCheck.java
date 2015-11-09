package HxCKDMS.HxCCore.Contributors;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class CodersCheck implements Runnable {
    @Override
    public void run() {
        loadFile();
    }

    private void loadFile() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCLib/master/HxCLib.txt");
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            loadAll(bufferedReader);
        } catch (Exception e) {
            LogHelper.error("Can not resolve HxCLib.txt", References.MOD_NAME);
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
                        String[] str = inputLine.split(":");
                        HxCCore.HxCLabels.put(UUID.fromString(str[1]), str[0]);
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
}
