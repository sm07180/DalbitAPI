package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoonLandCoinDataVO {
    private Integer score = 0;          //일반 코인의 점수 (달 갯수)
    private String jackpotYn = "n";       // 캐릭터 코인 조건의 3% 확률 => 성공: 'y', 실패: 'n'
    private Integer eventScore = 0;     //보너스 포인트 [황금코인, 캐릭터 코인]

}
