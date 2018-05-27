package hxckdms.hxccore.crash;

import hxckdms.hxccore.libraries.Constants;
import net.minecraft.crash.CrashReport;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

//TODO: doesn't work currently ... fix it eventually
public class CrashHandler extends Thread {
    private static boolean frogeFakeCrash = false;
    private Throwable throwable;
    private String crashString;

    private CrashHandler(String name, Throwable throwable, String crashString) {
        super(name);
        this.throwable = throwable;
        this.crashString = crashString;
    }

    @SuppressWarnings("unused")
    public static void handleCrash(CrashReport crashReport) {
        //if (Configuration.autoCrashReporterEnabled && frogeFakeCrash)
            //Runtime.getRuntime().addShutdownHook(new CrashHandler("HxCKDMS Crash check thread", crashReport.getCrashCause(), crashReport.getCompleteReport()));
        frogeFakeCrash = true;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(InetAddress.getByName(getServerIP()), getServerPort());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            CrashSerializer crash = new CrashSerializer(throwable, crashString);
            output.writeObject(crash);
            output.flush();
            output.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getServerIP() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCLib/master/HxCIssueIP.txt");
        InputStream inputStream = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String ip = reader.readLine().split(":")[0];
        if (ip.equals("")) return Constants.ERROR_REPORT_ADDRESS;
        return ip;
    }

    private int getServerPort() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCLib/master/HxCIssueIP.txt");
        InputStream inputStream = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            return Integer.parseInt(reader.readLine().split(":")[1]);
        } catch (Exception ignored) {
            return Constants.ERROR_REPORT_PORT;
        }
    }
}
