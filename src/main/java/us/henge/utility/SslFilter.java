package us.henge.utility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.henge.init.Config.Keys;

public class SslFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(SslFilter.class);
	private boolean enabled = false;
	private int port = -1;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String portParam = filterConfig.getServletContext().getInitParameter(Keys.publicSslPort.toString());
		
		if(portParam == null){
			return;
		}
		
		try {
			port = Integer.parseInt(portParam);
			enabled = true;
		} catch (NumberFormatException e){
			log.warn("Bad port parameter was passed {}", portParam); 
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(!enabled || request.isSecure()){
			chain.doFilter(request, response);
			return;
		}
		
		HttpServletRequest hRequest = (HttpServletRequest) request;
		HttpServletResponse hResponse = (HttpServletResponse) response;
		
		try {
			URI rawURI = new URI(hRequest.getRequestURL().toString());
			URI redirectURI = new URI("https", rawURI.getUserInfo(), rawURI.getHost(), port, rawURI.getPath(), hRequest.getQueryString(), rawURI.getFragment());
			hResponse.sendRedirect(redirectURI.toString());
		} catch (URISyntaxException e) {
			log.warn("Had an error rewritting the url {}", hRequest.getRequestURL().toString());
		}
	}

	@Override
	public void destroy() {}
}
