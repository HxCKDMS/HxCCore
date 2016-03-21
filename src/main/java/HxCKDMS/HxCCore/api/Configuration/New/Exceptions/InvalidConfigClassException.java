package HxCKDMS.HxCCore.api.Configuration.New.Exceptions;

import java.io.InvalidClassException;

public class InvalidConfigClassException extends InvalidClassException {
    public InvalidConfigClassException(String reason) {
        super(reason);
    }

    public InvalidConfigClassException(String cname, String reason) {
        super(cname, reason);
    }
}
