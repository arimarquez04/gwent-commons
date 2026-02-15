package com.arimar.gwent.security.actor;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class Actor {
    private UUID userId;
    private String gameId;
    private String username;
    private String tag;

}
