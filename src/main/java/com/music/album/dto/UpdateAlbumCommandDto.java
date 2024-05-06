package com.music.album.dto;

import java.net.URI;

public record UpdateAlbumCommandDto(
        String title,
        String artist,
        Integer releaseYear,
        URI coverUrl
) {
}
