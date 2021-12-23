package com.dalbit.event.controller;

import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.event.service.LikeTreeService;
import com.dalbit.member.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/event/likeTree")
@Scope("prototype")
@Slf4j
public class LikeTreeController {

    @Autowired
    private LikeTreeService likeTreeService;


    /**********************************************************************************************
     * @Method 설명 : 좋아요 트리 사연 장식리스트
     * @작성일 : 2021-12-16
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    @GetMapping("/decoList")
    public ResVO getLikeTreeDecoList(@RequestParam(value = "seqNo") Integer seqNo,
                                     @RequestParam(value = "pageNo") Integer pageNo,
                                     @RequestParam(value = "pagePerCnt") Integer pagePerCnt) {
        ResVO result = new ResVO();

        try {
            result = likeTreeService.getLikeTreeDecoList(seqNo, pageNo, pagePerCnt);

        } catch (Exception e) {
            log.error("LikeTreeController / getLikeTreeDecoList => {}", e);
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
    @GetMapping("/mainList")
    public ResVO getLikeTreeMainList(HttpServletRequest request) {
        ResVO result = new ResVO();
        try {
            String memNo = MemberVo.getMyMemNo(request);

            if (memNo == null) {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            } else {
                result = likeTreeService.getLikeTreeMainList(memNo);
            }
        } catch (Exception e) {
            log.error("LikeTreeController / getLikeTreeMainList => {}", e);
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
    @GetMapping("/storyList")
    public ResVO getLikeTreeStoryList(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(value = "pagePerCnt", defaultValue = "30") Integer pagePerCnt) {
        ResVO result = new ResVO();

        try {
            result = likeTreeService.getLikeTreeStoryList(pageNo, pagePerCnt);
        } catch (Exception e) {
            log.error("LikeTreeController / getLikeTreeStoryList => {}", e);
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
    @PostMapping("/registStory")
    public ResVO likeTreeStoryIns(@RequestParam Map<String, Object> param, HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);

            if (memNo == null) {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            } else {
                param.put("memNo", memNo);
                result = likeTreeService.likeTreeStoryIns(param);
            }
        } catch (Exception e) {
            log.error("LikeTreeController / likeTreeStoryIns => {}", e);
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
    @PostMapping("/updateStory")
    public ResVO likeTreeStoryUpd(@RequestParam Map<String, Object> param, HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);

            if (memNo == null) {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            } else {
                param.put("memNo", memNo);
                result = likeTreeService.likeTreeStoryUpd(param);
            }
        } catch (Exception e) {
            log.error("LikeTreeController / likeTreeStoryUpd => {}", e);
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
    @PostMapping("/deleteStory")
    public ResVO likeTreeStoryDel(@RequestParam Map<String, Object> param, HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);

            // 서비스는 관리자가 삭제 못함
            param.put("delChrgrNo", "");
            param.put("delSlct", 1);

            if (memNo == null) {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            } else {
                param.put("memNo", memNo);
                result = likeTreeService.likeTreeStoryDel(param);
            }
        } catch (Exception e) {
            log.error("LikeTreeController / likeTreeStoryDel => {}", e);
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
    @PostMapping("/reportStory")
    public ResVO likeTreeStoryRptIns(@RequestParam Map<String, Object> param, HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);

            if (memNo == null) {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            } else {
                param.put("memNo", memNo);
                result = likeTreeService.likeTreeStoryRptIns(param);
            }
        } catch (Exception e) {
            log.error("LikeTreeController / likeTreeStoryRptIns => {}", e);
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
    @GetMapping("/rankList")
    public ResVO getLikeTreeRankList(@RequestParam(value = "seqNo", defaultValue = "1") Integer seqNo,
                                     @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pagePerCnt", defaultValue = "30") Integer pagePerCnt) {
        ResVO result = new ResVO();

        try {
            result = likeTreeService.getLikeTreeRankList(seqNo, pageNo, pagePerCnt);
        } catch (Exception e) {
            log.error("LikeTreeController / getLikeTreeRankList => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    @GetMapping("/rankUserInfo")
    public ResVO getLikeTreeRankUserInfo(@RequestParam(value = "seqNo", defaultValue = "1") Integer seqNo,
                                     @RequestParam(value = "memNo") String memNo) {
        ResVO result = new ResVO();

        try {
            result = likeTreeService.getLikeTreeRankUserInfo(seqNo, memNo);
        } catch (Exception e) {
            log.error("LikeTreeController / getLikeTreeRankUserInfo => {}", e);
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
    @PostMapping("/reward/ins")
    public ResVO getLikeTreeRewardIns(@RequestParam Map<String, Object> param, HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime startDay = LocalDateTime.parse("2022-01-07T00:00:00.000");
            LocalDateTime endDay = LocalDateTime.parse("2022-01-09T00:00:00.000");


            // 이벤트 보상 기간 체크
            if (!(startDay.isBefore(today) && endDay.isAfter(today))) {
                result.setResVO(ResMessage.C30003.getCode(), ResMessage.C30003.getCodeNM(), null);
                return result;
            }

            if (memNo == null) {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            } else {
                param.put("memNo", memNo);
                result = likeTreeService.getLikeTreeRewardIns(param);
            }
        } catch (Exception e) {
            log.error("LikeTreeController / getLikeTreeRewardIns => {}", e);
            result.setFailResVO();
        }

        return result;
    }
}
