package us.henge.utility.security;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private static final Logger log = LoggerFactory.getLogger(LoginSuccessHandler.class);
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		String referer = request.getHeader("referer");
		try {
			URI refURI = new URL(referer).toURI();
			session.setAttribute(ProtectedUrlFilter.SESSION_ATTR, refURI.getScheme() + "://" + refURI.getAuthority());
		} catch (URISyntaxException e) {
			log.error("Unable to parse the referer", e);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
