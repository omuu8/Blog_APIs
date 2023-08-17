package com.example.blog_app.demo_blogging.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenHelper {

    public static final long JWT_TOKEN_VALIDITY = 5*60*60;
    // The validity is specified according to the need
    //format is no of hrs *60*60*1000

    //	private String secret = "jwtTokenKey";
    private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
    //retrieve username from jwt token
    public String getUserNameFromToken(String token) {

        return getClaimFromToken(token,Claims :: getSubject);
    }
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {

        return getClaimFromToken(token, Claims :: getExpiration);
    }
    //retrieve claims from token

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver) {
        final Claims claims  = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieving any information using token we need secret key
    private Claims getAllClaimsFromToken(String token) {

        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    //Check if the token is expired
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //Generate Token for user
    public String generateToken(UserDetails userDetails) {

        Map<String,Object> claims = new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret) // Use HmacSHA256 here
                .compact();
    }
    //HS256 and HS512 are the types of algorithm to generate token

    //validate token
    public boolean validateToken(String token,UserDetails userDetails) {
        //First take username from token ok!
        final String userName = getUserNameFromToken(token);
        //validate with userdetails name and check if the token is expired
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}