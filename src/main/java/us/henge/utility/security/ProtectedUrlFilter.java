package us.henge.utility.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtectedUrlFilter implements Filter {
	private static final Logger log = LoggerFactory.getLogger(ProtectedUrlFilter.class);
	
	public static final String HANDLER = "/p";
	public static final String SESSION_ATTR = ProtectedUrlFilter.class.getName() + "_session";
	
	private static final String[] protectUrls = new String[]{
		"/photos/add/folder"
	};
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse hres = (HttpServletResponse) response;
		
		String referer = hreq.getHeader("referer");
		
		HttpSession session = hreq.getSession();
		String sessionRef = (String) (session != null ? session.getAttribute(SESSION_ATTR) : null);
		
		String attr = (String) hreq.getAttribute(ProtectedUrlFilter.class.getName());
		
		//If already processed allow through
		if(attr != null && attr.equals(ProtectedUrlFilter.class.getName())){
			chain.doFilter(request, response);
			return;
		}
		
		//If protected, deny access
		String uri = hreq.getRequestURI();
		for(String protectUrl: protectUrls){
			if(uri.startsWith(protectUrl)){
				hres.sendError(HttpStatus.FORBIDDEN_403);
				return;
			}
		}
		
		//Allow everything non handler related through
		if(!uri.startsWith(HANDLER)){
			chain.doFilter(request, response);
			return;
		}
		
		if(sessionRef == null || referer == null || !referer.startsWith(sessionRef)){
			hres.sendError(HttpStatus.BAD_REQUEST_400);
			return;
		}
		
		String encryptedPart = uri.replaceFirst(HANDLER + "/", "");
		String decryptedPart = null;
		try {
			decryptedPart = KeyCreation.decrypt(session, encryptedPart);
		} catch (Exception e) {
			log.error("Unable to decrypt the string", e);
			hres.sendError(HttpStatus.FORBIDDEN_403);
			return;
		}
		
		session.getServletContext().getRequestDispatcher(decryptedPart).forward(request, response);
	}

	@Override
	public void destroy() {}
}