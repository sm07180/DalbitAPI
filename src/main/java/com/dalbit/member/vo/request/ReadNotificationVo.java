package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class ReadNotificationVo {

    @NotNull
    private Integer notiIdx;
}
