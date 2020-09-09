package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class ClipReplyEditVo {
    @NotBlank(message = "{\"ko_KR\" : \"클립 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 번호를\"}")
    private String clipNo;

    @NotNull(message = "{\"ko_KR\" : \"댓글 번호를\"}")
    private Integer replyIdx;

    @NotBlank(message = "{\"ko_KR\" : \"댓글 내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"댓글 내용을\"}")
    @Size(message = "{\"ko_KR\" : \"댓글 내용을\"}", max = 100)
    private String contents;

}
