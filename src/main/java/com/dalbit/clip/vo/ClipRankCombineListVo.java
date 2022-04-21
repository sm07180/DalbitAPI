package com.dalbit.clip.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClipRankCombineListVo {
    private Integer rankType = 1;
    private String rankingDate;
    private Integer page = 1;
    private Integer records = 100;
    private String callType = "0"; // 0: 어제 + 오늘, 1: 오늘, 2: 저번주 + 이번주, 3: 이번주
}
