package com.music.album;

import com.music.album.command.CreateAlbumCommand;
import org.springframework.stereotype.Service;

@Service
public class AlbumCommandService {

    private final AlbumRepository albumRepository;

    public AlbumCommandService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public void create(CreateAlbumCommand command) {
        var document = new AlbumDocument();
        document.setId(command.id());
        document.setArtist(command.artist());
        document.setTitle(command.title());
        document.setCoverUrl(command.coverUrl().toString());
        document.setReleaseYear(command.releaseYear());
        this.albumRepository.save(document);
    }
}
