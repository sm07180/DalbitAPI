package com.dalbit.broadcast.vo.request;

import com.dalbit.broadcast.vo.procedure.P_GuestManagementListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter @Setter @ToString
public class GuestListOutVo {

    private String memNo;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private int gstState;
    private String gstStartDt;
    private long gstStartTs;
    private int gstProposeState;
    private boolean isFan;
    private int gstTime;
    private int os;

    public GuestListOutVo(P_GuestManagementListVo target) {

        this.memNo = target.getMem_no();
        this.nickNm = target.getNickname();
        this.gender = target.getMemSex();
        this.profImg = new ImageVo(target.getProfileImage(), this.gender, DalbitUtil.getProperty("server.photo.url"));
        this.gstState = target.getGuestState();
        this.gstStartDt = DalbitUtil.isEmpty(target.getGuestStartDate()) ? "" :DalbitUtil.getUTCFormat(target.getGuestStartDate());
        this.gstStartTs = DalbitUtil.isEmpty(target.getGuestStartDate()) ? 0 :DalbitUtil.getUTCTimeStamp(target.getGuestStartDate());
        this.gstProposeState = target.getGuest_propose();
        this.isFan = target.getIsFan()  == 1 ? true : false;

        //게스트 시작시간 (현재시간 - 시작시간 (초단위))
        if(!DalbitUtil.isEmpty(target.getGuestStartDate())){
            Date time = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.gstTime = (int) (DalbitUtil.getUTCTimeStamp(sdf.format(time)) - DalbitUtil.getUTCTimeStamp(target.getGuestStartDate())); //"현재시간 - 시작시간 (초단위)
        } else {
            this.gstTime = 0;
        }

        this.os = target.getOs();

    }
}
