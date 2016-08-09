package org.basex.http.rest;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.basex.core.*;
import org.basex.core.cmd.*;
import org.basex.http.*;
import org.basex.query.value.type.*;

/**
 * Evaluate queries via REST.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
class RESTQuery extends RESTCmd {
  /** External variables. */
  private final Map<String, String[]> vars;
  /** Optional context value. */
  private final String value;

  /**
   * Constructor.
   * @param session REST Session
   * @param vars external variables
   * @param value context value
   */
  RESTQuery(final RESTSession session, final Map<String, String[]> vars, final String value) {
    super(session);
    this.vars = vars;
    this.value = value;
  }

  @Override
  protected void run0() throws IOException {
    query();
  }

  /**
   * Evaluates the specified query.
   * @throws HTTPException REST exception
   * @throws IOException I/O exception
   */
  final void query() throws IOException {
    // set base path and serialization parameters
    final HTTPContext http = session.http;
    context.options.set(MainOptions.SERIALIZER, http.sopts());
    http.initResponse();

    for(final Command cmd : session.commands) {
      if(cmd instanceof XQuery) {
        final XQuery xq = (XQuery) cmd;
        // create query instance
        if(value != null) xq.bind(null, value, NodeType.DOC.toString());

        // bind HTTP context and external variables
        xq.http(http);
        for(final Entry<String, String[]> e : vars.entrySet()) {
          final String key = e.getKey();
          final String[] val = e.getValue();
          if(val.length == 2) xq.bind(key, val[0], val[1]);
          if(val.length == 1) xq.bind(key, val[0]);
        }

        // initializes the response with query serialization options
        http.sopts().assign(xq.parameters(context));
        http.initResponse();
      }
      // run command
      run(cmd, http.res.getOutputStream());
    }
  }

  /**
   * Creates a new instance of this command.
   * @param session REST session
   * @param query query
   * @param vars external variables
   * @param val context value
   * @return command
   * @throws IOException I/O exception
   */
  @SuppressWarnings("unused")
  static RESTQuery get(final RESTSession session, final String query,
      final Map<String, String[]> vars, final String val) throws IOException {

    session.add(new XQuery(query).baseURI(session.context.soptions.get(StaticOptions.WEBPATH)));
    return new RESTQuery(session, vars, val);
  }
}
