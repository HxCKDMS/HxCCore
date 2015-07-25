package HxCKDMS.HxCCore.Crash;

import cpw.mods.fml.common.ICrashCallable;

public class CrashHandler implements ICrashCallable {
    static boolean hasCrashed = false;

    @Override
    public String getLabel() {
        return "HxCKDMS Crash Check";
    }

    @Override
    public String call() throws Exception {
        hasCrashed = true;
        return "Will analyze crash log and send the error to github if HxCKDMS Core is possibly involved.";
    }
}
