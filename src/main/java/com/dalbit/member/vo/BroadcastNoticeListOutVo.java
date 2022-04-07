package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter @Setter
public class BroadcastNoticeListOutVo {
    private Integer auto_no;    //INT       자동증가 번호
    @NotNull
    private Long mem_no;        //BIGINT    회원 번호

    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    @Size(message = "{\"ko_KR\" : \"내용을\"}", max = 1024)
    private String conts;       //VARCHAR   방송공지 등록글

    private Date ins_date;      //DATETIME  등록 일자
    private Date upd_date;      //DATETIME  수정 일자
}
