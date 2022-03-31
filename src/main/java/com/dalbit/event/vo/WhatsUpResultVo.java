package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WhatsUpResultVo {
    private Integer seqNo;              // 회차 번호
    private Integer rankNo;             // 순위
    private Integer fanCnt;             // 팬 수
    private Integer newFanCnt;          // 신입팬 수
    private String memNo;               // 회원 번호
    private String memId;               // 회원 아이디
    private String memNick;             // 회원 닉네임
    private String memSex;              // 회원성별
    private String imageProfile;        // 프로필
    private Integer memLevel;           // 레벨
    private Integer memState;           // 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
    private LocalDateTime insDate;      // 등록일시
    private LocalDateTime updDate;      // 수정일시
    private long viewScoreCnt;          // 방송점수
    private long totScoreCnt;           // 총수

    private LocalDateTime startDate;    // 회차시작일시
    private LocalDateTime endDate;      // 회차종료일시

    private ImageVo profImg;

}
