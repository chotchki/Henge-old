package us.chotchki.springWeb.init.jetty;

import javax.servlet.Filter;

import org.eclipse.jetty.servlets.GzipFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import us.chotchki.springWeb.init.spring.RootConfig;
import us.chotchki.springWeb.init.spring.WebConfig;
import us.chotchki.springWeb.init.spring.security.SecurityConfig;

public class WebAppInit extends AbstractAnnotationConfigDispatcherServletInitializer  {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{RootConfig.class, SecurityConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{WebConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

	@Override
	protected Filter[] getServletFilters() {
		return new Filter[]{new GzipFilter()};
	}
}
