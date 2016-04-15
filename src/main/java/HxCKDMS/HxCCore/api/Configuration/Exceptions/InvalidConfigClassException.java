package HxCKDMS.HxCCore.api.Configuration.Exceptions;

import java.io.InvalidClassException;

@SuppressWarnings("unused")
public class InvalidConfigClassException extends InvalidClassException {
    public InvalidConfigClassException(String reason) {
        super(reason);
    }

    public InvalidConfigClassException(String cname, String reason) {
        super(cname, reason);
    }
}
