package hxckdms.hxccore.crash;

import hxckdms.hxccore.configs.Configuration;
import net.minecraft.crash.CrashReport;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class CrashHandler extends Thread {
    private static boolean frogeFakeCrash = false;
    private Throwable throwable;
    private String crashString;

    public CrashHandler(String name, Throwable throwable, String crashString) {
        super(name);
        this.throwable = throwable;
        this.crashString = crashString;
    }

    public static void handleCrash(CrashReport crashReport) {
        if (Configuration.autoCrashReporterEnabled && frogeFakeCrash) {

            Runtime.getRuntime().addShutdownHook(new CrashHandler("HxCKDMS Crash check thread", crashReport.getCrashCause(), crashReport.getCompleteReport()));
            System.out.println("test");
        }
        frogeFakeCrash = true;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(InetAddress.getByName("localhost"), 7777);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            CrashSerializer crash = new CrashSerializer(throwable, crashString);
            output.writeObject(crash);
            //output.flush();
            output.close();
            socket.close();

            System.out.println("done");

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
