package net.insprill.rustbukkit.exception;

/**
 * Indicates an error when working with files.
 */
public class FileException extends RuntimeException {

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

}
