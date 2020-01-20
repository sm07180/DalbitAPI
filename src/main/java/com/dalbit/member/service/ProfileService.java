package com.dalbit.member.service;


import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.dao.ProfileDao;
import com.dalbit.member.vo.FanboardVo;
import com.dalbit.member.vo.P_FanboardAddVo;
import com.dalbit.member.vo.P_FanboardListVo;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ProfileService {
    @Autowired
    ProfileDao profileDao;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;


    /**
     * 정보 조회
     */
//    public String callMemberInfoView(ProcedureVo procedurevo) {
//        return "";
//    }
//
//



    /**
     * 팬보드 등록하기
     */
    public String callMemberFanboardAdd(P_FanboardAddVo pFanboardAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanboardAddVo);
        profileDao.callMemberFanboardAdd(procedureVo);

        String result;
        if(Status.팬보드_댓글달기성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기성공));

        }else if (Status.팬보드_댓글달기실패_스타회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_스타회원번호_회원아님, procedureVo.getData()));

        }else if (Status.팬보드_댓글달기실패_작성자회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_작성자회원번호_회원아님, procedureVo.getData()));

        }else if (Status.팬보드_댓글달기실패_잘못된댓글그룹번호.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_잘못된댓글그룹번호, procedureVo.getData()));

        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_depth값_오류, procedureVo.getData()));
        }

        return result;
    }




    /**
     * 팬보드 목록조회
     */
    public String callMemberFanboardList(P_FanboardListVo pFanboardListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanboardListVo);
        List<FanboardVo> fanboardVoList = profileDao.callMemberFanboardList(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드조회성공, fanboardVoList));

        }else if(Status.팬보드_댓글없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글없음));

        } else if(Status.팬보드_요청회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_요청회원번호_회원아님, procedureVo.getData()));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_스타회원번호_회원아님, procedureVo.getData()));
        }



        return result;
    }


    /**
     * 팬보드 삭제하기
     */
    public String callMemberFanboardDelete(ProcedureVo procedureVo) {

        return "";
    }



    /**
     * 팬보드 대댓글 조회하기
     */
    public String callMemberFanboardReply(ProcedureVo procedureVo) {

        return "";
    }

}
