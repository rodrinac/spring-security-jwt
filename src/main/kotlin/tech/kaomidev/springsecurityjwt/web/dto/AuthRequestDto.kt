package tech.kaomidev.springsecurityjwt.web.dto

import javax.validation.constraints.NotBlank

class AuthRequestDto {
    @NotBlank
    lateinit var username: String
    @NotBlank
    lateinit var password: String
}