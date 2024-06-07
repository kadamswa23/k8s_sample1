package com.example.springjpa.security;

import com.example.springjpa.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    private static Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.timeout}")
    private long jwtTimeout;

    @Autowired
    UserRepository userRepository;

    public String generateToken(String userName){
        Map<String, String> claims = new HashMap();
        claims.put("Subject", userName);
        return Jwts.builder()
                .setClaims(claims)
                .setAudience(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtTimeout))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Date extractExpiration(String token) {
        Date expiration = extractAllClaims(token).get("exp", Date.class);
        return expiration;
    }
    public String extractUserName(String token){
        String userName = extractAllClaims(token).get("Subject").toString();
        return userName;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        String userName = extractUserName(token);
        return !isTokenExpired(token) && (userName.equals(userDetails.getUsername())) ;
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
