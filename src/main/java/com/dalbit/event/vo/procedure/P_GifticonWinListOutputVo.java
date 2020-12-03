package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class P_GifticonWinListOutputVo extends P_ApiVo {
    /* Output */
    private int gifticon_type;
    private Date win_date;
    private int new_yn;
    private String mem_no;
    private String nickName;
    private String memSex;
    private String profileImage;

}
