package org.mobai.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

public abstract class HttpFilter extends GenericFilter {
  @Serial
  private static final long serialVersionUID = 7478463438252262094L;

  public HttpFilter() {
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
      this.doFilter((HttpServletRequest)req, (HttpServletResponse)res, chain);
    } else {
      throw new ServletException("non-HTTP request or response");
    }
  }

  protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
    chain.doFilter(req, res);
  }
}