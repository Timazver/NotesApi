package kz.notes.notesapi.admin.bootstrap

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
@Profile("bootstrap-admin")
class AdminBootstrapRunner(
    private val bootstrapService: AdminBootstrapService,
    private val environment: Environment,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val command =
            AdminBootstrapCommand(
                firstName = args.requiredOption(FIRST_NAME_OPTION),
                lastName = args.requiredOption(LAST_NAME_OPTION),
                email = args.requiredOption(EMAIL_OPTION),
                password = environment.requiredSecret(PASSWORD_ENVIRONMENT_VARIABLE),
            )

        when (bootstrapService.bootstrap(command)) {
            AdminBootstrapResult.CREATED -> logger.info("Administrator account created")
            AdminBootstrapResult.ALREADY_EXISTS -> logger.info("Administrator already exists; no changes made")
        }
    }

    private fun ApplicationArguments.requiredOption(name: String): String =
        getOptionValues(name)
            ?.singleOrNull()
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: throw IllegalArgumentException("Required option --$name is missing or specified more than once")

    private fun Environment.requiredSecret(name: String): String =
        getProperty(name)
            ?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("Required environment variable $name is missing")

    private companion object {
        val logger = LoggerFactory.getLogger(AdminBootstrapRunner::class.java)

        const val FIRST_NAME_OPTION = "first-name"
        const val LAST_NAME_OPTION = "last-name"
        const val EMAIL_OPTION = "email"
        const val PASSWORD_ENVIRONMENT_VARIABLE = "ADMIN_BOOTSTRAP_PASSWORD"
    }
}
