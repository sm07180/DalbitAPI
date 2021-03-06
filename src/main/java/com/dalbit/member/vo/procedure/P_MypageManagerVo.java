package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter @Setter
public class P_MypageManagerVo extends P_ApiVo {
    /*  Input */
    private String mem_no;

    /* Output */
    private Date regDate;
    private String mem_nick;
    private String mem_id;
    private String controlRole;
    private String memSex;
    private String profileImage;
}
