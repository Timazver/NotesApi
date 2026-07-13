package kz.notes.notesapi.users.controller

import jakarta.validation.Valid
import kz.notes.notesapi.common.BaseResponse
import kz.notes.notesapi.users.dto.CreateUserDto
import kz.notes.notesapi.users.dto.UpdateUserDto
import kz.notes.notesapi.users.dto.UserResponseDto
import kz.notes.notesapi.users.dto.toResponseDto
import kz.notes.notesapi.users.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(val service: UserService) {
    @GetMapping
    fun getAllUsers(): BaseResponse<List<UserResponseDto>> {
        return BaseResponse.success(service.getAllUsers().map { it -> it.toResponseDto() })
    }

    @GetMapping("/{id}")
    fun getUserInfo(@PathVariable id: Long): BaseResponse<UserResponseDto> {
        val user = service.getUserInfo(id)
        return BaseResponse.success(user.toResponseDto())
    }

    @PostMapping
    fun addUser(@Valid @RequestBody payload: CreateUserDto): BaseResponse<Nothing> {
        service.addUser(payload.firstName, payload.lastName)
        return BaseResponse.success(data = null, status = 201)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody payload: UpdateUserDto): BaseResponse<Nothing> {
        service.updateUser(id, payload.firstName, payload.lastName)
        return BaseResponse.success(data = null)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): BaseResponse<Nothing> {
        service.deleteUser(id)
        return BaseResponse.success(data = null)
    }
}