package hxckdms.hxccore.crash;

import java.io.Serializable;

public class CrashSerializer implements Serializable {
    private static final long serialVersionUID = 5950169519310163575L;

    public Throwable error;
    public String crashString;

    public CrashSerializer(Throwable error, String crashString) {
        this.error = error;
        this.crashString = crashString;
    }
}
