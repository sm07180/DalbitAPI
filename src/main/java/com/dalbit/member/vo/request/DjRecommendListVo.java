package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class DjRecommendListVo {
    @NotNull(message = "{\"ko_KR\" : \"나이대를\"}")
    @NotBlank(message = "{\"ko_KR\" : \"나이대를\"}")
    private String ageList="1";

    @NotNull(message = "{\"ko_KR\" : \"성별을\"}")
    @NotBlank(message = "{\"ko_KR\" : \"성별을\"}")
    private String gender="m";

    //private Integer page;
    //private Integer records;
}
