package io.common.hoony.noticeboard.error;

public class DKFileMaximumEAException extends RuntimeException {
    public DKFileMaximumEAException(String message) {
        super(message);
    }

    public DKFileMaximumEAException(String message, Throwable cause) {
        super(message, cause);
    }
}
