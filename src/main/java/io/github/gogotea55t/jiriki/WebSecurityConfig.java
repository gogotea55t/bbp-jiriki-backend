package io.github.gogotea55t.jiriki;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Value("${auth0.audience}")
  private String audience;
  
  @Value("${auth0.issuer}")
  private String issuer;
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
//              .antMatchers(HttpMethod.GET).authenticated()
//              .antMatchers(HttpMethod.POST).authenticated()
              .anyRequest().permitAll();

      JwtWebSecurityConfigurer
              .forRS256(audience, issuer)   //Request Headers Authorization tokenを検証
              .configure(http);
  }
}
