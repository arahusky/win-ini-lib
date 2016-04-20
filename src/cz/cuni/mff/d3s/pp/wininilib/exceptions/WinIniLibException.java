package cz.cuni.mff.d3s.pp.wininilib.exceptions;

/**
 * Provides a basic wrapper for WinIniLib exceptions.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class WinIniLibException extends Exception {

    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new instance of <code>WinIniLibException</code> without detail
     * message.
     */
    public WinIniLibException() {
    }

    /**
     * Constructs an instance of <code>WinIniLibException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public WinIniLibException(String msg) {
        super(msg);
    }
}
