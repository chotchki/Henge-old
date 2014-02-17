package us.henge.utility.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtectedUrlFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(ProtectedUrlFilter.class);
	
	private static final String handler = "/p";
	
	private static final String[] protect = new String[]{
		
	}; 

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {}

	@Override
	public void destroy() {}
}