package tech.kaomidev.springsecurityjwt

import com.fasterxml.jackson.core.type.TypeReference
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tech.kaomidev.springsecurityjwt.web.dto.AuthRequestDto
import tech.kaomidev.springsecurityjwt.web.dto.AuthResponseDto


@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityJwtApplicationTests {

	@Autowired
	private lateinit var mvc: MockMvc

	@Autowired
	private lateinit var jacksonConverter: MappingJackson2HttpMessageConverter

	@Test
	fun `Should not allow access to unauthenticated users`() {
		mvc.perform(MockMvcRequestBuilders.get("/users/me")).andExpect(status().isForbidden)
	}

	@Test
	fun `Should authenticate and allow user access`() {

		val request = AuthRequestDto().apply {
			username = "user"
			password = "123"
		}

		val result = mvc.perform(MockMvcRequestBuilders.post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jacksonConverter.objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk)
				.andReturn()

		val resultContent: String = result.response.contentAsString

		var response: AuthResponseDto = jacksonConverter.objectMapper
				.readValue(resultContent, AuthResponseDto::class.java)

		mvc.perform(MockMvcRequestBuilders.get("/users/me")
				.header("Authorization", "Bearer ${response.token}"))
				.andExpect(status().isOk)
	}
}
