package com.dalbit.clip.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_ClipRecommendLeaderExtVo extends P_ApiVo {

    public P_ClipRecommendLeaderExtVo() {
    }

    private String minRecDate;          // 가장 첫번째의 등록날짜 (화면에서 이전으로 더 못넘어가게 하기 위함)
}
