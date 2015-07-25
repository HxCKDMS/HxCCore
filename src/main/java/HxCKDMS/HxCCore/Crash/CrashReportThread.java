package HxCKDMS.HxCCore.Crash;

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
        while ((line = reader.readLine()) != null) {
            if(++lineNumber == 7) title = line;
            stringBuilder.append(line).append("\n");
        }

        if (stringBuilder.toString().contains("at " + HxCCore.class.getPackage().getName())) sendToServer(stringBuilder.toString(), title);
    }

    private void sendToServer(String crash, String title) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), References.ERROR_REPORT_PORT);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        String json = gson.toJson(new crashSendTemplate(crash, "HxCCore", title, References.VERSION));

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
            return calendar.before(calendar2) && name.startsWith("crash-") && name.endsWith(isClient ? "-client.txt" : "-server.txt");
        }
    }

    class crashSendTemplate {
        String crash;
        String mod;
        String title;
        String version;

        public crashSendTemplate(String crash, String mod, String title, String version) {
            this.crash = crash;
            this.mod = mod;
            this.title = title;
            this.version = version;
        }
    }
}
