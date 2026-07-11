package kz.notes.notesapi

import kz.notes.notesapi.auth.jwt.JwtConfigProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtConfigProperties::class)
class NotesApiApplication

fun main(args: Array<String>) {
    runApplication<NotesApiApplication>(*args)
}
