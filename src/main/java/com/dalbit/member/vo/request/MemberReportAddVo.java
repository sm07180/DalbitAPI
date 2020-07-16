package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
@Getter
@Setter
public class MemberReportAddVo {

    @NotBlank(message = "{\"ko_KR\" : \"신고할 회원을\"}")
    @NotNull(message = "{\"ko_KR\" : \"신고할 회원을\"}")
    private String memNo;

    @NotNull(message = "{\"ko_KR\" : \"신고 사유를\"}")
    private Integer reason;

    @Size (message = "{\"ko_KR\" : \"상세 사유를\"}", max = 256)
    private String cont;

    private String roomNo;
}
