package com.music.album;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Document(indexName = "albums")
@Data
public class AlbumDocument {
    @Id
    private UUID id;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String artist;
    @Field(type = FieldType.Integer)
    private Integer releaseYear;
    @Field(type = FieldType.Text)
    private String coverUrl;
}
