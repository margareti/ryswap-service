package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RYSwapConfiguration {


  @Autowired
  OAuth2ClientContext oauth2ClientContext;

  @Bean
  public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
    FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
    registration.setFilter(filter);
    registration.setOrder(-100);
    return registration;
  }

  @Bean
  @ConfigurationProperties("github")
  public ClientResources github() {
    return new ClientResources();
  }

  @Bean
  @ConfigurationProperties("facebook")
  public ClientResources facebook() {
    return new ClientResources();
  }

  @Bean
  public Filter ssoFilter() {
    CompositeFilter filter = new CompositeFilter();
    List<Filter> filters = new ArrayList<>();
    filters.add(ssoFilter(facebook(), "/login/facebook"));
    filters.add(ssoFilter(github(), "/login/github"));
    filter.setFilters(filters);
    return filter;
  }

  private Filter ssoFilter(ClientResources client, String path) {
    OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationFilter = new OAuth2ClientAuthenticationProcessingFilter(path);
    OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
    oAuth2ClientAuthenticationFilter.setRestTemplate(oAuth2RestTemplate);
    UserInfoTokenServices tokenServices = new UserInfoTokenServices(client.getResource().getUserInfoUri(),
        client.getClient().getClientId());
    tokenServices.setRestTemplate(oAuth2RestTemplate);
    oAuth2ClientAuthenticationFilter.setTokenServices(tokenServices);
    return oAuth2ClientAuthenticationFilter;
  }

}
