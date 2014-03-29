package us.henge.init.jetty;

import javax.servlet.Filter;

import org.eclipse.jetty.servlets.GzipFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import us.henge.init.spring.RootConfig;
import us.henge.init.spring.WebConfig;
import us.henge.init.spring.security.SecurityConfig;
import us.henge.utility.SslFilter;
import us.henge.utility.security.ProtectedUrlFilter;

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
		return new Filter[]{new GzipFilter(), new SslFilter(), new ProtectedUrlFilter()};
	}
	
	@Override
	protected WebApplicationContext createServletApplicationContext(){
		WebApplicationContext wac = super.createServletApplicationContext();
		return wac;
	}
}
