package com.dalbit.mailbox.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MailBoxReadVo {
    @NotBlank(message = "{\"ko_KR\" : \"채팅번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"채팅번호를\"}")
    private String chatNo;
}
