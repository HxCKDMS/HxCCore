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
import java.net.URL;
import java.util.ArrayList;

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

        ArrayList<String> crash = new ArrayList<>();
        String line;
        boolean hasMod = false;
        while ((line = reader.readLine()) != null) {
            crash.add(line);
            if(line.contains("at HxCKDMS")) hasMod = true;
        }
        reader.close();

        if(Configurations.lastCheckedCrash.equals(mostRecent.getName())) return;

        Configurations.lastCheckedCrash = mostRecent.getName();
        HxCCore.hxCConfig.handleConfig(Configurations.class, HxCCore.HxCConfigFile);

        if (hasMod) sendToServer(crash);
    }

    private void sendToServer(ArrayList<String> crash) throws IOException {
        Socket socket = new Socket(InetAddress.getByName(getServerIP()), getServerPort());
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        String json = gson.toJson(new crashSendTemplate(crash));

        try {
            sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        ArrayList<String> crash;

        public crashSendTemplate(ArrayList<String> crash) {
            this.crash = crash;
        }
    }

    private String getServerIP() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCLib/master/HxCIssueIP.txt");
        InputStream inputStream = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String ip = reader.readLine().split(":")[0];
        if(ip.equals("")) return References.ERROR_REPORT_ADDRESS;
        return ip;
    }

    private int getServerPort() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCLib/master/HxCIssueIP.txt");
        InputStream inputStream = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            return Integer.parseInt(reader.readLine().split(":")[1]);
        } catch (Exception ignored) {
            return References.ERROR_REPORT_PORT;
        }
    }
}
