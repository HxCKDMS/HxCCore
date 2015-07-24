package HxCKDMS.HxCCore.Crash;

import HxCKDMS.HxCCore.HxCCore;
import com.google.gson.Gson;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Objects;

public class CrashReportThread extends Thread {
    @Override
    public void run() {
        if(CrashHandler.hasCrashed){
            System.out.println("CRASH!!!!!!!");
            try{
                if(checkCrash(FMLCommonHandler.instance().getSide().isClient())) {
                    System.err.println("Crash-report successfully sent to github.");
                }else{
                    System.err.println("Crash-report wasn't sent due an unknown issue.");
                }
            }catch (Exception exception){
                System.err.println("We failed to analyze crash-reports, reason: " + exception);
            }
        }else{
            System.err.println("No crashes have happened");
        }
    }

    private boolean checkCrash(final boolean isClient) throws IOException {
        File folder = new File(isClient ? Minecraft.getMinecraft().mcDataDir : new File("."), "crash-reports");
        File[] logs = folder.listFiles(new filter(isClient));

        if (logs == null) return false;

        File mostRecent = null;
        for (File log : logs) {
            if (mostRecent == null || log.getName().compareTo(mostRecent.getName()) > 0) {
                mostRecent = log;
            }
        }
        if (mostRecent == null) return false;

        byte[] crashFileBytes = Files.readAllBytes(Paths.get(mostRecent.getPath()));
        String crashFileString = new String(crashFileBytes, "UTF-8");

        return crashFileString.contains("at " + HxCCore.class.getPackage().getName()) && sendCrash(crashFileString);

    }

    private boolean sendCrash(String crash) throws IOException{
        System.out.println(crash);

        HttpURLConnection connection = (HttpURLConnection) new URL("http://paste.ee/api").openConnection();
        connection.setDoOutput(true); connection.setDoInput(true);

        Gson gson = new Gson();
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write("key=1fead29cd2729b1786fe6b11df369328&description=HxCKDMS crash&language=java&paste=" + crash);
        writer.flush();
        writer.close();


        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line).append("\n");

        String status = (gson.fromJson(sb.toString(), pasteeeTemplate.class).status);

        if(Objects.equals(status, "success")){
            //GITHUB CODE
        }

        connection.disconnect();
        return true;
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

    class pasteeeTemplate {
        String status;
    }
}
