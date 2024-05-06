package com.music.album.populator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.album.AlbumCommandService;
import com.music.album.AlbumQueryService;
import com.music.album.command.CreateAlbumCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
public class AlbumPopulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumPopulator.class);
    private final AlbumCommandService albumCommandService;
    private final AlbumQueryService albumQueryService;
    private final Resource albumsSample;
    private final ObjectMapper jsonMapper;

    public AlbumPopulator(
            AlbumCommandService albumCommandService,
            AlbumQueryService albumQueryService,
            @Value("classpath:data/albums_sample.json")
            Resource albumsSample,
            ObjectMapper jsonMapper
    ) {
        this.albumCommandService = albumCommandService;
        this.albumQueryService = albumQueryService;
        this.albumsSample = albumsSample;
        this.jsonMapper = jsonMapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void populate() throws IOException {
        var count = albumQueryService.count();

        if (count > 0) {
            return;
        }

        LOGGER.info("Populating albums.");

        try (var stream = albumsSample.getInputStream()) {
            var values = jsonMapper.readValue(stream, SampleAlbum[].class);
            Arrays.stream(values).forEach(album -> {
                        albumCommandService.create(new CreateAlbumCommand(
                                album.id(),
                                album.title(),
                                album.artist(),
                                album.releaseYear(),
                                album.coverURL()
                        ));
                    }
            );

            LOGGER.info("Albums populated.");
        }
    }
}
