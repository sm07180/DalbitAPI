package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class ReplyAddVo {

    @NotBlank(message = "{\"ko_KR\" : \"회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"회원번호를\"}")
    private String memNo;		//이벤트 댓글 작성 회원번호

    @NotNull(message = "{\"ko_KR\" : \"댓글 구분을\"}")
    @Min(value = 1, message = "댓글 구분을")
    @Max(value = 2, message = "댓글 구분을")
    private Integer depth;			//1: 댓글, 2:대댓글

    @NotNull(message = "{\"ko_KR\" : \"이벤트를\"}")
    private Integer eventIdx;		//이벤트 번호
    @NotBlank(message = "{\"ko_KR\" : \"내용를\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용를\"}")
    @Size(message = "{\"ko_KR\" : \"내용를\"}", max = 500)
    private String content;		//내용

}
