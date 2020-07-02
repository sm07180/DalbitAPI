package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.UserDao;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
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
                        String streamId = (String) restService.antCreate(DalbitUtil.getStringMap(roomGuestInfo, "title") + "'s Geust " + mem_no, request).get("streamId");
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
}
