package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NotificationDeleteVo {

    @NotBlank(message = "{\"ko_KR\" : \"알림번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"알림번호를\"}")
    private String delete_notiIdx;

}