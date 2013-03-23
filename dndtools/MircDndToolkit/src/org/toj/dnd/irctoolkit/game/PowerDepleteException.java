package org.toj.dnd.irctoolkit.game;

public class PowerDepleteException extends Exception {

    private static final long serialVersionUID = -8765003246183224229L;

    public PowerDepleteException() {
        super();
    }

    public PowerDepleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public PowerDepleteException(String message) {
        super(message);
    }

    public PowerDepleteException(Throwable cause) {
        super(cause);
    }
}
