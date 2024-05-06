package com.music.album.command;

import java.net.URI;
import java.util.UUID;

public record CreateAlbumCommand(
        UUID id,
        String title,
        String artist,
        Integer releaseYear,
        URI coverUrl
) {
}
