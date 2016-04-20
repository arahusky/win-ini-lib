package cz.cuni.mff.d3s.pp.wininilib.exceptions;

/**
 * This exception is thrown when there is an attept to create multiple sections
 * or properties with the same identifiers (keys).
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class DupliciteNameException extends WinIniLibException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>DupliciteNameException</code> without
     * detail message.
     */
    public DupliciteNameException() {
    }

    /**
     * Constructs an instance of <code>DupliciteNameException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DupliciteNameException(String msg) {
        super(msg);
    }
}
