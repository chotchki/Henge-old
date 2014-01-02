package us.chotchki.springWeb.init.spring;

import nz.net.ultraq.thymeleaf.LayoutDialect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import us.chotchki.springWeb.utility.TimeDialect;

@Configuration
public class ThymeleafConfig {
	private static final Logger log = LoggerFactory.getLogger(ThymeleafConfig.class);

	@Autowired
	private TimeDialect timeDialect;
	
	@Bean
	public ServletContextTemplateResolver templateResolver() {
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
		resolver.setPrefix("/thymeleaf/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		resolver.setOrder(1);
		
		String os = System.getProperty("os.name");
		if(os.startsWith("Windows")){
			log.debug("Disabling Thymeleaf Cache");
			resolver.setCacheable(false);
		} else {
			log.debug("Enabling Thymeleaf Cache");
			resolver.setCacheable(true);
		}
		
		
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		engine.addDialect(new LayoutDialect());
		engine.addDialect(new SpringSecurityDialect());
		engine.addDialect(timeDialect);
		return engine;
	}

	@Bean
	public ThymeleafViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		return resolver;
	}
}