package kz.notes.notesapi.notes
import kz.notes.notesapi.common.BaseResponse
import kz.notes.notesapi.notes.dto.*
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notes")
class NoteController(val service: NoteService) {
    @GetMapping
    fun getAllNotes():BaseResponse<List<NoteResponseDto>>{
        return BaseResponse.success(service.getNotes().map{ it -> it.toResponseDto()})
    }

    @GetMapping("/{id}")
    fun getNote(@PathVariable id: Long): BaseResponse<NoteResponseDto> {
        val note = service.getNote(id)
        return BaseResponse.success(note.toResponseDto())
    }

    @PostMapping
    fun addNote(@RequestBody  payload: CreateNoteDto): BaseResponse<Nothing> {
        service.addNote(payload.title, payload.content)
        return BaseResponse.success(data=null,status = 201)
    }

    @PutMapping("/{id}")
    fun updateNote(@PathVariable id: Long, @RequestBody  payload: UpdateNoteDto):BaseResponse<Nothing> {
        service.updateNote(id, payload.title, payload.content)
        return  BaseResponse.success(data=null)
    }

    @DeleteMapping("/{id}")
    fun deleteNote(@PathVariable id: Long):BaseResponse<Nothing> {
        service.deleteNote(id)
        return BaseResponse.success(data=null)
    }
}