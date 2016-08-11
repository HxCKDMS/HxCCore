package HxCKDMS.HxCCore.api.Configuration;

public final class Flags {
    public static final int RETAIN_ORIGINAL_VALUES = 0b1;
    public static final int OVERWRITE = 0b10;

    public static final byte TYPE_HANDLER = 0b1;
    public static final byte COLLECTION_HANDLER = 0b10;


    @Deprecated
    public static final int retainOriginalValues = RETAIN_ORIGINAL_VALUES;
    @Deprecated
    public static final int overwrite = OVERWRITE;
}