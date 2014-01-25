package us.henge.init.spring;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@Import({ ThymeleafConfig.class })
@ComponentScan("us.henge.web")
public class WebConfig extends WebMvcConfigurerAdapter {
	// Maps resources path to webapp/resources
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/css/**").addResourceLocations("/static/css/");
		registry.addResourceHandler("/static/img/**").addResourceLocations("/static/img/").setCachePeriod(60 * 60 * 24 *180);
		registry.addResourceHandler("/static/js/**").addResourceLocations("/static/js/");
		registry.addResourceHandler("/favicon.ico").addResourceLocations("/static/favicon.ico");
		registry.addResourceHandler("/vendor/**").addResourceLocations("/vendor/").setCachePeriod(60 * 60 * 24 *180);
	}
	
    @Override
	public void addFormatters(FormatterRegistry registry) {
        JodaTimeFormatterRegistrar j = new JodaTimeFormatterRegistrar();
        j.setUseIsoFormat(true);
        j.registerFormatters(registry);
    }
}
