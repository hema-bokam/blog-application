package com.example.blogapp.security;

import com.example.blogapp.exception.ApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currTime = new Date();
        Date expireDate = new Date(currTime.getTime() + jwtExpirationDate);

        String token = Jwts.builder().setSubject(username)
                        .setIssuedAt(currTime)
                        .setExpiration(expireDate)
                        .signWith(key())
                        .compact();              //compact method will club all these and returns token
        return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //gives username from jwt token
    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build().parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build().parse(token);
            return true;
        }catch(MalformedJwtException ex){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        }catch(ExpiredJwtException ex){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        }catch(UnsupportedJwtException ex){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        }catch(IllegalArgumentException ex){
            throw new ApiException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }
    }
}
