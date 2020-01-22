package com.dalbit.member.service;



import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.dao.ProfileDao;
import com.dalbit.member.vo.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
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
    @Value("${server.photo.url}")
    private String SERVER_PHOTO_URL;

    /**
     * 정보 조회
     */

    public String callMemberInfo(P_ProfileInfoVo pProfileInfo) {
        ProcedureVo procedureVo = new ProcedureVo(pProfileInfo);
        profileDao.callMemberInfo(procedureVo);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        P_ProfileInfoVo profileInfo = new Gson().fromJson(procedureVo.getExt(), P_ProfileInfoVo.class);
        ProfileInfoOutVo ProfileInfoOutVo = new ProfileInfoOutVo(profileInfo, pProfileInfo.getTarget_mem_no());

        String result;
        if(procedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기_성공, ProfileInfoOutVo)));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기_회원아님)));
        }else{
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기_대상아님)));
        }
        return result;
    }

    /**
     * 팬보드 등록하기
     */
    public String callMemberFanboardAdd(P_FanboardAddVo pFanboardAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanboardAddVo);
        profileDao.callMemberFanboardAdd(procedureVo);

        String result="";
        if(Status.팬보드_댓글달기성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기성공));

        }else if (Status.팬보드_댓글달기실패_스타회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_스타회원번호_회원아님, procedureVo.getData()));

        }else if (Status.팬보드_댓글달기실패_작성자회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_작성자회원번호_회원아님, procedureVo.getData()));

        }else if (Status.팬보드_댓글달기실패_잘못된댓글그룹번호.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_잘못된댓글그룹번호, procedureVo.getData()));

        }else if (Status.팬보드_댓글달기실패_depth값_오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_depth값_오류, procedureVo.getData()));
        }

        return result;
    }



    /**
     * 팬보드 목록조회
     */
    public String callMemberFanboardList(P_FanboardListVo pFanboardListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanboardListVo);
        List<P_FanboardListVo> fanboardVoList = profileDao.callMemberFanboardList(procedureVo);

        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(fanboardVoList)){
            procedureOutputVo = null;
        }else{
            List<FanboardVo> outVoList = new ArrayList<>();
            for (int i=0; i<fanboardVoList.size(); i++){
                //outVoList.add(new FanboardVo(fanboardVoList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }
        HashMap fanBoardList = new HashMap();
        fanBoardList.put("list", procedureOutputVo.getOutputBox());

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");


        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드조회성공, fanBoardList));
        }else if(Status.팬보드_댓글없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글없음));
        } else if(Status.팬보드_요청회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_요청회원번호_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_스타회원번호_회원아님));
        }

        return result;
    }


    /**
     * 팬보드 삭제하기
     */
    public String callMemberFanboardDelete(P_FanboardDeleteVo p_fanboardDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(p_fanboardDeleteVo);
        profileDao.callMemberFanboardDelete(procedureVo);

        String result = null;
        if(Integer.parseInt(procedureVo.getRet()) == 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status. 팬보드_댓글삭제성공));

        } else if(Status.팬보드_댓글삭제실패_스타회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_스타회원번호_회원아님));

        } else if(Status.팬보드_댓글삭제실패_삭제자회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_삭제자회원번호_회원아님));

        } else if(Status.팬보드_댓글삭제실패_댓글인덱스번호_잘못된번호.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_요청인덱스번호_스타회원번호가다름));

        } else if(Status.팬보드_댓글삭제실패_요청인덱스번호_스타회원번호가다름.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_요청인덱스번호_스타회원번호가다름));

        } else if(Status.팬보드_댓글삭제실패_이미삭제됨.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_이미삭제됨));

        } else if(Status.팬보드_댓글삭제실패_삭제권한없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_삭제권한없음));
        }

        return result;
    }



    /**
     * 팬보드 대댓글 조회하기
     */
    public String callMemberFanboardReply(P_FanboardReplyVo p_FanboardReplyVo) {
        ProcedureVo procedureVo = new ProcedureVo(p_FanboardReplyVo);
        List<P_FanboardReplyVo> fanboardVoReplyList = profileDao.callMemberFanboardReply(procedureVo);


        ProcedureOutputVo procedureOutputVo;
        if(DalbitUtil.isEmpty(fanboardVoReplyList)){
            procedureOutputVo = null;
        }else{
            List<FanboardReplyOutVo> outVoList = new ArrayList<>();
            for (int i=0; i<fanboardVoReplyList.size(); i++){
                outVoList.add(new FanboardReplyOutVo(fanboardVoReplyList.get(i)));
            }
            procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        }
        HashMap fanboardReplyList = new HashMap();
        fanboardReplyList.put("list", procedureOutputVo.getOutputBox());

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result="";

        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_대댓글조회성공, fanboardReplyList));
        } else if(Status.팬보드_대댓글조회실패_대댓글없음.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_대댓글조회실패_대댓글없음, procedureOutputVo.getData()));
        } else if(Status.팬보드_대댓글조회실패_요청회원번호_회원아님.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson((new JsonOutputVo(Status.팬보드_대댓글조회실패_요청회원번호_회원아님, procedureOutputVo.getData())));
        } else if(Status.팬보드_대댓글조회실패_스타회원번호_회원아님.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson((new JsonOutputVo(Status.팬보드_대댓글조회실패_스타회원번호_회원아님, procedureOutputVo.getData())));
        }

        return result;
        }


}
