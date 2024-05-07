package com.music.album;

import com.music.album.command.CreateAlbumCommand;
import com.music.album.command.UpdateAlbumCommand;
import com.music.album.event.AlbumCreatedEvent;
import com.music.album.event.AlbumUpdatedEvent;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.util.Pair;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(indexName = "albums")
@Setting(settingPath = "/analyzer/custom-analyzer.json")
@Data
public class AlbumDocument {
    @Id
    private UUID id;
    @Field(type = FieldType.Text, analyzer = "custom_analyzer")
    private String title;
    @Field(type = FieldType.Text, analyzer = "custom_analyzer")
    private String artist;
    @Field(type = FieldType.Integer)
    private Integer releaseYear;
    @Field(type = FieldType.Text)
    private String coverUrl;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime createTime;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime updateTime;

    public static Pair<AlbumDocument, AlbumCreatedEvent> create(CreateAlbumCommand command) {
        var document = new AlbumDocument();
        document.setId(command.id());
        document.setArtist(command.artist());
        document.setTitle(command.title());
        document.setCoverUrl(command.coverUrl().toString());
        document.setReleaseYear(command.releaseYear());
        document.setCreateTime(LocalDateTime.now());
        document.setUpdateTime(LocalDateTime.now());

        var event = new AlbumCreatedEvent(
                document.getId(),
                document.getTitle(),
                document.getArtist(),
                document.getReleaseYear(),
                URI.create(document.getCoverUrl()),
                document.getCreateTime(),
                document.getUpdateTime()
        );

        return Pair.of(document, event);
    }

    public AlbumUpdatedEvent update(UpdateAlbumCommand command) {
        this.setArtist(command.artist());
        this.setTitle(command.title());
        this.setCoverUrl(command.coverUrl().toString());
        this.setReleaseYear(command.releaseYear());
        this.setUpdateTime(LocalDateTime.now());

        return new AlbumUpdatedEvent(
                this.getId(),
                this.getTitle(),
                this.getArtist(),
                this.getReleaseYear(),
                URI.create(this.getCoverUrl()),
                this.getCreateTime(),
                this.getUpdateTime()
        );
    }
}
