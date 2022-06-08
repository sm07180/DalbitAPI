package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DallaRoomSelResultVO {
    private Integer totalCnt;        // 전체 방송 수
    private Integer liveDjRank;      // 실시간 순위
    private String imageBackground;  // 방장 프로필 사진
    private String bjNickName;       // 방장 닉네임
    private String title;            // 방송방 제목
    private Integer countGood;       // 받은 좋아요
    private String startDate;        // 방송 시작 시간
}
