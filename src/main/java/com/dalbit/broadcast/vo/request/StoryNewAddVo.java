package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class StoryNewAddVo {

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;

    @NotBlank(message = "{\"ko_KR\" : \"사연 내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"사연 내용을\"}")
    @Size(message = "{\"ko_KR\" : \"사연 내용을\"}", max = 500)
    private String contents;

    private String djMemNo = "";

    private String plusYn = "n";
}
