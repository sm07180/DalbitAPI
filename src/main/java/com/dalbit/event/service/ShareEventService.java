package com.dalbit.event.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.event.proc.ShareEvent;
import com.dalbit.event.vo.ShareEventInputVo;
import com.dalbit.event.vo.ShareEventVo;
import com.dalbit.util.DBUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dalbit.util.GsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShareEventService {
    private final ShareEvent shareEvent;
    private final GsonUtil gsonUtil;

    /**********************************************************************************************
    * @Method 설명 : # 공유 이벤트 댓글 목록
    * @작성일   : 2022-02-22
    * @작성자   : 박성민
    **********************************************************************************************/
    public String shareTailList(ShareEventVo shareEventVo) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Object> procResult = shareEvent.shareTailList(shareEventVo);
            Integer listCnt = DBUtil.getData(procResult, 0, Integer.class);
            List<ShareEventVo> list = DBUtil.getList(procResult, 1, ShareEventVo.class);

            result.put("listCnt", listCnt);
            result.put("list", list);

            return gsonUtil.toJson(new JsonOutputVo(Status.공유이벤트_댓글목록_조회_성공, result));
        } catch (Exception e) {
            log.error("ShareEventService shareTailList Error : ", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공유이벤트_댓글목록_조회_실패, result));
        }
    }

    /**********************************************************************************************
     * @Method 설명 : # 공유 이벤트 댓글 삭제
     * @작성일   : 2022-02-22
     * @작성자   : 박성민
     **********************************************************************************************/
    public String shareTailDel(ShareEventInputVo shareEventInputVo) {
        Integer procResult = 0;
        try {
            procResult = shareEvent.shareTailDel(shareEventInputVo);
            if(procResult == 1) {
                return gsonUtil.toJson(new JsonOutputVo(Status.공유이벤트_댓글목록_삭제_성공, procResult));
            }else {
                return gsonUtil.toJson(new JsonOutputVo(Status.공유이벤트_댓글목록_삭제_실패, procResult));
            }
        } catch (Exception e) {
            log.error("ShareEventService shareTailDel Error : ", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공유이벤트_댓글목록_삭제_실패, procResult));
        }
    }

    /**********************************************************************************************
     * @Method 설명 : # 공유 이벤트 댓글 등록
     * @작성일   : 2022-02-22
     * @작성자   : 박성민
     **********************************************************************************************/
    public String shareTailIns(ShareEventInputVo shareEventInputVo) {
        Integer procResult = 0;
        try {
            procResult = shareEvent.shareTailIns(shareEventInputVo);
            if(procResult == 1) {
                return gsonUtil.toJson(new JsonOutputVo(Status.공유이벤트_댓글목록_등록_성공, procResult));
            }else {
                return gsonUtil.toJson(new JsonOutputVo(Status.공유이벤트_댓글목록_등록_실패, procResult));
            }
        } catch (Exception e) {
            log.error("ShareEventService shareTailIns Error : ", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공유이벤트_댓글목록_등록_실패, procResult));
        }
    }

    /**********************************************************************************************
     * @Method 설명 : # 공유 이벤트 댓글 수정
     * @작성일   : 2022-02-22
     * @작성자   : 박성민
     **********************************************************************************************/
    public String shareTailUpd(ShareEventInputVo shareEventInputVo) {
        Integer procResult = 0;
        try {
            procResult = shareEvent.shareTailUpd(shareEventInputVo);
            if(procResult == 1) {
                return gsonUtil.toJson(new JsonOutputVo(Status.공유이벤트_댓글목록_수정_성공, procResult));
            }else {
                return gsonUtil.toJson(new JsonOutputVo(Status.공유이벤트_댓글목록_수정_실패, procResult));
            }
        } catch (Exception e) {
            log.error("ShareEventService shareTailUpd Error : ", e);
            return gsonUtil.toJson(new JsonOutputVo(Status.공유이벤트_댓글목록_수정_실패, procResult));
        }
    }
}
