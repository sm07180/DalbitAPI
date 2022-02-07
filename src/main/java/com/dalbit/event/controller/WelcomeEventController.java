package com.dalbit.event.controller;

import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.event.service.WelcomeEventService;
import com.dalbit.member.vo.MemberVo;
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
    public ResVO putWelcomeDayConfirmChecker(HttpServletRequest request){
        ResVO result = new ResVO();
        try {
            String memNo = MemberVo.getMyMemNo(request);

            if(memNo != null){
                Integer dbResultCode = welcomeEventService.putWelcomeDayConfirmChecker(memNo);

                if(dbResultCode == 1) { // DB result값이 정상
                    result.setResVO(ResMessage.C00000.getCode(), ResMessage.C00000.getCodeNM(), dbResultCode);
                } else if(dbResultCode == -1){ // 이미 오늘 봤어요 (클라이언트 로직 오류)
                    result.setResVO(ResMessage.C39006.getCode(), ResMessage.C39006.getCodeNM(), dbResultCode);
                } else { // DB result값이 에러
                    result.setResVO(ResMessage.C99997.getCode(), ResMessage.C99997.getCodeNM(), dbResultCode);
                }
            } else {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
            }

        }catch(Exception e){
            log.error("putWelcomeDayConfirmChecker => {}", e);
            result.setResVO(ResMessage.C99998.getCode(), ResMessage.C99998.getCodeNM(), null);
        }

        return result;
    }
}