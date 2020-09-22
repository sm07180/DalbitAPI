package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_BroadcastOptionListVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BroadcastOptionListOutVo {
    private String contents;
    private int idx;

    public BroadcastOptionListOutVo(P_BroadcastOptionListVo target){
        setContents(target.getContents());
        setIdx(target.getIdx());
    }
}
