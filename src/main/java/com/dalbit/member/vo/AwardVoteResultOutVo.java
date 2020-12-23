package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_AwardVoteResultVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AwardVoteResultOutVo {

    private int rowNum;
    private String memNo;
    private String nickNm;
    private ImageVo profileImage;
    private String fan1_memNo;
    private String fan1_nickNm;
    private ImageVo fan1_profileImage;
    private String fan2_memNo;
    private String fan2_nickNm;
    private ImageVo fan2_profileImage;
    private String fan3_memNo;
    private String fan3_nickNm;
    private ImageVo fan3_profileImage;


    public AwardVoteResultOutVo(){}
    public AwardVoteResultOutVo(P_AwardVoteResultVo target){
        setRowNum(target.getRowNum());
        setMemNo(target.getMemNo());
        setNickNm(target.getMemNick());
        setProfileImage(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setFan1_memNo(DalbitUtil.isEmpty(target.getFan1_memNo()) ? "" : target.getFan1_memNo());
        setFan1_nickNm(DalbitUtil.isEmpty(target.getFan1_memNick()) ? "" : target.getFan1_memNick());
        setFan1_profileImage(DalbitUtil.isEmpty(target.getFan1_profileImage()) ? new ImageVo() : new ImageVo(target.getFan1_profileImage(), target.getFan1_memSex(), DalbitUtil.getProperty("server.photo.url")));
        setFan2_memNo(DalbitUtil.isEmpty(target.getFan2_memNo()) ? "" : target.getFan2_memNo());
        setFan2_nickNm(DalbitUtil.isEmpty(target.getFan2_memNick()) ? "" : target.getFan2_memNick());
        setFan2_profileImage(DalbitUtil.isEmpty(target.getFan2_profileImage()) ? new ImageVo() : new ImageVo(target.getFan2_profileImage(), target.getFan2_memSex(), DalbitUtil.getProperty("server.photo.url")));
        setFan3_memNo(DalbitUtil.isEmpty(target.getFan3_memNo()) ? "" : target.getFan3_memNo());
        setFan3_nickNm(DalbitUtil.isEmpty(target.getFan3_memNick()) ? "" : target.getFan3_memNick());
        setFan3_profileImage(DalbitUtil.isEmpty(target.getFan3_profileImage()) ? new ImageVo() : new ImageVo(target.getFan3_profileImage(), target.getFan3_memSex(), DalbitUtil.getProperty("server.photo.url")));
    }
}
