package de.frittenburger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
	private LoginAuthenticationSuccessHandler authenticationSuccessHandler;
	
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/fonts/**", "/registration").permitAll()
                .antMatchers("/","/play/**", "/registration").permitAll()
                .antMatchers("/api/v1/datasets", "/api/v1/dataset/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .successHandler(authenticationSuccessHandler)
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll()
                .and()
            .rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400);

        http.csrf().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

    }


   
}