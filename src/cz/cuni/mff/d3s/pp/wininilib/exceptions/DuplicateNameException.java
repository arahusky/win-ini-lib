package cz.cuni.mff.d3s.pp.wininilib.exceptions;

/**
 * This exception is thrown when there is an attept to create multiple sections
 * or properties with the same identifiers (keys).
 *
 * @author xxx
 */
public class DuplicateNameException extends WinIniLibException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>DupliciteNameException</code> without
     * detail message.
     */
    public DuplicateNameException() {
    }

    /**
     * Constructs an instance of <code>DupliciteNameException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DuplicateNameException(String msg) {
        super(msg);
    }
}
