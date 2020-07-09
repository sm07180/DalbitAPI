package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.UserDao;
import com.dalbit.broadcast.vo.procedure.P_RoomInfoViewVo;
import com.dalbit.broadcast.vo.procedure.P_RoomStreamVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@Service
public class GuestService {
    @Autowired
    UserDao userDao;
    @Autowired
    RestService restService;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    SocketService socketService;

    public String guestStream(HttpServletRequest request){
        String result = "";
        String mem_no = request.getParameter("memNo");
        String room_no = request.getParameter("roomNo");
        String mode = request.getParameter("mode");

        Status status = null;
        HashMap resultMap = new HashMap();
        if(DalbitUtil.isEmpty(mem_no) || DalbitUtil.isEmpty(room_no)){
            status = Status.파라미터오류;
        }else{
            HashMap selParams = new HashMap();
            selParams.put("mem_no", mem_no);
            selParams.put("room_no", room_no);
            HashMap roomGuestInfo = userDao.selectGuestStreamInfo(selParams);
            if(roomGuestInfo != null && !DalbitUtil.isEmpty(DalbitUtil.getStringMap(roomGuestInfo, "title"))){
                if("publish".equals(mode)){
                    try{
                        String streamId = (String) restService.antCreate(DalbitUtil.getStringMap(roomGuestInfo, "title") + "'s Geust " + mem_no, request).get("streamId");
                        String publishToken = (String) restService.antToken(streamId, "publish", request).get("tokenId");
                        HashMap updateParam = new HashMap();
                        updateParam.put("mem_no", mem_no);
                        updateParam.put("room_no", room_no);
                        updateParam.put("guest_stream_id", streamId);
                        updateParam.put("guest_publish_token", publishToken);
                        if(userDao.updateGuestStreamInfo(updateParam) > 0){
                            status = Status.조회;
                            resultMap.put("guest_stream_id", streamId);
                            resultMap.put("guest_publish_token", publishToken);
                            resultMap.put("guest_play_token", "");
                            resultMap.put("memNo", mem_no);
                            resultMap.put("roomNo", room_no);
                            resultMap.put("mode", mode);
                        }else{
                            status = Status.비즈니스로직오류;
                        }
                    }catch(Exception e){
                        status = Status.비즈니스로직오류;
                    }
                }else if("play".equals(mode)){
                    if(!DalbitUtil.isEmpty(DalbitUtil.getStringMap(roomGuestInfo, "guest_streamid"))){
                        try{
                            String streamId = DalbitUtil.getStringMap(roomGuestInfo, "guest_streamid");
                            String playToken = (String) restService.antToken(streamId, "play", request).get("tokenId");
                            HashMap updateParam = new HashMap();
                            updateParam.put("mem_no", MemberVo.getMyMemNo(request));
                            updateParam.put("room_no", room_no);
                            updateParam.put("guest_stream_id", streamId);
                            updateParam.put("guest_play_token", playToken);
                            if(userDao.updateGuestStreamInfo(updateParam) > 0){
                                status = Status.조회;
                                resultMap.put("guest_stream_id", streamId);
                                resultMap.put("guest_publish_token", "");
                                resultMap.put("guest_play_token", playToken);
                                resultMap.put("memNo", mem_no);
                                resultMap.put("roomNo", room_no);
                                resultMap.put("mode", mode);
                            }else{
                                status = Status.비즈니스로직오류;
                            }
                        }catch(Exception e){
                            status = Status.비즈니스로직오류;
                        }
                    }else{
                        status = Status.방송참여_해당방이없음;
                    }
                }else{
                    try{
                        String streamId = DalbitUtil.getStringMap(roomGuestInfo, "guest_streamid");
                        if(DalbitUtil.isEmpty(streamId)){
                            streamId = (String) restService.antCreate(DalbitUtil.getStringMap(roomGuestInfo, "title") + "'s Geust " + mem_no, request).get("streamId");
                        }
                        String publishToken = (String) restService.antToken(streamId, "publish", request).get("tokenId");
                        String playToken = (String) restService.antToken(streamId, "play", request).get("tokenId");
                        HashMap updateParam = new HashMap();
                        updateParam.put("mem_no", mem_no);
                        updateParam.put("room_no", room_no);
                        updateParam.put("guest_stream_id", streamId);
                        updateParam.put("guest_publish_token", publishToken);
                        updateParam.put("guest_play_token", playToken);
                        if(userDao.updateGuestStreamInfo(updateParam) > 0){
                            status = Status.조회;
                            resultMap.put("guest_stream_id", streamId);
                            resultMap.put("guest_publish_token", publishToken);
                            resultMap.put("guest_play_token", playToken);
                            resultMap.put("memNo", mem_no);
                            resultMap.put("roomNo", room_no);
                            resultMap.put("mode", mode);
                        }else{
                            status = Status.비즈니스로직오류;
                        }
                    }catch(Exception e){
                        status = Status.비즈니스로직오류;
                    }
                }
            }else{
                status = Status.방송참여_해당방이없음;
            }
        }

        return gsonUtil.toJson(new JsonOutputVo(status, resultMap));
    }

    public String guest(HttpServletRequest request){
        String roomNo = request.getParameter("roomNo");
        String memNo = request.getParameter("memNo");
        String mode = request.getParameter("mode");

        Status status = null;
        HashMap resultMap = new HashMap();
        if(!DalbitUtil.isEmpty(roomNo) && !DalbitUtil.isEmpty(memNo) && !DalbitUtil.isEmpty(mode)){
            if("1".equals(mode) || "2".equals(mode) || "3".equals(mode) || "4".equals(mode) || "5".equals(mode) || "6".equals(mode)){
                String djMemNo = null;
                if("1".equals(mode)) { // 초대
                }else if("2".equals(mode)){ // 초대취소
                }else if("3".equals(mode) || "4".equals(mode) || "5".equals(mode)){ // 승락 | 거절 | 신청 방장번호 필요
                    HashMap selParams = new HashMap();
                    selParams.put("mem_no", memNo);
                    selParams.put("room_no", roomNo);
                    HashMap roomGuestInfo = userDao.selectGuestStreamInfo(selParams);
                    if(roomGuestInfo != null && !DalbitUtil.isEmpty(DalbitUtil.getStringMap(roomGuestInfo, "title"))){
                        if("3".equals(mode)) {
                            try{
                                String streamId = (String) restService.antCreate(DalbitUtil.getStringMap(roomGuestInfo, "title") + "'s Geust " + memNo, request).get("streamId");
                                String publishToken = (String) restService.antToken(streamId, "publish", request).get("tokenId");
                                HashMap updateParam = new HashMap();
                                updateParam.put("mem_no", memNo);
                                updateParam.put("room_no", roomNo);
                                updateParam.put("guest_stream_id", streamId);
                                updateParam.put("guest_publish_token", publishToken);
                                if(userDao.updateGuestStreamInfo(updateParam) > 0){
                                    resultMap.put("guest_stream_id", streamId);
                                    resultMap.put("guest_publish_token", publishToken);
                                    resultMap.put("guest_play_token", "");
                                    //resultMap.put("memNo", memNo);
                                    //resultMap.put("roomNo", roomNo);
                                }else{
                                    status = Status.비즈니스로직오류;
                                }
                            }catch(Exception e){
                                status = Status.비즈니스로직오류;
                            }
                        }else{
                            djMemNo = DalbitUtil.getStringMap(roomGuestInfo, "mem_no");
                        }
                    }else{
                        status = Status.방송참여_해당방이없음;
                    }
                }else if("6".equals(mode)){ // 퇴장
                }
                if(status == null){
                    status = Status.조회;
                    try{
                        socketService.sendGuest(memNo, roomNo, djMemNo, mode, request, DalbitUtil.getAuthToken(request));
                    }catch(Exception e){}
                }
            }else{
                status = Status.파라미터오류;
            }
        }else{
            status = Status.파라미터오류;
        }

        return gsonUtil.toJson(new JsonOutputVo(status, resultMap));
    }
}
