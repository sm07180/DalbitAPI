package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.RoomEditVo;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_RoomEditVo extends P_ApiVo {

    public P_RoomEditVo(){}
    public P_RoomEditVo(RoomEditVo roomEditVo, HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(roomEditVo.getRoomNo());
        setSubjectType(roomEditVo.getRoomType());
        setTitle(roomEditVo.getTitle());
        if(!DalbitUtil.isEmpty(roomEditVo.getBgImg())){
            setBackgroundImage(roomEditVo.getBgImg());
        }
        setBackgroundImageDelete(roomEditVo.getBgImgDel());
        setBackgroundImageGrade(DalbitUtil.isStringToNumber(roomEditVo.getBgImgRacy()));
        setWelcomMsg(roomEditVo.getWelcomMsg());

        DeviceVo deviceVo = new DeviceVo(request);
        setOs(deviceVo.getOs());
        setDeviceUuid(deviceVo.getDeviceUuid());
        setDeviceToken(deviceVo.getDeviceToken());
        setAppVersion(deviceVo.getAppVersion());
        setDjListenerIn(roomEditVo.getDjListenerIn());
        setDjListenerOut(roomEditVo.getDjListenerOut());
        setImageType(roomEditVo.getImageType());
    }

    private String mem_no;                  //방 수정하는 회원번호
    private String room_no;                 //방 번호
    private String subjectType;             //방송종류
    private String title;                   //방송제목
    private String backgroundImage;         //배경이미지 경로
    private String backgroundImageDelete;   //삭제할 배경이미지 경로
    private int backgroundImageGrade;       //배경이미지 선정성
    private String welcomMsg;               //환영메시지
    private int os;                         //OS구분
    private String deviceUuid;              //디바이스 고유아이디
    private String deviceToken;             //디바이스 토큰
    private String appVersion;              //앱 버전

    private String djListenerIn;
    private String djListenerOut;

    private int imageType;                  //스페셜DJ일 경우 실시간live 이미지 노출선택(1:프로필, 2:배경)

}
