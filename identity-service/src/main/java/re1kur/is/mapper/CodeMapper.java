package re1kur.is.mapper;

import re1kur.is.entity.Code;

public interface CodeMapper {
    Code write(String id, String value, Integer cacheTtl);
}
