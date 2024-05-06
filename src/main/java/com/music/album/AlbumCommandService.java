package com.music.album;

import com.music.album.command.CreateAlbumCommand;
import com.music.album.command.DeleteAlbumCommand;
import com.music.album.command.UpdateAlbumCommand;
import com.music.commons.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        document.setCreateTime(LocalDateTime.now());
        document.setUpdateTime(LocalDateTime.now());
        this.albumRepository.save(document);
    }

    public void update(UpdateAlbumCommand command) {
        var document = albumRepository.findById(command.id()).orElseThrow(NotFoundException::new);
        document.setArtist(command.artist());
        document.setTitle(command.title());
        document.setCoverUrl(command.coverUrl().toString());
        document.setReleaseYear(command.releaseYear());
        document.setUpdateTime(LocalDateTime.now());
        this.albumRepository.save(document);
    }

    public void delete(DeleteAlbumCommand command) {
        albumRepository.deleteById(command.id());
    }
}
