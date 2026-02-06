package com.arimar.gwent.security.actor;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class Actor {
    private UUID playerId;
    private String username;
    private String gameId;
    private String tag;
}
