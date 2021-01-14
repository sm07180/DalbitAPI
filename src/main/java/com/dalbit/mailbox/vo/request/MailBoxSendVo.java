package com.dalbit.mailbox.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MailBoxSendVo {
    @NotBlank(message = "{\"ko_KR\" : \"채팅번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"채팅번호를\"}")
    private String chatNo;

    @NotNull(message = "{\"ko_KR\" : \"대화구분을\"}")
    @Min(message = "{\"ko_KR\" : \"대화구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"대화구분을\"}", value = 3)
    private Integer chatType;       //(1:text, 2:image, 3:gift)

    private String msgNo;           //서버생성

    private String msg;
    private String addData1="";     //부가정보1 (image: URL, gift: itemCode)
    private String addData2="";     //부가정보2 (gift: itemCnt)
    private String addData3="";     //부가정보3 (gift: itemType)
}
