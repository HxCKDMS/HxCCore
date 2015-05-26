package HxCKDMS.HxCCore.Contributors;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Utils.LogHelper;
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
    public void loadFile(){
        try {
            URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCLib/master/HxCLib.txt");
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            loadAll(bufferedReader);
        } catch (Exception e) {
            LogHelper.error("Can not resolve HxCLib.txt", References.MOD_NAME);
            if (Config.DebugMode) {
                e.printStackTrace();
            }
        }
    }

    public void loadAll(BufferedReader reader){
        if (reader != null) {
            try {
            String inputLine;
                while((inputLine = reader.readLine()) != null){
                    if (inputLine.startsWith("Coder:")) {
                        HxCCore.coders.add(UUID.fromString(inputLine.replace("Coder:","").trim()));
                    } else if (inputLine.startsWith("Helper:")) {
                        HxCCore.supporters.add(UUID.fromString(inputLine.replace("Helper:","").trim()));
                    } else if (inputLine.startsWith("Supporter:")) {
                        HxCCore.helpers.add(UUID.fromString(inputLine.replace("Supporter:","").trim()));
                    } else if (inputLine.startsWith("Artist:")) {
                        HxCCore.artists.add(UUID.fromString(inputLine.replace("Artist:","").trim()));
                    } else if (inputLine.startsWith("Mascot:")) {
                        HxCCore.mascots.add(UUID.fromString(inputLine.replace("Mascot:","").trim()));
                    }
                }
            } catch (Exception ignored) {
                LogHelper.error("Something went wrong in loading HxCLib report this to DrZed on github @ http://github.com/HxCLib/issues", References.MOD_NAME);
            }
        }
    }
}
