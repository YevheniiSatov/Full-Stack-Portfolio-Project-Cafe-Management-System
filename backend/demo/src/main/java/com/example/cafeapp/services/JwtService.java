package com.example.cafeapp.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "qey0UfBcIij2DLQ1kuZbYKDD/NvLaiT80pfE+4I0Gswij2PCePfOYizspp0Tw7twM3LkA8JVXCat7L2ujX4Ogw==";

    /**
     * Extracts the username (subject) from the JWT token.
     * @param token The JWT token.
     * @return The username contained within the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     * @param token The JWT token.
     * @return The expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token using the provided resolver function.
     * @param token The JWT token.
     * @param claimsResolver A function to extract a specific claim from the token.
     * @param <T> The type of the claim being extracted.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     * @param token The JWT token.
     * @return The claims contained within the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the JWT token has expired.
     * @param token The JWT token.
     * @return True if the token has expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates the JWT token by checking its username and expiration.
     * @param token The JWT token.
     * @param userDetails The user details to validate the token against.
     * @return True if the token is valid, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
