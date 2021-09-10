package com.dalbit.member.vo.request;

import lombok.Data;

@Data
public class FanstarMultiInsertVo {
    private int type=0;   // 0:일반, 1:추천DJ

    private String[] memNoList;
}
