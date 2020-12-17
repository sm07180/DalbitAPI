package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class ClipRecommendListInputVo {

    @NotBlank(message = "{\"ko_KR\" : \"날짜를\"}")
    @NotNull(message = "{\"ko_KR\" : \"날짜를\"}")
    private String recDate;

    @NotNull(message = "{\"ko_KR\" : \"로그인 상태를\"}")
    private Boolean isLogin;  // 로그인 : true, 비로그인 : false

    private Boolean isClick;  // 클릭 : 1, 클릭x : 0 (조회수 카운트를 위한 클릭 여부)
}
