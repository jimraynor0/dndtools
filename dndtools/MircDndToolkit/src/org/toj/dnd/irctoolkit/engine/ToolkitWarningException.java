package org.toj.dnd.irctoolkit.engine;

public class ToolkitWarningException extends Exception {
    private static final long serialVersionUID = 6341831247718196673L;

    public ToolkitWarningException() {
        super();
    }

    public ToolkitWarningException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ToolkitWarningException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolkitWarningException(String message) {
        super(message);
    }

    public ToolkitWarningException(Throwable cause) {
        super(cause);
    }
}
