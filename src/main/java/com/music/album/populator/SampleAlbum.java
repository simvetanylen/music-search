package com.music.album.populator;

import java.net.URI;
import java.util.UUID;

public record SampleAlbum(
        UUID id,
        String title,
        String artist,
        Integer releaseYear,
        URI coverURL
) {}
