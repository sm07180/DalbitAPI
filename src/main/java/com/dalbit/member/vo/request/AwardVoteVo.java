package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class AwardVoteVo {
    @NotBlank(message = "{\"ko_KR\" : \"투표년도를\"}")
    @NotNull(message = "{\"ko_KR\" : \"투표년도를\"}")
    private String year;

    @NotNull(message = "{\"ko_KR\" : \"DJ_1 번호를\"}")
    private Integer djIdx_1;
    @NotNull(message = "{\"ko_KR\" : \"DJ_2 번호를\"}")
    private Integer djIdx_2;
    @NotNull(message = "{\"ko_KR\" : \"DJ_3 번호를\"}")
    private Integer djIdx_3;

    @NotBlank(message = "{\"ko_KR\" : \"DJ_1 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"DJ_1 회원번호를\"}")
    private String djMemNo_1;
    @NotBlank(message = "{\"ko_KR\" : \"DJ_2 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"DJ_2 회원번호를\"}")
    private String djMemNo_2;
    @NotBlank(message = "{\"ko_KR\" : \"DJ_3 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"DJ_3 회원번호를\"}")
    private String djMemNo_3;
}
