package HxCKDMS.HxCCore.Crash;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.lib.References;
import com.google.gson.Gson;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class CrashReportThread extends Thread {
    private Gson gson = new Gson();

    @Override
    public void run() {
        if(CrashHandler.hasCrashed){
            try{
                checkCrash(FMLCommonHandler.instance().getSide().isClient());
            }catch (Exception ignored){}
        }
    }

    private void checkCrash(final boolean isClient) throws IOException {
        File folder = new File(isClient ? Minecraft.getMinecraft().mcDataDir : new File("."), "crash-reports");
        File[] logs = folder.listFiles(new filter());

        if (logs == null) return;

        File mostRecent = null;
        for (File log : logs) {
            if (mostRecent == null || log.getName().compareTo(mostRecent.getName()) > 0) {
                mostRecent = log;
            }
        }
        if (mostRecent == null) return;

        BufferedReader reader = new BufferedReader(new FileReader(mostRecent));

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        String mod = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");

            if((mod == null || mod.equals("HxCCore")) && line.contains("at HxCKDMS.")) {
                String[] words;
                if((words = line.split("\\.")).length >= 2) mod = words[1];
            }
        }
        reader.close();

        if(Configurations.lastCheckedCrash.equals(mostRecent.getName())) return;

        Configurations.lastCheckedCrash = mostRecent.getName();
        HxCCore.hxCConfig.handleConfig(Configurations.class, HxCCore.HxCConfigFile);

        if (stringBuilder.toString().contains("at HxCKDMS.")) sendToServer(stringBuilder.toString(), mod);
    }

    private void sendToServer(String crash, String mod) throws IOException {
        Socket socket = new Socket(InetAddress.getByName(References.ERROR_REPORT_ADDRESS), References.ERROR_REPORT_PORT);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        String json = gson.toJson(new crashSendTemplate(crash, mod));

        writer.write(json);
        writer.flush();
        writer.close();
        socket.close();
    }

    class filter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return file.getName().startsWith("crash-");
        }
    }

    class crashSendTemplate {
        String crash;
        String mod;

        public crashSendTemplate(String crash, String mod) {
            this.crash = crash;
            this.mod = mod;
        }
    }
}
