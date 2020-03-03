package com.dalbit.member.vo.procedure;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter @Setter
public class P_MypageManagerVo {
    /*  Input */
    private String mem_no;

    /* Output */
    private Date regDate;
    private String mem_nick;
    private String mem_id;
    private String controlRole;
}
