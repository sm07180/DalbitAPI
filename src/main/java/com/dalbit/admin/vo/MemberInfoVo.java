package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoVo {

    public MemberInfoVo(){}
    public MemberInfoVo(String mem_no){
        setMem_no(mem_no);
    }

    private String image_profile;
    private String mem_no;
    private String mem_userid;
    private String certYn;
    private String mem_name;
    private String mem_id;
    private String mem_phone;
    private String mem_nick;
    private String birthDate;
    private String age;
    private String mem_sex;
    private String op_memo_cnt;
    private int level;
    private String grade;
    private String mem_state;
    private String connect_state;
    private String broadcast_room_no;
    private String broadcast_start_date;
    private String broadcast_state;
    private String listen_room_no;
    private String listening_state;
    private int dal;
    private int byeol;
    private int manager_ICnt;
    private int manager_MeCnt;
    private int black_ICnt;
    private int black_MeCnt;
    private String mem_slct;
    private String join_date;
    private String withdrawal_date;
    private String first_broadcast_date;
    private String last_op_date;
    private String last_op_name;
    private String device_uuid;
    private String ip;

    private String nickName;
}
