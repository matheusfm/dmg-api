package io.github.matheusfm.dmg.infra.security

import io.github.matheusfm.dmg.domain.exception.InvalidJWTToken
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenFilter(private val jwtTokenProvider: JwtTokenProvider) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            jwtTokenProvider.resolveToken(request)?.takeIf(jwtTokenProvider::validateToken)?.let { token ->
                SecurityContextHolder.getContext().authentication = jwtTokenProvider.getAuthentication(token)
            }
        } catch (ex: InvalidJWTToken) {
            SecurityContextHolder.clearContext()
            response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.message)
            return
        }

        filterChain.doFilter(request, response)
    }

}