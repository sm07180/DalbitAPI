package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class QnaVo {

    @Min(message = "{\"ko_KR\" : \"문의 구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"문의 구분을\"}", value = 99)
    private int qnaType;
    @NotBlank(message = "{\"ko_KR\" : \"제목을\"}")
    @NotNull(message = "{\"ko_KR\" : \"제목을\"}")
    @Size(message = "{\"ko_KR\" : \"제목을\"}", max = 100)
    private String title;
    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    private String contents;

    private String questionFile;
    private String questionFile1;
    private String questionFile2;
    private String questionFile3;
    private String questionFileName1;
    private String questionFileName2;
    private String questionFileName3;

    private int qnaIdx;

    //@NotBlank(message = "{\"ko_KR\" : \"이메일을\"}")
    //@NotNull(message = "{\"ko_KR\" : \"이메일을\"}")
    private String email;

    @Size(message = "{\"ko_KR\" : \"이름/닉네임을\"}", max = 50)
    private String nickName;
    private String phone;
}
