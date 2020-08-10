package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.event.vo.procedure.P_GifticonWinListOutputVo;
import com.dalbit.event.vo.procedure.P_ReplyListOutputVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GifiticonWinListOutputVo {

    /* Output */
    private int gifticonType;
    private String winDt;
    private long winTs;
    private String memNo;
    private String nickNm;
    private String memSex;
    private ImageVo profImg;	//작성자 프로필이미지

    public GifiticonWinListOutputVo(){}

    public GifiticonWinListOutputVo(P_GifticonWinListOutputVo target) {
        setGifticonType(target.getGifticon_type());
        setWinDt(DalbitUtil.isEmpty(target.getWin_date()) ? "" : DalbitUtil.getUTCFormat(target.getWin_date()));
        setWinTs(DalbitUtil.isEmpty(target.getWin_date()) ? 0 : DalbitUtil.getUTCTimeStamp(target.getWin_date()));
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setMemSex(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
    }
}
