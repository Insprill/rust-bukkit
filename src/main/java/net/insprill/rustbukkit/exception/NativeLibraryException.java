package net.insprill.rustbukkit.exception;

public class NativeLibraryException extends RuntimeException {

    public NativeLibraryException(String message) {
        super(message);
    }

    public NativeLibraryException(String message, Throwable cause) {
        super(message, cause);
    }

}
