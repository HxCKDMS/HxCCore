package HxCKDMS.HxCCore.Asm.Hooks;

import HxCKDMS.HxCCore.Crash.CrashSendThread;
import net.minecraft.crash.CrashReport;

public class CrashHooks {
    public static void handleCrash(CrashReport crashReport){
        new Thread(new CrashSendThread(crashReport));
    }
}
