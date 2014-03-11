package us.henge.init.spring;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.CompositeUriComponentsContributor;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@Import({ ThymeleafConfig.class })
@ComponentScan("us.henge.web")
public class WebConfig extends WebMvcConfigurationSupport {
	// Maps resources path to webapp/resources
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/css/**").addResourceLocations("/static/css/");
		registry.addResourceHandler("/static/img/**").addResourceLocations("/static/img/").setCachePeriod(60 * 60 * 24 *180);
		registry.addResourceHandler("/static/js/**").addResourceLocations("/static/js/");
		registry.addResourceHandler("/favicon.ico").addResourceLocations("/static/favicon.ico");
		registry.addResourceHandler("/robots.txt").addResourceLocations("/static/robots.txt");
		registry.addResourceHandler("/vendor/**").addResourceLocations("/vendor/").setCachePeriod(60 * 60 * 24 *180);
	}
	
    @Override
	public void addFormatters(FormatterRegistry registry) {
    	super.addFormatters(registry);
        JodaTimeFormatterRegistrar j = new JodaTimeFormatterRegistrar();
        j.setUseIsoFormat(true);
        j.registerFormatters(registry);
    }
    
    @Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    	super.addDefaultHttpMessageConverters(converters);
    	BufferedImageHttpMessageConverter bimc = new BufferedImageHttpMessageConverter();
		bimc.setDefaultContentType(MediaType.IMAGE_JPEG);
		converters.add(bimc);
	}
    
    @Override
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		return super.requestMappingHandlerMapping();
	}

    @Override
	@Bean
	public ContentNegotiationManager mvcContentNegotiationManager() {
		return super.mvcContentNegotiationManager();
	}
    
	@Bean
	public HandlerMapping viewControllerHandlerMapping() {
		return super.viewControllerHandlerMapping();
	}
	
	@Bean
	public BeanNameUrlHandlerMapping beanNameHandlerMapping() {
		return super.beanNameHandlerMapping();
	}
	
	@Bean
	public HandlerMapping resourceHandlerMapping() {
		return super.resourceHandlerMapping();
	}
	
	@Bean
	public HandlerMapping defaultServletHandlerMapping() {
		return super.defaultServletHandlerMapping();
	}
	
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		return super.requestMappingHandlerAdapter();
	}
	
	@Bean
	public FormattingConversionService mvcConversionService() {
		return super.mvcConversionService();
	}
	
	@Bean
	public Validator mvcValidator() {
		return super.mvcValidator();
	}
	
	@Bean
	public CompositeUriComponentsContributor mvcUriComponentsContributor() {
		return super.mvcUriComponentsContributor();
	}
	
	@Bean
	public HttpRequestHandlerAdapter httpRequestHandlerAdapter() {
		return super.httpRequestHandlerAdapter();
	}
	
	@Bean
	public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter() {
		return super.simpleControllerHandlerAdapter();
	}
	
	@Bean
	public HandlerExceptionResolver handlerExceptionResolver() {
		return super.handlerExceptionResolver();
	}
}
