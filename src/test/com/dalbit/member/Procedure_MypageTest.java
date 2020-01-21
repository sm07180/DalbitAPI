package com.dalbit.member;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles({"local"})
public class Procedure_MypageTest {

    @Autowired
    private MypageService mypageService;
    @Autowired
    private GsonUtil gsonUtil;

    @Test
    public void 프로필편집(){

        /*String result = mypageService.callProfileEdit(P_ProfileEditVo.builder().build());
        log.debug("프로필편집 결과 : {}", result);*/
    }

    @Test
    public void 회원팬등록(){

        /*String result = mypageService.callFanstarInsert(P_FanstarInsertVo.builder().build());
        log.debug("회원팬등록 결과 : {}", result);*/
    }

    @Test
    public void 회원팬해제() {

        /*String result = mypageService.callFanstarDelete(P_FanstarDeleteVo.builder().build());
        log.debug("회원팬해제 결과 : {}", result);*/
    }
}
