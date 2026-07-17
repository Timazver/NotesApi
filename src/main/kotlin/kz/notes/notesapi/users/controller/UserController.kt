package kz.notes.notesapi.users.controller

import kz.notes.notesapi.common.BaseResponse
import kz.notes.notesapi.users.dto.UpdateUserDto
import kz.notes.notesapi.users.dto.UserResponseDto
import kz.notes.notesapi.users.dto.toResponseDto
import kz.notes.notesapi.users.service.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    val service: UserService,
) {
    @GetMapping("/me")
    fun getUserInfo(
        @AuthenticationPrincipal email: String,
    ): BaseResponse<UserResponseDto> {
        val user = service.getUserInfo(email)
        return BaseResponse.success(user.toResponseDto())
    }

    @PutMapping("/me")
    fun updateUser(
        @RequestBody payload: UpdateUserDto,
        @AuthenticationPrincipal email: String,
    ): BaseResponse<Nothing> {
        service.updateUser(email, payload.firstName, payload.lastName)
        return BaseResponse.success(data = null)
    }

    @DeleteMapping("/me")
    fun deleteUser(
        @AuthenticationPrincipal email: String,
    ): BaseResponse<Nothing> {
        service.deleteUser(email)
        return BaseResponse.success(data = null)
    }
}
