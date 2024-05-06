package com.music.album;

import com.music.album.command.CreateAlbumCommand;
import com.music.album.command.DeleteAlbumCommand;
import com.music.album.command.UpdateAlbumCommand;
import com.music.album.event.AlbumCreatedEvent;
import com.music.album.event.AlbumDeletedEvent;
import com.music.album.event.AlbumUpdatedEvent;
import com.music.commons.NotFoundException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;

@Service
public class AlbumCommandService {

    private final AlbumRepository albumRepository;
    private final AlbumEventProducer albumEventProducer;

    public AlbumCommandService(
            AlbumRepository albumRepository,
            AlbumEventProducer albumEventProducer
    ) {
        this.albumRepository = albumRepository;
        this.albumEventProducer = albumEventProducer;
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

        albumEventProducer.produce(document.getId(), new AlbumCreatedEvent(
                document.getId(),
                document.getTitle(),
                document.getArtist(),
                document.getReleaseYear(),
                URI.create(document.getCoverUrl()),
                document.getCreateTime(),
                document.getUpdateTime()
        ));
    }

    public void update(UpdateAlbumCommand command) {
        var document = albumRepository.findById(command.id()).orElseThrow(NotFoundException::new);
        document.setArtist(command.artist());
        document.setTitle(command.title());
        document.setCoverUrl(command.coverUrl().toString());
        document.setReleaseYear(command.releaseYear());
        document.setUpdateTime(LocalDateTime.now());
        this.albumRepository.save(document);

        albumEventProducer.produce(document.getId(), new AlbumUpdatedEvent(
                document.getId(),
                document.getTitle(),
                document.getArtist(),
                document.getReleaseYear(),
                URI.create(document.getCoverUrl()),
                document.getCreateTime(),
                document.getUpdateTime()
        ));
    }

    public void delete(DeleteAlbumCommand command) {
        albumRepository.deleteById(command.id());

        albumEventProducer.produce(command.id(), new AlbumDeletedEvent(
                command.id(),
                LocalDateTime.now()
        ));
    }
}
