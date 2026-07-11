package kz.notes.notesapi.auth.controller

import jakarta.validation.Valid
import kz.notes.notesapi.auth.dto.AuthRequestDto
import kz.notes.notesapi.auth.dto.AuthResponseDto
import kz.notes.notesapi.auth.dto.RegisterRequestDto
import kz.notes.notesapi.auth.dto.toCommand
import kz.notes.notesapi.auth.jwt.JwtTokenService
import kz.notes.notesapi.auth.service.AuthService
import kz.notes.notesapi.common.BaseResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val jwtTokenUtil: JwtTokenService,
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun signIn(@Valid @RequestBody payload: AuthRequestDto): BaseResponse<AuthResponseDto> {
        val token = jwtTokenUtil.generateToken(payload.email)
        return BaseResponse.success(AuthResponseDto(token))
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody payload: RegisterRequestDto): BaseResponse<Nothing> {
        authService.registerUser(payload.toCommand())
        return BaseResponse.success(data = null, status = 201)
    }
}
