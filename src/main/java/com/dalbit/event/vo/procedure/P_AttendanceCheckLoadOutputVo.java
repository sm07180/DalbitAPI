package com.dalbit.event.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_AttendanceCheckLoadOutputVo {

    public P_AttendanceCheckLoadOutputVo(){}

    public P_AttendanceCheckLoadOutputVo(String the_date, int the_day, int check_ok, int reward_exp, int reward_dal){
        setThe_date(the_date);
        setThe_day(the_day);
        setCheck_ok(check_ok);
        setReward_exp(reward_exp);
        setReward_dal(reward_dal);

    }
    private String the_date;
    private int the_day;
    private int check_ok;
    private int reward_exp;
    private int reward_dal;
}
