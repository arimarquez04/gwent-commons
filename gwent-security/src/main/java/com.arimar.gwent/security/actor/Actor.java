package com.arimar.gwent.security.actor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    private UUID userId;
    private String gameId;
    private String username;
    private String tag;

}
