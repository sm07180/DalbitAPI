package com.dalbit.event.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_AttendanceCheckLoadOutputVo {
    private String the_date;
    private int the_day;
    private int check_ok;
    private int reward_exp;
    private int reward_dal;
}
