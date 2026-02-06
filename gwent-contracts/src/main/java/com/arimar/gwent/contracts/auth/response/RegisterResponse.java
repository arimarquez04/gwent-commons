package com.arimar.gwent.contracts.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterResponse {
    @Schema(description = "Player ID, unique for each player", example = "1234")
    private Long playerId;

    @Schema(description = "Game ID associated with the user", example = "gamePlayer_99")
    private String gameId;

    @Schema(description = "Tag for unique identification (4-digit number)", example = "1234")
    private String tag;
}
