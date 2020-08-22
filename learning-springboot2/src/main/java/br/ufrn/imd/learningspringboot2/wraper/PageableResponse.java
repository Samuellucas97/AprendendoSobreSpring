package br.ufrn.imd.learningspringboot2.wraper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
public class PageableResponse<T> extends PageImpl<T> {
    private Boolean first;
    private Boolean last;
    private int totalPages;
    private int numberOfElements;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageableResponse(@JsonProperty("content") List<T> content,
                            @JsonProperty("first") Boolean first,
                            @JsonProperty("last") Boolean last,
                            @JsonProperty("number") int number,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalElements") int totalElements,
                            @JsonProperty("totalPages") int totalPages,
                            @JsonProperty("numberOfElements") int numberOfElements,
                            @JsonProperty("pageable") JsonNode pageable,
                            @JsonProperty("sort") JsonNode sort) {
        super(content, PageRequest.of(number,size), totalElements);
        this.first = first;
        this.last = last;
        this.totalPages = totalPages;
        this.numberOfElements = numberOfElements;
    }
}