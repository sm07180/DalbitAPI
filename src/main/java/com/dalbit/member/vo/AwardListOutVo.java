package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_AwardListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AwardListOutVo {

    private int djIdx;
    private String djMemNo;
    private String djNickNm;
    private ImageVo djProfImg;
    private String fan1_memNo;
    private String fan1_nickNm;
    private ImageVo fan1_profImg;
    private String fan2_memNo;
    private String fan2_nickNm;
    private ImageVo fan2_profImg;
    private String fan3_memNo;
    private String fan3_nickNm;
    private ImageVo fan3_profImg;

    public AwardListOutVo(){}
    public AwardListOutVo(P_AwardListVo target){
        setDjIdx(target.getIdx());
        setDjMemNo(target.getDj_memNo());
        setDjNickNm(target.getDj_nickName());
        setDjProfImg(new ImageVo(target.getDj_profileImage(), target.getDj_memSex(), DalbitUtil.getProperty("server.photo.url")));
        setFan1_memNo(target.getFan1_memNo());
        setFan1_nickNm(target.getFan1_nickName());
        setFan1_profImg(new ImageVo(target.getFan1_profileImage(), target.getFan1_memSex(), DalbitUtil.getProperty("server.photo.url")));
        setFan2_memNo(target.getFan2_memNo());
        setFan2_nickNm(target.getFan2_nickName());
        setFan2_profImg(new ImageVo(target.getFan2_profileImage(), target.getFan2_memSex(), DalbitUtil.getProperty("server.photo.url")));
        setFan3_memNo(target.getFan3_memNo());
        setFan3_nickNm(target.getFan3_nickName());
        setFan3_profImg(new ImageVo(target.getFan3_profileImage(), target.getFan3_memSex(), DalbitUtil.getProperty("server.photo.url")));
    }


}
