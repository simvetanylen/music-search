package com.music.album.dto;

import java.net.URI;

public record CreateAlbumCommandDto(
        String title,
        String artist,
        Integer releaseYear,
        URI coverUrl
) {
}
