package tech.kaomidev.springsecurityjwt.web.filters

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import tech.kaomidev.springsecurityjwt.service.UserDetailsServiceImpl
import tech.kaomidev.springsecurityjwt.utils.extractJwtUsername
import tech.kaomidev.springsecurityjwt.utils.validateJwtToken
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var userDetailsService: UserDetailsServiceImpl

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val authHeader = req.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.doFilter(req, res)
        }

        val jwtToken = authHeader.substring(7)
        val username = extractJwtUsername(jwtToken)
        val securityContext = SecurityContextHolder.getContext()

        if (username != null && securityContext.authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            if (validateJwtToken(jwtToken, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(req)

                securityContext.authentication = authToken
            }
        }

        chain.doFilter(req, res)
    }

}