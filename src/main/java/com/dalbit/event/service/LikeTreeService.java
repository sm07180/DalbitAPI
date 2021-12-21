package com.dalbit.event.service;

import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.event.proc.LikeTreeEvent;
import com.dalbit.event.vo.LikeTreeRankingVO;
import com.dalbit.event.vo.LikeTreeRewardInsVo;
import com.dalbit.event.vo.LikeTreeStoryVO;
import com.dalbit.util.DBUtil;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LikeTreeService {
    @Autowired
    LikeTreeEvent likeTreeEvent;
    @Autowired CommonService commonService;

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
    public ResVO getLikeTreeMainList(String memNo) {
        ResVO result = new ResVO();
        Map<String, Object> resultInfo = new HashMap<>();
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime endDay = LocalDateTime.parse("2022-01-28T00:00:00.000");

        try {

            List<Object> data = likeTreeEvent.getLikeTreeMainList(memNo);
            String stepYn = DBUtil.getData(data, 0, String.class);
            Integer totScoreCnt = DBUtil.getData(data, 1, Integer.class);
            List<LikeTreeStoryVO> list = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                LikeTreeStoryVO temp = DBUtil.getData(data, (i + 1), LikeTreeStoryVO.class);
                if (temp != null && temp.getAutoNo() != 0) {
                    list.add(temp);
                }
            }

            // 이벤트 단계 설정, check 선물 받았는지 확인하는 프로시저 필요함
            if (!today.isBefore(endDay)) {
                resultInfo.put("step", ("n".equals(stepYn) ? 3 : 2)); // 2 - 보상 안받음, 3 - 보상 받음 
            }

            resultInfo.put("step", 1); // 1 - 보상 수령 기간 아님
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

    /**
     *  좋아요 트리 사연 내용 valid
     */
    private ResVO storyValidationCheck(String story) {
        ResVO resVO = new ResVO();
        int STORY_MAX_LENGTH = 100;

        try {
            String systemBanWord = commonService.banWordSelect();
            if(story == null || story.trim().length() == 0) { // 0자
                resVO.setResVO(ResMessage.C30211.getCode(), ResMessage.C30211.getCodeNM(), null);
            }else if(story.length() > STORY_MAX_LENGTH) { // max 초과
                resVO.setResVO(ResMessage.C30212.getCode(), ResMessage.C30212.getCodeNM(), null);
            }else if(DalbitUtil.isStringMatchCheck(systemBanWord, story)){ //금지어 체크
                resVO.setResVO(ResMessage.C30213.getCode(), ResMessage.C30213.getCodeNM(), null);
            }else {
                resVO.setSuccessResVO(null);
            }
        } catch (Exception e) {
            log.error("storyValidationCheck error ===> {}", e);
            resVO.setResVO(ResMessage.C30210.getCode(), ResMessage.C30210.getCodeNM(), null);
        }

        return resVO;
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
            String storyConts = String.valueOf(param.get("storyConts"));
            ResVO validCheck = storyValidationCheck(storyConts);

            if(!StringUtils.equals(validCheck.getCode(), "00000")) { // 사연 내용 유효성 체크
                result = validCheck;
            }else { // 정상
                Integer resultInfo = likeTreeEvent.likeTreeStoryIns(param);

                if (resultInfo == 1) {
                    result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
                } else {
                    result.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), null);
                }
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
            String storyConts = String.valueOf(param.get("storyConts"));
            ResVO validCheck = storyValidationCheck(storyConts);

            if(!StringUtils.equals(validCheck.getCode(), "00000")) { // 사연 내용 유효성 체크
                result = validCheck;
            }else { // 정상
                Integer resultInfo = likeTreeEvent.likeTreeStoryUpd(param);

                if (resultInfo == 1) {
                    result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
                } else {
                    result.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), null);
                }
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
            if(param.get("storyNo") == null) { // 사연번호 없음
                result.setResVO(ResMessage.C30215.getCode(), ResMessage.C30215.getCodeNM(), null);
            }else { // 정상
                Integer resultInfo = likeTreeEvent.likeTreeStoryDel(param);

                if (resultInfo == 1) {
                    result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), resultInfo);
                } else if(resultInfo == -1) {
                    result.setResVO(ResMessage.C30214.getCode(), ResMessage.C30214.getCodeNM(), resultInfo);
                } else {
                    result.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), null);
                }
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
            List<LikeTreeRankingVO> list = DBUtil.getList(data, 1, LikeTreeRankingVO.class);

            if (cnt != null && cnt > 0) {
                resultInfo.put("cnt", cnt);
                for (int i = 1; i <= list.size(); i++) {
                    list.get(i - 1).setRankNo((pageNo - 1) * pagePerCnt + i);
                }
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

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 회원의 랭킹 정보
     * @작성일 : 2021-12-20
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/ 
    public ResVO getLikeTreeRankUserInfo(Integer seqNo, String memNo) {
        ResVO result = new ResVO();
        
        try {
            LikeTreeRankingVO data = likeTreeEvent.getLikeTreeRankUserInfo(seqNo, memNo);
            if (data != null && data.getMemNo().equals(memNo)) {
                result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), data);
            } else {
                result.setResVO(ResMessage.C99994.getCode(), ResMessage.C99994.getCodeNM(), null);
            }
        } catch (Exception e) {
            log.error("LikeTreeService / getLikeTreeRankList => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 이벤트 보상 지급
     * @작성일 : 2021-12-17
     * @작성자 : 박성민
     * @변경이력 :
     * @Parameter : memNo, memPhone
     * @Return : -3:100점 미만 , -2:본인 미인증, -1:이미 인증받은 번호로 달 받음 , 0: 에러, 1:정상
     **********************************************************************************************/
    public ResVO getLikeTreeRewardIns(Map<String, Object> param) {
        ResVO result = new ResVO();
        try {
            LikeTreeRewardInsVo getData = likeTreeEvent.getLikeTreeRewardIns(param);

            switch (getData.getS_return()) {
                case -3: result.setResVO(ResMessage.C30216.getCode(), ResMessage.C30216.getCodeNM(), null); break;
                case -2: result.setResVO(ResMessage.C30217.getCode(), ResMessage.C30217.getCodeNM(), null); break;
                case -1: result.setResVO(ResMessage.C30218.getCode(), ResMessage.C30218.getCodeNM(), getData); break;
                case 1: result.setSuccessResVO(getData); break;
                default: result.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), null);
            }
        } catch (Exception e) {
            log.error("LikeTreeService / getLikeTreeRewardIns => {}", e);
            result.setFailResVO();
        }

        return result;
    }
}
