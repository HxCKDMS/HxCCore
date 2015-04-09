package HxCKDMS.HxCCore.Crash;

import net.minecraft.crash.CrashReport;

public class CrashSendThread implements Runnable {
    private CrashReport crashReport;

    public CrashSendThread(CrashReport crashReport) {
        this.crashReport = crashReport;
    }

    @Override
    public void run() {

    }
}
