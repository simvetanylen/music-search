package com.music.album.event;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

public record AlbumUpdatedEvent(
        UUID id,
        String title,
        String artist,
        Integer releaseYear,
        URI coverUrl,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
}
