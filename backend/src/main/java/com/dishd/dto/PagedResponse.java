package com.dishd.dto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/** Envelope de paginacao estavel para respostas em lista (ex.: feed). */
public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last) {

    private static final int MAX_SIZE = 100;

    /** Constroi um {@link Pageable} com page/size saneados (size 1..100, page >= 0). */
    public static Pageable pageable(int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), MAX_SIZE);
        int safePage = Math.max(page, 0);
        return PageRequest.of(safePage, safeSize);
    }

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
