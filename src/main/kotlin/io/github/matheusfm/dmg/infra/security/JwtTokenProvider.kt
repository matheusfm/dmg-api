package io.github.matheusfm.dmg.infra.security

import io.github.matheusfm.dmg.domain.exception.InvalidJWTToken
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider {
    private val secretKey: String
    private val expirationMillis: Long
    private val userAuthService: UserAuthService

    constructor(
        userAuthService: UserAuthService,
        @Value("\${security.jwt.token.secret-key}") secretKey: String,
        @Value("\${security.jwt.token.expire-length}") expirationMillis: Long
    ) {
        this.userAuthService = userAuthService
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
        this.expirationMillis = expirationMillis
    }

    fun createToken(authentication: Authentication): String {
        val claims = Jwts.claims().setSubject(authentication.name)
        claims["auth"] = authentication.authorities
        val now = LocalDateTime.now()
        val expiration = now.plus(expirationMillis, ChronoUnit.MILLIS)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
            .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS256, "c2VjcmV0LWtleQ==")
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = userAuthService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUsername(token: String) = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject!!

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return true
        } catch (e: Exception) {
            throw InvalidJWTToken()
        }
    }

}