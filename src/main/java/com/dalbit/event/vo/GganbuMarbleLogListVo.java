package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMarbleLogListVo {
    private String gganbu_no; // 회차번호
    private String mem_no; // 회원번호 (신청자)
    private String mem_nick; // 회원대화명 (신청자)
    private String ins_slct; // 구슬 획득/사용 위치 구분 [r:방송, c:달충전, e:교환, b:배팅]
    private String chng_slct; // 획득/사용 구분[s:획득, u:사용]
    private String red_marble; // 빨강구슬수
    private String yellow_marble; // 노랑구슬수
    private String blue_marble; // 파랑구슬수
    private String violet_marble; // 보라구슬수
    private String room_no; // 룸번호
    private String ins_date; // 등록일자

    private String image_profile;
    private String mem_sex;
    private ImageVo mem_profile;
}
