package com.music.album;

import com.music.album.command.CreateAlbumCommand;
import com.music.album.command.DeleteAlbumCommand;
import com.music.album.command.UpdateAlbumCommand;
import com.music.album.event.AlbumDeletedEvent;
import com.music.commons.NotFoundException;
import org.springframework.stereotype.Service;

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
        var result = AlbumDocument.create(command);
        this.albumRepository.save(result.getFirst());
        albumEventProducer.produce(result.getSecond().id(), result.getSecond());
    }

    public void update(UpdateAlbumCommand command) {
        var document = albumRepository.findById(command.id()).orElseThrow(NotFoundException::new);
        var event = document.update(command);
        this.albumRepository.save(document);
        albumEventProducer.produce(event.id(), event);
    }

    public void delete(DeleteAlbumCommand command) {
        albumRepository.deleteById(command.id());

        albumEventProducer.produce(command.id(), new AlbumDeletedEvent(
                command.id(),
                LocalDateTime.now()
        ));
    }
}
