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
@RequestMapping("/event/welcom")
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
                result = welcomeEventService.getWelcomeDjInfo(memNo);
            } else {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
                result = welcomeEventService.getWelcomeDjInfo("11631261112504");
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
                result = welcomeEventService.getWelcomeUserInfo(memNo);
            } else {
                result.setResVO(ResMessage.C10001.getCode(), ResMessage.C10001.getCodeNM(), null);
                result = welcomeEventService.getWelcomeUserInfo("11631261112504");
            }
        } catch (Exception e) {
            log.error("WelcomeEventController / getWelcomeUserInfo => {}", e);
            result.setFailResVO();
        }
        return result;
    }

    /*@GetMapping("/authInfo")
    public ResVO getItemAuthInfo(@RequestParam(value = "memPhone", defaultValue = "") String memPhone, HttpServletRequest request) {
        ResVO result = new ResVO();

        try {
            result = welcomeEventService.getWelcomeUserInfo("11631261112504");

        } catch (Exception e) {
            log.error("WelcomeEventController / getWelcomeUserInfo => {}", e);
            result.setFailResVO();
        }
        return result;
    }
*/
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
                result = welcomeEventService.getWelcomeEventQualityInfo("11631261112504");//
            }

        } catch (Exception e) {
            log.error("WelcomeEventController / getWelcomeUserInfo => {}", e);
            result.setFailResVO();
        }
        return result;
    }
}
