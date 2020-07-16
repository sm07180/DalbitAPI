package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ShareLinkVo {

    @NotBlank(message = "{\"ko_KR\" : \"공유번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"공유번호를\"}")
    private String link;
}
