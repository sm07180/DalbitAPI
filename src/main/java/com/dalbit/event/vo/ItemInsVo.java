package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemInsVo {
    private String memNo; // 회원번호
    private String itemType; // 1:부스터
    private int itemState; // 1: 지급, 2: 사용, 3: 차감
    private int itemCnt; // 아이템 수
    private String opName; // 처리내용
}
