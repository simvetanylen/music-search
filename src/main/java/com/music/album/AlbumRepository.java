package com.music.album;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AlbumRepository extends CrudRepository<AlbumDocument, UUID> {
}
