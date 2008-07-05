package org.basex.query.xpath.expr;

import org.basex.data.MetaData;
import org.basex.data.Serializer;
import org.basex.query.FTOpt;
import org.basex.query.QueryException;
import org.basex.query.xpath.XPContext;
import org.basex.query.xpath.locpath.Step;
import org.basex.util.TokenBuilder;

/**
 * This is an abstract class for fulltext array expressions.
 * 
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Christian Gruen
 */
public abstract class FTArrayExpr extends Expr {
  /** Array with arguments. */
  protected FTArrayExpr[] exprs;
  /** Fulltext option. */
  public FTOpt fto;
  /** Fulltext position filter. */
  public FTPositionFilter ftpos;

  @Override
  public FTArrayExpr compile(final XPContext ctx) throws QueryException {
    for(int i = 0; i < exprs.length; i++) exprs[i] = exprs[i].compile(ctx);
    return this;
  }

  /**
   * Verifies if the fulltext query options comply with the index defaults.
   * Check where this method is overwritten to get more info.
   * @param meta meta data
   * @return result of check
   */
  public boolean indexOptions(final MetaData meta) {
    for(int i = 0; i < exprs.length; i++) 
      if(!exprs[i].indexOptions(meta)) return false;
    return true;
  }

  @Override
  public final boolean usesPos() {
    return false;
  }

  @Override
  public final boolean usesSize() {
    return false;
  }

  @Override
  @SuppressWarnings("unused")
  public FTArrayExpr indexEquivalent(final XPContext ctx, final Step step)
      throws QueryException {
    return null;
  }

  @Override
  public String toString() {
    final TokenBuilder sb = new TokenBuilder(name());
    sb.add('(');
    for(int i = 0; i != exprs.length; i++) {
      if(i != 0) sb.add(", ");
      sb.add(exprs[i]);
    }
    sb.add(')');
    return sb.toString();
  }

  @Override
  public void plan(final Serializer ser) throws Exception {
    ser.openElement(this);
    for(final Expr expr : exprs) expr.plan(ser);
    ser.closeElement(this);
  }
}
