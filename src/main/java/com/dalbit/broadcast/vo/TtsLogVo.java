package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TtsLogVo {
    private String ttsYn; // 아이템사용여부
    private String memNo; // 회원번호 (보낸이)
    private String pmemNo; // 회원번호 (받은이)
    private String itemCode; // 사용아이템 코드
    private String itemName; // 사용아이템 이름
    private String ttsCrtSlct; // TTS 캐릭터 구분 (a:빠다가이, b:하나)
    private String ttsMsg; // 메세지 내용
    private int sendItemCnt; // 아이템선물수
    private int sendDalCnt; // 선물달수
}
