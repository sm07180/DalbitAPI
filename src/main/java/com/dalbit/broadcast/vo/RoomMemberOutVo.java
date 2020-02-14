package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_RoomMemberListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RoomMemberOutVo{

    private String memNo;
    private String nickNm;
    private String gender;
    private int age;
    private ImageVo profImg;
    private int auth;
    private String ctrlRole;
    private String joinDt;
    private long joinTs;
    private boolean isFan;
    private int goldCnt;

    public RoomMemberOutVo(P_RoomMemberListVo target){
        this.memNo = target.getMem_no();
        this.nickNm = target.getNickName();
        this.gender = target.getMemSex();
        this.age = DalbitUtil.ageCalculation(target.getBirthYear());
        this.profImg = new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url"));
        this.auth = target.getAuth();
        this.ctrlRole = target.getControlRole();
        this.joinDt = DalbitUtil.getUTCFormat(target.getJoin_date());
        this.joinTs = DalbitUtil.getUTCTimeStamp(target.getJoin_date());

        //TODO - 임시 데이터..
        this.isFan = Math.random() < 0.5 ? true : false;
        this.goldCnt = Integer.valueOf(DalbitUtil.randomValue("number", 3));
    }
}
