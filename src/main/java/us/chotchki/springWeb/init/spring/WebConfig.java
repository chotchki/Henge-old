package us.chotchki.springWeb.init.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@Import({ ThymeleafConfig.class })
@ComponentScan("us.chotchki.springWeb.web")
public class WebConfig extends WebMvcConfigurerAdapter {
	// Maps resources path to webapp/resources
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		registry.addResourceHandler("/vendor/**").addResourceLocations("/vendor/").setCachePeriod(60 * 60 * 24 *180);
		registry.addResourceHandler("/favicon.ico").addResourceLocations("/static/favicon.ico");
	}
}
