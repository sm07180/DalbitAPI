package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_MiniGameListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MiniGameListOutVo {

    private int gameNo;
    private String gameName;
    private String gameImg;
    private String gameDesc;

    public MiniGameListOutVo(){}
    public MiniGameListOutVo(P_MiniGameListVo target){
        setGameNo(target.getGameNo());
        setGameName(target.getGameName());
        setGameImg(target.getImageUrl());
        setGameDesc(target.getGameDesc());
    }


}
