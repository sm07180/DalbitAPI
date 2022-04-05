package com.dalbit.event.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.event.service.WelcomeEventService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/event/welcome")
@Scope("prototype")
@Slf4j
public class WelcomeEventController {

    @Autowired
    WelcomeEventService welcomeEventService;

    @Autowired
    GsonUtil gsonUtil;
    /**********************************************************************************************
     * @Method 설명 : 웰컴 DJ 정보 가져오기
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    @GetMapping("/djInfo")
    public ResVO getWelcomeDjInfo(HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);

            if (memNo != null) {
                result = welcomeEventService.getWelcomeUserInfo(memNo, "1");
            } else {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            }
        } catch (Exception e) {
            log.error("WelcomeEventController / getWelcomeDjInfo => {}", e);
            result.setFailResVO();
        }
        return result;
    }


    /**********************************************************************************************
     * @Method 설명 : 웰컴 청취자 정보 가져오기
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter :
     * @Return :
     **********************************************************************************************/
    @GetMapping("/userInfo")
    public ResVO getWelcomeUserInfo(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);

            if (memNo != null) {
                result = welcomeEventService.getWelcomeUserInfo(memNo, "2");
            } else {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            }
        } catch (Exception e) {
            log.error("WelcomeEventController / getWelcomeUserInfo => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 웹컴 조건 인증체크 프로시저 (페이지 접근시 호출하여 자격을 검증해야함.(데이터 처리때문에 페이지 접근시 돌려야함))
     * @작성일 : 2021-12-24
     * @작성자 : 이정혁
     * @변경이력 :
     * @Parameter : memNo 		BIGINT		-- 회원번호
     *             memSlct 	BIGINT		-- 회원[1:dj, 2:청취자]
     * @Return :
     **********************************************************************************************/
    @GetMapping("/authInfo")
    public ResVO getWelcomeEventQualityInfo(HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);

            if (memNo != null) {
                result = welcomeEventService.getWelcomeEventQualityInfo(memNo);//
            } else {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            }

        } catch (Exception e) {
            log.error("WelcomeEventController / getWelcomeUserInfo => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    @PostMapping("/reqGift/{giftSlct}")
    public ResVO putWelcomeGift(@RequestParam Map<String, Object> params, @PathVariable("giftSlct") String giftSlct, HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            String memNo = MemberVo.getMyMemNo(request);

            params.put("giftSlct", giftSlct);

            if (memNo == null) {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            } else if (!params.containsKey("memPhone")) {
                result.setResVO(ResMessage.C10002.getCode(), ResMessage.C10002.getCodeNM(), null);
            } else if (!params.containsKey("giftStepNo")) {
                result.setResVO(ResMessage.C10002.getCode(), ResMessage.C10002.getCodeNM(), null);
            } else if (!params.containsKey("giftCode")) {
                result.setResVO(ResMessage.C10002.getCode(), ResMessage.C10002.getCodeNM(), null);
            } else if (!params.containsKey("giftTheMonth")) {
                result.setResVO(ResMessage.C10002.getCode(), ResMessage.C10002.getCodeNM(), null);
            } else if (!params.containsKey("giftDalCnt")) {
                result.setResVO(ResMessage.C10002.getCode(), ResMessage.C10002.getCodeNM(), null);
            } else if (!params.containsKey("giftCont")) {
                result.setResVO(ResMessage.C10002.getCode(), ResMessage.C10002.getCodeNM(), null);
            } else if (!params.containsKey("giftName")) {
                result.setResVO(ResMessage.C10002.getCode(), ResMessage.C10002.getCodeNM(), null);
            } else if (!params.containsKey("giftOrdNo")) {
                result.setResVO(ResMessage.C10002.getCode(), ResMessage.C10002.getCodeNM(), null);
            } else {
                params.put("memNo", memNo);
                result = welcomeEventService.putWelcomeGift(memNo, params);
            }
        } catch (Exception e) {
            log.error("WelcomeEventController / putWelcomeGift => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 웹컴페이지 접속 체크값 수정 (방송방에서 버튼 하루 한번만 표시용, 버튼을 클릭시 이 API를 호출합니다)
     * @작성일 : 2022-01-21
     * @작성자 : 박용훈
     * @변경이력 :
     * @Parameter : memNo       BIGINT
     * @Return :    s_return		INT		--   -1: 이상, 0: 에러, 1:정상
     **********************************************************************************************/
    @PostMapping("/chkInfoUpd")
    public String putWelcomeDayConfirmChecker(HttpServletRequest request){
        String result;
        try {
            String memNo = MemberVo.getMyMemNo(request);

            if(memNo != null){
                Integer dbResultCode = welcomeEventService.putWelcomeDayConfirmChecker(memNo);

                if(dbResultCode == 1) { // DB result값이 정상
                    result = gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_성공, dbResultCode));

                } else {
                    result = gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
                }
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
            }

        }catch(Exception e){
            log.error("putWelcomeDayConfirmChecker => {}", e);
            result = gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_실패));
        }
        result = gsonUtil.toJson(new JsonOutputVo(Status.공통_기본_성공, 1));
        return result;
    }
}
