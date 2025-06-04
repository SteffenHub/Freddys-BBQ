package com.freddys_bbq_mail;

import com.freddys_bbq_mail.model.EmailDetails;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling email sending requests.
 * Exposes endpoints for sending simple emails and emails with attachments.
 */
@RestController
@OpenAPIDefinition(
        info = @Info(
                title = "Freddy's BBQ Email Service API",
                description = "API for sending emails, both simple text emails and emails with attachments."
        ),
        servers = {
                @Server(url = "http://localhost:8010", description = "Local Development Server")
        }
)
@Tag(name = "Email Operations", description = "Endpoints for managing and sending emails")
public class EmailController {

    private final EmailService emailService;

    /**
     * Constructor for dependency injection of {@link EmailService}.
     *
     * @param emailService The email service to be injected.
     */
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Endpoint to send a simple email.
     * Accepts POST requests to "/sendMail".
     * The request body should be a JSON object matching {@link EmailDetails}.
     *
     * @param details Email details deserialized from the request body.
     * @return A status message from the {@link EmailService}.
     */
    @Operation(
            summary = "Send a simple email",
            description = "Takes email details (recipient, body, subject) and sends a simple text email. The 'attachment' field in the request body can be omitted or null for this endpoint."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email send status (success or operational error)",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string"),
                            examples = {
                                    @ExampleObject(name = "Success", value = "Mail Sent Successfully..."),
                                    @ExampleObject(name = "Error: No Sender", value = "Error while Sending Mail: No sender E-Mail configured"),
                                    @ExampleObject(name = "Error: Generic", value = "Error while Sending Mail")
                            })),
            @ApiResponse(responseCode = "400", description = "Invalid request payload (e.g., missing required fields)",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Bad Request: recipient is missing")))
    })
    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailDetails details) {
        return emailService.sendSimpleMail(details);
    }

    /**
     * Endpoint to send an email with an attachment.
     * Accepts POST requests to "/sendMailWithAttachment".
     * The request body should be a JSON object matching {@link EmailDetails},
     * where the 'attachment' field contains the path to the file.
     *
     * @param details Email details including attachment path, deserialized from the request body.
     * @return A status message from the {@link EmailService}.
     */
    @Operation(
            summary = "Send an email with an attachment",
            description = "Takes email details (recipient, body, subject) and the path to an attachment file, then sends the email. The 'attachment' field in the request body is required for this endpoint."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email send status (success or operational error)",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string"),
                            examples = {
                                    @ExampleObject(name = "Success", value = "Mail sent Successfully"),
                                    @ExampleObject(name = "Error: No Sender", value = "Error while Sending Mail: No sender E-Mail configured"),
                                    @ExampleObject(name = "Error: No Attachment", value = "Error while sending mail!!! No attachment provided."),
                                    @ExampleObject(name = "Error: Attachment Not Found", value = "Error while sending mail!!! Attachment file not found at: /path/to/file.pdf"),
                                    @ExampleObject(name = "Error: Messaging", value = "Error while sending mail!!! Specific messaging error details.")
                            })),
            @ApiResponse(responseCode = "400", description = "Invalid request payload (e.g., missing attachment path or required fields)",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Bad Request: attachment path is missing")))
    })
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(@RequestBody EmailDetails details) {
        return emailService.sendMailWithAttachment(details);
    }
}