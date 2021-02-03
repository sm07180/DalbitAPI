package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class StateEditVo {

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;

    @NotBlank(message = "{\"ko_KR\" : \"미디어 상태를\"}")
    @NotNull(message = "{\"ko_KR\" : \"미디어 상태를\"}")
    private String mediaState;

    @NotBlank(message = "{\"ko_KR\" : \"OnOff 여부를\"}")
    @NotNull(message = "{\"ko_KR\" : \"OnOff 여부를\"}")
    private String mediaOn;

}
