package com.dalbit.event.service;

import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.event.proc.LikeTreeEvent;
import com.dalbit.event.vo.LikeTreeStoryVO;
import com.dalbit.util.DBUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LikeTreeService {
    @Autowired
    LikeTreeEvent likeTreeEvent;

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 장식리스트
     * @작성일 : 2021-12-16
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    public ResVO getLikeTreeDecoList(Integer seqNo, Integer pageNo, Integer pagePerCnt) {
        ResVO result = new ResVO();
        Map<String, Object> resultInfo = new HashMap<>();

        try {
            List<Object> data = likeTreeEvent.getLikeTreeDecoList(seqNo, pageNo, pagePerCnt);
            Integer cnt = DBUtil.getData(data, 0, Integer.class);
            List<LikeTreeStoryVO> list = DBUtil.getList(data, 1, LikeTreeStoryVO.class);

            if (cnt > 0) {
                resultInfo.put("cnt", cnt);
                resultInfo.put("list", list);
                result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
            } else {
                result.setResVO(ResMessage.C99994.getCode(), ResMessage.C99994.getCodeNM(), null);
            }

        } catch (Exception e) {
            log.error("LikeTreeService / getLikeTreeDecoList => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 메인 리스트
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    public ResVO getLikeTreeMainList() {
        ResVO result = new ResVO();
        Map<String, Object> resultInfo = new HashMap<>();

        try {

            List<Object> data = likeTreeEvent.getLikeTreeMainList();
            Integer totScoreCnt = DBUtil.getData(data, 0, Integer.class);
            List<LikeTreeStoryVO> list = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                LikeTreeStoryVO temp = DBUtil.getData(data, (i + 1), LikeTreeStoryVO.class);
                if (temp != null) {
                    list.add(temp);
                }
            }

            resultInfo.put("totScoreCnt", totScoreCnt);
            resultInfo.put("list", list);

            result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
        } catch (Exception e) {
            log.error("LikeTreeService / getLikeTreeMainList => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 리스트
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    public ResVO getLikeTreeStoryList(Integer pageNo, Integer pagePerCnt) {
        ResVO result = new ResVO();
        Map<String, Object> resultInfo = new HashMap<>();

        try {
            List<Object> data = likeTreeEvent.getLikeTreeStoryList(pageNo, pagePerCnt);
            Integer cnt = DBUtil.getData(data, 0, Integer.class);
            List<LikeTreeStoryVO> list = DBUtil.getList(data, 1, LikeTreeStoryVO.class);

            if (cnt > 0) {
                resultInfo.put("cnt", cnt);
                resultInfo.put("list", list);
                result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
            } else {
                result.setResVO(ResMessage.C99994.getCode(), ResMessage.C99994.getCodeNM(), null);
            }
        } catch (Exception e) {
            log.error("LikeTreeService / getLikeTreeStoryList => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 등록
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    public ResVO likeTreeStoryIns(Map<String, Object> param) {
        ResVO result = new ResVO();

        try {
            Integer resultInfo = likeTreeEvent.likeTreeStoryIns(param);

            if (resultInfo == 1) {
                result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
            } else {
                result.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), null);
            }
        } catch (Exception e) {
            log.error("LikeTreeService / likeTreeStoryIns => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 수정
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    public ResVO likeTreeStoryUpd(Map<String, Object> param) {
        ResVO result = new ResVO();

        try {
            Integer resultInfo = likeTreeEvent.likeTreeStoryUpd(param);

            if (resultInfo == 1) {
                result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
            } else {
                result.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), null);
            }
        } catch (Exception e) {
            log.error("LikeTreeService / likeTreeStoryUpd => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 삭제
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    public ResVO likeTreeStoryDel(Map<String, Object> param) {
        ResVO result = new ResVO();

        try {
            Integer resultInfo = likeTreeEvent.likeTreeStoryDel(param);

            if (resultInfo == 1) {
                result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
            } else {
                result.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), null);
            }
        } catch (Exception e) {
            log.error("LikeTreeService / likeTreeStoryDel => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 신고 등록
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    public ResVO likeTreeStoryRptIns(Map<String, Object> param) {
        ResVO result = new ResVO();

        try {
            Integer resultInfo = likeTreeEvent.likeTreeStoryRptIns(param);

            if (resultInfo == 1) {
                result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
            } else {
                result.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), null);
            }

        } catch (Exception e) {
            log.error("LikeTreeService / likeTreeStoryRptIns => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 회원 리스트(랭킹)
     * @작성일 : 2021-12-17
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    public ResVO getLikeTreeRankList(Integer seqNo, Integer pageNo, Integer pagePerCnt) {
        ResVO result = new ResVO();
        Map<String, Object> resultInfo = new HashMap<>();

        try {
            List<Object> data = likeTreeEvent.getLikeTreeRankList(seqNo, pageNo, pagePerCnt);
            Integer cnt = DBUtil.getData(data, 0, Integer.class);
            List<LikeTreeStoryVO> list = DBUtil.getList(data, 1, LikeTreeStoryVO.class);

            if (cnt > 0) {
                resultInfo.put("cnt", cnt);
                resultInfo.put("list", list);
                result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
            } else {
                result.setResVO(ResMessage.C99994.getCode(), ResMessage.C99994.getCodeNM(), null);
            }
        } catch (Exception e) {
            log.error("LikeTreeService / getLikeTreeRankList => {}", e);
            result.setFailResVO();
        }
        return result;
    }
}
