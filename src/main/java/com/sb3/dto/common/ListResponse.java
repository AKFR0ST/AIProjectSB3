package com.sb3.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListResponse<T> {
    private List<T> items;
    private long total;
    private int page;
    private int size;
    private int totalPages;
    private boolean first;
    private boolean last;

    public static <T> ListResponse<T> fromPage(Page<T> page) {
        return ListResponse.<T>builder()
                .items(page.getContent())
                .total(page.getTotalElements())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public static <T> ListResponse<T> fromList(List<T> items, long total) {
        return ListResponse.<T>builder()
                .items(items)
                .total(total)
                .page(0)
                .size(items.size())
                .totalPages(1)
                .first(true)
                .last(true)
                .build();
    }
}
