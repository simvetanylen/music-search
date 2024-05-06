package com.music.album;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.LongTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.util.NamedValue;
import com.music.album.query.AlbumQuery;
import com.music.commons.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlbumQueryService {

    private final AlbumRepository albumRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public AlbumQueryService(
            AlbumRepository albumRepository,
            ElasticsearchOperations elasticsearchOperations
    ) {
        this.albumRepository = albumRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public AlbumDocument findById(UUID id) {
        return albumRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public Page<AlbumDocument> findAlbums(Pageable pageable, AlbumQuery albumQuery) {

        var boolQuery = QueryBuilders.bool()
                .must(
                        QueryBuilders.multiMatch()
                                .fields("title", "artist")
                                .query(albumQuery.keywords())
                                .build()
                                ._toQuery()
                )
                .filter(
                        QueryBuilders.term()
                                .field("releaseYear")
                                .value(albumQuery.year())
                                .build()
                                ._toQuery()
                )
                .build()
                ._toQuery();

        var query = NativeQuery.builder()
                .withQuery(boolQuery)
                .withPageable(pageable)
                .build();

        var searchResult = elasticsearchOperations.search(query, AlbumDocument.class);
        var page = SearchHitSupport.searchPageFor(searchResult, pageable);
        return (Page<AlbumDocument>) SearchHitSupport.unwrapSearchHits(page);
    }

    public Map<Long, Long> countByYear() {
        var releaseYearAggregation = new TermsAggregation.Builder()
                .field("releaseYear")
                .size(1000)
                .order(List.of(NamedValue.of("_key", SortOrder.Desc)))
                .build()
                ._toAggregation();

        var query = NativeQuery.builder()
                .withMaxResults(0) // No need to get albums.
                .withAggregation("ReleaseYearAggregation", releaseYearAggregation)
                .build();

        var searchResult = elasticsearchOperations.search(query, AlbumDocument.class);

        var aggregationResult = ((List<ElasticsearchAggregation>) searchResult
                .getAggregations()
                .aggregations())
                .get(0);

        var buckets = aggregationResult.aggregation()
                .getAggregate()
                .lterms()
                .buckets();

        return buckets.array()
                .stream()
                .collect(Collectors.toMap(LongTermsBucket::key, LongTermsBucket::docCount));
    }

    public Long count() {
        return albumRepository.count();
    }
}
