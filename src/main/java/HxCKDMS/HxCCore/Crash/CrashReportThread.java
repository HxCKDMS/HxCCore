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
import java.util.Calendar;

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
        File[] logs = folder.listFiles(new filter(isClient));

        if (logs == null) return;

        File mostRecent = null;
        for (File log : logs) {
            if (mostRecent == null || log.getName().compareTo(mostRecent.getName()) > 0) {
                mostRecent = log;
            }
        }
        if (mostRecent == null) return;

        BufferedReader reader = new BufferedReader(new FileReader(mostRecent));

        String title = null;
        int lineNumber = 0;
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        String mod = null;
        while ((line = reader.readLine()) != null) {
            if(++lineNumber == 7) title = line;
            stringBuilder.append(line).append("\n");

            if(mod == null && line.contains("at HxCKDMS.")) {
                String[] test;
                if((test = line.split("\\.")).length >= 2) mod = test[1];
            }
        }
        if(Configurations.lastCheckedCrash.equals(mostRecent.getName())) return;

        Configurations.lastCheckedCrash = mostRecent.getName();
        HxCCore.hxCConfig.handleConfig(Configurations.class, HxCCore.HxCConfigFile);

        if (stringBuilder.toString().contains("at HxCKDMS.")) sendToServer(stringBuilder.toString(), title, mod);
    }

    private void sendToServer(String crash, String title, String mod) throws IOException {
        Socket socket = new Socket(InetAddress.getByName(References.ERROR_REPORT_ADDRESS), References.ERROR_REPORT_PORT);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        String json = gson.toJson(new crashSendTemplate(crash, mod, title));

        writer.write(json);
        writer.flush();
        writer.close();
        socket.close();
    }

    class filter implements FileFilter {
        boolean isClient;

        public filter(boolean isClient){
            this.isClient = isClient;
        }

        @Override
        public boolean accept(File file) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -7);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(file.lastModified());

            String name = file.getName();
            return calendar2.after(calendar) && name.startsWith("crash-");
        }
    }

    class crashSendTemplate {
        String crash;
        String mod;
        String title;

        public crashSendTemplate(String crash, String mod, String title) {
            this.crash = crash;
            this.mod = mod;
            this.title = title;
        }
    }
}
