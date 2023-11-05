package com.shopping.productcatelogue.model;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class PagedList<T> extends PageImpl<T> {

    public static <T1, T2> PagedList<T2> getPagedList(Page<T1> page,
            Function<? super T1, ? extends T2> mapperFunction) {
        return new PagedList<>(
                page.getContent().stream().map(mapperFunction).collect(Collectors.toList()),
                PageRequest.of(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getPageable().getSort()),
                page.getTotalElements());
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PagedList(@JsonProperty("content") List<T> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") Long totalElements,
            @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("last") boolean last,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") boolean first,
            @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    public PagedList(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PagedList(List<T> content) {
        super(content);
    }
}
