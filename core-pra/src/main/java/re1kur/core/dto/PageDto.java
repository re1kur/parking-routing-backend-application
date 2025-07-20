package re1kur.core.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PageDto<T>(
        Integer pageNumber,
        Integer totalPages,
        Integer pageSize,
        List<T> content

) {
}
