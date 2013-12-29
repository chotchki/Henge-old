package us.chotchki.springWeb.init;

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

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	   http
	        .authorizeRequests() //Simple Security, any POST must be authenticated
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
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
            .passwordEncoder(new BCryptPasswordEncoder(16));
    }
}