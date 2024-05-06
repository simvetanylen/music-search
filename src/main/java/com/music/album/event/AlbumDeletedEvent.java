package com.music.album.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlbumDeletedEvent(
        UUID id,
        LocalDateTime deleteTime
) {
}
