package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_RoomGuestVo extends P_ApiVo {

    private String mem_no;                      //요청 회원번호
    private String room_no;                     //참가하려는 방 번호
    private String guest_mem_no;                //게스트 지정할 회원번호
    private String guest_streamid;              //guest 스트림아이디
    private String guest_publish_tokenid;       //guest 토큰아이디
    private String guest_play_tokenid;          //guest play토큰
    private String bj_streamid;                 //bj 스트림아이디
    private String bj_publish_tokenid;          //bj 토큰아이디
    private String bj_play_tokenid;             //bj play토큰

    public P_RoomGuestVo(){}
    public P_RoomGuestVo(String memNo, String guestNo, String roomNo, String bjStreamId, String bjPublishToken, String bjPlayToken, String guestStreamId, String guestPublishToken, String guestPlayToken, HttpServletRequest request){
        setMem_no(memNo);
        setRoom_no(roomNo);
        setGuest_mem_no(guestNo);
        setGuest_streamid(guestStreamId);
        setGuest_publish_tokenid(guestPublishToken);
        setGuest_play_tokenid(guestPlayToken);
        setBj_streamid(bjStreamId);
        setBj_publish_tokenid(bjPublishToken);
        setBj_play_tokenid(bjPlayToken);
    }

}
