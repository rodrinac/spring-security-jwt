package tech.kaomidev.springsecurityjwt.web.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.kaomidev.springsecurityjwt.service.UserDetailsServiceImpl
import tech.kaomidev.springsecurityjwt.utils.generateJwtToken
import tech.kaomidev.springsecurityjwt.web.dto.AuthRequestDto
import tech.kaomidev.springsecurityjwt.web.dto.AuthResponseDto
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    private lateinit var authManager: AuthenticationManager

    @Autowired
    private lateinit var userDetailsService: UserDetailsServiceImpl

    @PostMapping("/login")
    fun login(@Valid @RequestBody authRequest: AuthRequestDto): AuthResponseDto {
        return try {
            val authToken = UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
            authManager.authenticate(authToken)

            val userDetails = userDetailsService.loadUserByUsername(authRequest.username)

            AuthResponseDto().apply {
                token = generateJwtToken(userDetails)
            }
        } catch (ex: BadCredentialsException) {
            AuthResponseDto().apply {
                message = "Invalid username or password"
            }
        }
    }
}