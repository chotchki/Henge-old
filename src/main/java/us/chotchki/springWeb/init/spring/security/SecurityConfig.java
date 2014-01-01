package us.chotchki.springWeb.init.spring.security;

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
	        	.antMatchers("/login", "/register").permitAll()
	        	.antMatchers(HttpMethod.POST, "/blog", "/blog/**").authenticated()
	        	
	        	//Catch alls, GETs are allowed, POSTs are denied
	        	.antMatchers(HttpMethod.POST, "/**").hasAnyRole("ROLE_ADMIN")
	            .anyRequest().permitAll()
	            .and()
	        .formLogin()
	        	.loginPage("/login")
	        	.and()
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	        	.and();
	}
	
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
        	.jdbcAuthentication()
        		.dataSource(dataSource)
        		.and()
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
            .passwordEncoder(bCryptPasswordEncoder);
    }
}