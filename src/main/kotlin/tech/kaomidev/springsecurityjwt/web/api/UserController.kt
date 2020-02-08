package tech.kaomidev.springsecurityjwt.web.api

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@RestController
@RequestMapping("/users")
class UserController {
	
	@GetMapping("/me")
	fun me(principal: Principal) =  principal
}