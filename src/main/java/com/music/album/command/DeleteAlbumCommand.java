package com.music.album.command;

import java.util.UUID;

public record DeleteAlbumCommand(
        UUID id
) {
}
