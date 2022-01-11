package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoonLandCoinDataVO {
    private Integer score = 0;          //일반 코인의 점수 (달 갯수)
    private Integer goldScore = 0;     //보너스 포인트 [황금코인]
    private Integer characterCnt = 0;       // 캐릭터 코인 조건의 3% 확률 => 성공: 'y', 실패: 'n'
    private Integer characterScore = 0;   //보너스 포인트 [캐릭터 코인]
    private String missionSuccessYn = "n";     //아이템 미션 달성 여부 ["y", "n"]

}
