package com.arimar.gwent.contracts.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Schema(description = "Username of the user", example = "JohnDoe123")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Schema(description = "User password (must be at least 8 characters)", example = "P@ssw0rd123")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "User email address", example = "johndoe@example.com")
    private String email;

    @NotBlank(message = "Game ID is required")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,15}$", message = "Game ID must be 3-15 characters, only letters, numbers, _ or -")
    @Schema(description = "Game ID associated with the user", example = "gamePlayer_99")
    private String gameId;

    @NotBlank(message = "Tag is required")
    @Pattern(regexp = "^\\d{4}$", message = "Tag must be exactly 4 digits")
    @Schema(description = "Tag for unique identification (4-digit number)", example = "1234")
    private String tag;

}
