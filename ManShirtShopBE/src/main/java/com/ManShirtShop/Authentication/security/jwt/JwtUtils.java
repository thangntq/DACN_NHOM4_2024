package com.ManShirtShop.Authentication.security.jwt;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.*;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  private String jwtSecret = "JwtSecretKey";

  private int jwtExpirationMs = 3 * 60 * 60 * 1000;

  public String generateJwtToken(Authentication authentication) {
    Long now = System.currentTimeMillis();

    return Jwts.builder()
        .setSubject((authentication.getName()))
        .claim("authorities", authentication.getAuthorities().stream()
           .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now  + jwtExpirationMs)) 
        .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
        .compact();
  }
  public Claims validateAccessToken(String token) {
    try {
    	Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
    	return claims;
    } catch (ExpiredJwtException ex) {
      logger.error("JWT expired");
    } catch (IllegalArgumentException ex) {
      logger.error("Token is null, empty or only whitespace");
    } catch (MalformedJwtException ex) {
      logger.error("JWT is invalid");
    } catch (UnsupportedJwtException ex) {
      logger.error("JWT is not supported");
    } catch (SignatureException ex) {
      logger.error("Signature validation failed");
    }
    return null;
  }
  public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

	}

}
