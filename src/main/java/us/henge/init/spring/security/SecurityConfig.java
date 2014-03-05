package us.henge.init.spring.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import us.henge.utility.security.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	   http
	        .authorizeRequests() 
	        	.antMatchers("/favicon.ico", "/static/**", "/vendor/**").permitAll()
	        	.antMatchers("/login", "/register","/tracking").permitAll()
	        	.antMatchers("/blog/new", "/photos/new", "/blog/**/edit", "/photos/**/edit").authenticated()
	        	.antMatchers(HttpMethod.POST, "/blog", "/blog/**").authenticated()
	        	
	        	//Catch alls, GETs are allowed, POSTs are denied
	        	.antMatchers(HttpMethod.POST, "/**").authenticated()
	            .anyRequest().permitAll()
	            .and()
	        .formLogin()
	        	.loginPage("/login")
	        	.failureUrl("/login/error")
	        	.successHandler(new LoginSuccessHandler())
	        	.and()
	        .sessionManagement()
	        	.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
	        	.and();
	}
	
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
        	.jdbcAuthentication()
        		.dataSource(dataSource)
        		.passwordEncoder(bCryptPasswordEncoder);
    }
}