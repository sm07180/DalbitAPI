package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MessageInsertVo extends BaseVo {
    private String title;                //제목
    private String send_cont;       //메시지 내용
    private String target_rooms;       //발송 방송방번호 ('|' 구분자 사용)
    private String send_cnt;        //발송건수
    private String reg_date;            //등록일
    private String op_name;             //등록자

    private String send_all;           //전체 발송 여부  (0: 전체, 1:target)

}

