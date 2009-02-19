package org.basex.test.w3c;

/**
 * XQuery Test Suite Wrapper.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Christian Gruen
 */
public final class XQTS extends W3CTS {
  /**
   * Main method of the test class.
   * @param args command line arguments (ignored)
   * @throws Exception exception
   */
  public static void main(final String[] args) throws Exception {
    new XQTS().init(args);
  }

  /**
   * Constructor.
   */
  public XQTS() {
    super("XQTS");
  }
}
