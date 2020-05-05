package io.github.gogotea55t.jiriki;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class AuthService {
  public String getUserSubjectFromToken() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
    DecodedJWT decodedJwt = JWT.decode(details.getTokenValue());
    return decodedJwt.getSubject();
  }
}
