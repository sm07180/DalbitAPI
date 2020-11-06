package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EventPageWinnerAddInfoEditVo {

    @NotNull(message = "{\"ko_KR\" : \"이벤트를\"}")
    private int eventIdx;
    @NotNull(message = "{\"ko_KR\" : \"경품을\"}")
    private int prizeIdx;

    @NotNull(message = "{\"ko_KR\" : \"이름을\"}")
    private String winner_name;

    @NotNull(message = "{\"ko_KR\" : \"휴대폰 번호를\"}")
    private String winner_phone;
    @NotNull(message = "{\"ko_KR\" : \"이메일을\"}")
    private String winner_email;
    @NotNull(message = "{\"ko_KR\" : \"우편번호를\"}")
    private String winner_post_code;
    @NotNull(message = "{\"ko_KR\" : \"주소를\"}")
    private String winner_address_1;

    private String winner_social_no;
    private String winner_address_2;
    private String winner_add_file_1;
    private String winner_add_file_1_name;
    private String winner_add_file_2;
    private String winner_add_file_2_name;
}
