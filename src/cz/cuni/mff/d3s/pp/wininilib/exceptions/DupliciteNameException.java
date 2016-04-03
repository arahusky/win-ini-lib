package cz.cuni.mff.d3s.pp.wininilib.exceptions;

/**
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class DupliciteNameException extends WinIniLibException {

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
