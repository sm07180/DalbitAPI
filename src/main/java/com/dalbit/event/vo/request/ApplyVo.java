package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class ApplyVo {

    public ApplyVo(){}

    public ApplyVo(int eventIdx){
        this.eventIdx = eventIdx;
    }

    @NotNull(message = "{\"ko_KR\" : \"이벤트를\"}")
    private Integer eventIdx;
    private String name;
    private String contactNo;
    private String recvData1;
    private String recvData2;
    private String recvData3;
    private String recvData4;
    private String recvData5;
}
