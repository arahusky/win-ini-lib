package cz.cuni.mff.d3s.pp.wininilib.exceptions;

/**
 * Bad file format.
 *
 * @author xxx
 */
public class FileFormatException extends WinIniLibException {

    private static final long serialVersionUID = 1L;
    
    public WinIniLibException innerException = null;

    /**
     * Creates a new instance of <code>FileFormatException</code> without detail
     * message.
     */
    public FileFormatException() {
    }

    /**
     * Creates a new instance of <code>FileFormatException</code> with detailed
     * message and inner exception.
     *
     * @param msg the detail message.
     * @param innerException the inner exception to set.
     */
    public FileFormatException(String msg, WinIniLibException innerException) {
        super(msg);
        this.innerException = innerException;
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
