package cz.cuni.mff.d3s.pp.wininilib.exceptions;

/**
 * Bad file format.
 * 
 * @author Jakub Naplava; Jan Kluj
 */
public class FileFormatException extends WinIniLibException {

    /**
     * Creates a new instance of <code>FileFormatException</code> without detail
     * message.
     */
    public FileFormatException() {
    }

    /**
     * Constructs an instance of <code>FileFormatException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FileFormatException(String msg) {
        super(msg);
    }
}
