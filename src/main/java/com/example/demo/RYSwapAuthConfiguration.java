package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

@Component
public class RYSwapAuthConfiguration extends WebSecurityConfigurerAdapter {
  @Autowired
  Filter ssoFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.antMatcher("/**").authorizeRequests().antMatchers("/", "/login**", "/webjars/**", "/error**").permitAll().anyRequest()
        .authenticated().and().exceptionHandling()
        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")).and().logout()
        .logoutSuccessUrl("/").permitAll().and().csrf()
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
        .addFilterBefore(ssoFilter, BasicAuthenticationFilter.class);
    // @formatter:on
  }

  @Configuration
  @EnableResourceServer
  protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
      // @formatter:off
      http.antMatcher("/me").authorizeRequests().anyRequest().authenticated();
      // @formatter:on
    }
  }
}
