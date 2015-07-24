package HxCKDMS.HxCCore.Crash;

import cpw.mods.fml.common.ICrashCallable;

public class CrashHandler implements ICrashCallable {
    public static final CrashReportThread crashReportThread = new CrashReportThread();
    static boolean hasCrashed = false;

    @Override
    public String getLabel() {
        return "HxCKDMS Crash Check";
    }

    @Override
    public String call() throws Exception {
        hasCrashed = true;

        crashReportThread.setName("HxCKDMS Crash check thread");
        return "Will analyze crash log and send the error to github if HxCKDMS Core is possibly involved.";
    }
}
