package com.music.album.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.net.URI;

public record UpdateAlbumCommandDto(
        @Size(min = 3, max = 100)
        @NotNull
        String title,
        @Size(min = 3, max = 100)
        String artist,
        @Min(1900)
        Integer releaseYear,
        URI coverUrl
) {
}
