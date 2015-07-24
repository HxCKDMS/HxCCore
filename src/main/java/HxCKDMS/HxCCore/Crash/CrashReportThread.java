package HxCKDMS.HxCCore.Crash;

import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;

public class CrashReportThread extends Thread {
    @Override
    public void run() {
        if(CrashHandler.hasCrashed){

        }else{
            LogHelper.info("No crashes have happened", References.MOD_NAME);
        }
    }
}
