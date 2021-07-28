package br.com.ead.personsearch.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Log4j2
@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("username")
                .password("password")
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize
                .mvcMatchers("/ping").permitAll()
                .mvcMatchers("/health").permitAll()
                .mvcMatchers(HttpMethod.POST, "/persons").authenticated()
                .mvcMatchers(HttpMethod.GET, "/persons").authenticated()
                .mvcMatchers(HttpMethod.DELETE, "/persons/**").denyAll()
                .mvcMatchers("/**").authenticated()
        ).httpBasic();
    }
}