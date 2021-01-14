package com.dalbit.mailbox.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MailBoxAddTargetListVo {
    @NotNull(message = "{\"ko_KR\" : \"타입구분을\"}")
    @Min(message = "{\"ko_KR\" : \"타입구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"타입구분을\"}", value = 2)
    private Integer slctType;   //1:팬, 2:스타

    private Integer page;
    private Integer records;
}
