package tech.kaomidev.springsecurityjwt.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

const val JWT_EXPIRATION_TIME = 1000 * 60 * 60 * 10

const val JWT_SECRET_KEY = "@m4nd@"

fun generateJwtToken(userDetails: UserDetails): String {
    return createJwtToken(mutableMapOf(), userDetails.username)
}

fun extractJwtUsername(token: String): String? {
    return extractJwtClaim(token, Claims::getSubject)
}

fun validateJwtToken(token: String, userDetails: UserDetails): Boolean {
    val username = extractJwtUsername(token)

    return username == userDetails.username && !isJwtTokenExpired(token)
}

fun extractJwtExpiration(token: String): Date {
    return extractJwtClaim(token, Claims::getExpiration)
}

fun isJwtTokenExpired(token: String): Boolean {
    return extractJwtExpiration(token).before(Date())
}

private fun createJwtToken(claims: MutableMap<String, Any>, subject: String): String {
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() * JWT_EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
            .compact()
}

private fun extractJwtClaims(token: String): Claims {
    return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).body
}

private fun <T> extractJwtClaim(token: String, resolver: (Claims) -> T): T {
    return resolver(extractJwtClaims(token))
}