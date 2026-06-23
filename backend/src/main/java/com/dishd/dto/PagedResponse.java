package com.dishd.dto;

import java.util.List;
import org.springframework.data.domain.Page;

/** Envelope de paginacao estavel para respostas em lista (ex.: feed). */
public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last) {

    public static <T> PagedResponse<T> from(Page<T> p) {
        return new PagedResponse<>(
                p.getContent(),
                p.getNumber(),
                p.getSize(),
                p.getTotalElements(),
                p.getTotalPages(),
                p.isLast());
    }
}
