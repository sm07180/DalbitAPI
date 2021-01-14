package com.dalbit.mailbox.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MailBoxMsgListVo {
    @NotBlank(message = "{\"ko_KR\" : \"채팅번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"채팅번호를\"}")
    private String chatNo;

    @NotNull(message = "{\"ko_KR\" : \"대화시작 번호를\"}")
    @NotBlank(message = "{\"ko_KR\" : \"대화시작 번호를\"}")
    private String nowIdx;       //(0: 전체)

    private Integer page;
    private Integer records;
}
