package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ManagerAddVo {

    @NotBlank(message = "{\"ko_KR\" : \"매니저로 등록할 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"매니저로 등록할 회원번번호를\"}")
    private String memNo;
    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;
    
    private String role;
}
