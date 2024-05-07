package com.music.album;

import com.music.album.command.CreateAlbumCommand;
import com.music.album.command.DeleteAlbumCommand;
import com.music.album.command.UpdateAlbumCommand;
import com.music.album.dto.CreateAlbumCommandDto;
import com.music.album.dto.UpdateAlbumCommandDto;
import com.music.album.query.AlbumQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("albums")
public class AlbumWebservices {

    private final AlbumQueryService albumQueryService;
    private final AlbumCommandService albumCommandService;

    public AlbumWebservices(
            AlbumQueryService albumQueryService,
            AlbumCommandService albumCommandService
    ) {
        this.albumQueryService = albumQueryService;
        this.albumCommandService = albumCommandService;
    }

    @GetMapping("{id}")
    public AlbumDocument get(@PathVariable("id") UUID id) {
        return albumQueryService.findById(id);
    }

    @GetMapping("years")
    public Map<Long, Long> getYearCount() {
        return albumQueryService.countByYear();
    }

    @PostMapping("_search")
    public Page<AlbumDocument> search(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "page-size", defaultValue = "20") int pageSize,
            @RequestBody AlbumQuery albumQuery
    ) {
        return albumQueryService.findAlbums(PageRequest.of(pageNumber, pageSize), albumQuery);
    }

    @PostMapping
    public void create(
            @Validated @RequestBody CreateAlbumCommandDto dto
    ) {
        albumCommandService.create(map(dto));
    }

    @PutMapping("{id}")
    public void update(
            @PathVariable("id") UUID id,
            @Validated @RequestBody UpdateAlbumCommandDto dto
    ) {
        albumCommandService.update(map(id, dto));
    }

    @DeleteMapping("{id}")
    public void delete(
            @PathVariable("id") UUID id
    ) {
        albumCommandService.delete(new DeleteAlbumCommand(id));
    }

    private static CreateAlbumCommand map(CreateAlbumCommandDto dto) {
        return new CreateAlbumCommand(
                UUID.randomUUID(),
                dto.title(),
                dto.artist(),
                dto.releaseYear(),
                dto.coverUrl()
        );
    }

    private static UpdateAlbumCommand map(UUID id, UpdateAlbumCommandDto dto) {
        return new UpdateAlbumCommand(
                id,
                dto.title(),
                dto.artist(),
                dto.releaseYear(),
                dto.coverUrl()
        );
    }
}
