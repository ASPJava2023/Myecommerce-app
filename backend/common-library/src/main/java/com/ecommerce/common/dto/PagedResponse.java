package com.ecommerce.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic paginated response wrapper
 * Used for list endpoints with pagination support
 *
 * @param <T> Type of content in the page
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    /**
     * List of items in current page
     */
    private List<T> content;

    /**
     * Current page number (0-indexed)
     */
    private int pageNumber;

    /**
     * Number of items per page
     */
    private int pageSize;

    /**
     * Total number of elements across all pages
     */
    private long totalElements;

    /**
     * Total number of pages
     */
    private int totalPages;

    /**
     * Whether this is the last page
     */
    private boolean last;

    /**
     * Whether this is the first page
     */
    private boolean first;

    /**
     * Whether the page has content
     */
    private boolean empty;

    /**
     * Number of elements in current page
     */
    private int numberOfElements;

    /**
     * Create a paged response from Spring Data Page
     */
    public static <T> PagedResponse<T> of(org.springframework.data.domain.Page<T> page) {
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .empty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .build();
    }

    /**
     * Create a paged response with custom content
     */
    public static <T, R> PagedResponse<R> of(org.springframework.data.domain.Page<T> page, List<R> content) {
        return PagedResponse.<R>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .empty(page.isEmpty())
                .numberOfElements(content.size())
                .build();
    }
}
