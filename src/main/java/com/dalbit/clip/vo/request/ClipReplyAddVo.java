package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class ClipReplyAddVo {
    @NotBlank(message = "{\"ko_KR\" : \"클립 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 번호를\"}")
    private String clipNo;

    @NotBlank(message = "{\"ko_KR\" : \"댓글 내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"댓글 내용을\"}")
    @Size(message = "{\"ko_KR\" : \"댓글 내용을\"}", max = 100)
    private String contents;


}
