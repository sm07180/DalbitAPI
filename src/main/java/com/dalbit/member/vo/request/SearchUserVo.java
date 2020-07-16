package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class SearchUserVo {

    @NotNull(message = "{\"ko_KR\" : \"검색구분을\"}")
    @Min(message = "{\"ko_KR\" : \"검색구분을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"검색구분을\"}", value = 2)
    private Integer userType;
    @NotBlank(message = "{\"ko_KR\" : \"검색어\"}")
    @NotNull(message = "{\"ko_KR\" : \"검색어\"}")
    private String search;
    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회건수를\"}", value = 1)
    private Integer records;
    private String searchType;
}
