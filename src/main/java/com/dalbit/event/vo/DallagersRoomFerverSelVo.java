package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DallagersRoomFerverSelVo {
    private Long room_no;                 //BIGINT   방번호
    private Integer gift_fever_cnt;       //INT      피버횟수(선물)
    private Integer play_fever_cnt;       //INT      피버횟수(시간)
    private Integer tot_fever_cnt;        //INT      총합
    private String fever_yn;              //CHAR     피버확인[y,n]
    private Integer gold;                 //INT      누적 받은별
    private Integer booster_cnt;          //INT      누적 부스터수
    private String start_date;            //DATETIME 방송시작시간
}
