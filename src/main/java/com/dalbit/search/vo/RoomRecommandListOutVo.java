package com.dalbit.search.vo;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.search.vo.procedure.P_RoomRecommandListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class RoomRecommandListOutVo {

    private String roomNo;
    private String roomType;
    private String title;
    private String nickNm;
    private ImageVo bgImg;
    private int entryType;
    private int entryCnt;
    private String startDt;
    private long startTs;
    private Boolean isSpecial;
    private int imageType;

    public RoomRecommandListOutVo(){}
    public RoomRecommandListOutVo(P_RoomRecommandListVo target, HttpServletRequest request){
        this.roomNo = target.getRoomNo();
        this.roomType = target.getSubject_type();
        this.title = target.getTitle();
        this.nickNm = target.getNickName();
        DeviceVo deviceVo = new DeviceVo(request);
        if(target.getBadge_special() == 1 && target.getImageType() == 1){    //스페셜DJ일 경우 실시간live 이미지 노출선택(1:프로필, 2:배경)
            if(deviceVo != null && target.getImage_background().toString().toLowerCase().endsWith(".gif")){
                if(deviceVo.getOs() == 2 && DalbitUtil.versionCompare("14.0", deviceVo.getSdkVersion())){
                    this.bgImg = new ImageVo(target.getBj_profileImage(), target.getBj_memSex(), DalbitUtil.getProperty("server.photo.url"));
                }else{ //webp사용
                    this.bgImg = new ImageVo(target.getBj_profileImage().toString() + ".webp", target.getBj_memSex(), DalbitUtil.getProperty("server.photo.url"));
                }
            }else{
                this.bgImg = new ImageVo(target.getBj_profileImage(), target.getBj_memSex(), DalbitUtil.getProperty("server.photo.url"));
            }
        }else{
            this.bgImg = new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
        }
        this.entryType = target.getType_entry();
        this.entryCnt = target.getCount_entry();
        this.startDt = DalbitUtil.getUTCFormat(target.getStart_date());
        this.startTs = DalbitUtil.getUTCTimeStamp(target.getStart_date());
        this.isSpecial = target.getBadge_special() == 1 ? true : false;
        this.imageType = target.getImageType();

    }
}
