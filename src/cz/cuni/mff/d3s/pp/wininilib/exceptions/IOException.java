package cz.cuni.mff.d3s.pp.wininilib.exceptions;

/**
 * Basically only wrapper for <i>java.io.Exception</i>.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class IOException extends WinIniLibException {

    /**
     * Creates a new instance of <code>IOException</code> without detail
     * message.
     */
    public IOException() {
    }

    /**
     * Constructs an instance of <code>IOException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public IOException(String msg) {
        super(msg);
    }
}