package com.dalbit.member.service;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MemberDao;
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

import java.util.HashMap;

@Slf4j
@Service
@Transactional
public class MemberService {

    @Autowired
    MemberDao memberDao;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;

    @Value("${server.photo.url}")
    private String SERVER_PHOTO_URL;

    public ProcedureVo callMemberLogin(P_LoginVo pLoginVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
        memberDao.callMemberLogin(procedureVo);
        return procedureVo;
    }

    /**
     * 회원 가입
     */
    public String signup(P_JoinVo pLoginVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
        memberDao.callMemberJoin(procedureVo);

        log.info("sp_member_join: {}", procedureVo.getRet());
        log.info("sp_member_join: {}", procedureVo.getExt());

        log.debug("회원가입 결과 : {}", procedureVo.toString());

        String result;
        if(Status.회원가입성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입성공));

        }else if (Status.회원가입실패_중복가입.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_중복가입, procedureVo.getData()));

        }else if (Status.회원가입실패_닉네임중복.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_닉네임중복, procedureVo.getData()));

        }else if (Status.회원가입실패_파라메터오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류, procedureVo.getData()));

        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입오류, procedureVo.getData()));
        }

        return result;
    }

    /**
     * 닉네임 중복체크
     */
    public String callNickNameCheck(ProcedureVo procedureVo) {
        memberDao.callNickNameCheck(procedureVo);

        log.debug("닉네임중복체크 결과 : {}", procedureVo.toString());

        String result = "";
        if(Status.닉네임중복.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.닉네임중복, procedureVo.getData()));

        }else if(Status.닉네임사용가능.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.닉네임사용가능, procedureVo.getData()));
        }
        return result;
    }

    /**
     * 비밀번호 변경
     */
    public String callChangePassword(P_ChangePasswordVo pChangePasswordVo){
        ProcedureVo procedureVo = new ProcedureVo(pChangePasswordVo);
        memberDao.callChangePassword(procedureVo);
        String result;

        if(procedureVo.getRet().equals(Status.비밀번호변경성공.getMessageCode())) {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.비밀번호변경성공, procedureVo.getData())));
        } else {
            result = gsonUtil.toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.비밀번호변경실패_회원아님)));
        }
        return result;
    }

    /**
     * 회원 정보 보기
     */
    public String getMemberInfo(P_InfoVo pInfoVo) throws GlobalException{
        ProcedureVo procedureVo = new ProcedureVo(pInfoVo);
        memberDao.callMemberInfoView(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.debug("회원정보보기 결과 : {}", pInfoVo.toString());


        String result = "";
        if(Status.회원정보보기성공.getMessageCode().equals(procedureVo.getRet())) {
            //result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기성공, procedureVo.getData())));
            HashMap map = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            MemberVo memberVo = new MemberVo(); //new Gson().fromJson(procedureVo.getExt(), MemberVo.class);

            memberVo.setMem_no(MemberVo.getUserInfo().getMem_no());
            memberVo.setMem_nick(DalbitUtil.getStringMap(map, "nickName"));
            memberVo.setMem_sex(DalbitUtil.getStringMap(map, "memSex"));
            memberVo.setAge(DalbitUtil.getIntMap(map, "age"));
            memberVo.setMem_id(DalbitUtil.getStringMap(map, "memId"));
            memberVo.setLevel(DalbitUtil.getIntMap(map, "level"));
            memberVo.setFan_count(DalbitUtil.getIntMap(map, "fanCount"));
            memberVo.setStar_count(DalbitUtil.getIntMap(map, "starCount"));
            memberVo.setEnable_fan(DalbitUtil.getBooleanMap(map, "enableFan"));

            memberVo.setBackground_image(new ImageVo(DalbitUtil.getStringMap(map, "backgroundImage"), SERVER_PHOTO_URL));
            memberVo.setProfile_image(new ImageVo(DalbitUtil.getStringMap(map, "profileImage"), DalbitUtil.getStringMap(map, "memSex"), SERVER_PHOTO_URL));

            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기성공, memberVo));

        }else if(Status.회원정보_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            throw new GlobalException(Status.회원정보_회원아님, procedureVo.getData());
        }else if(Status.회원정보_대상회원아님.getMessageCode().equals(procedureVo.getRet())) {
            throw new GlobalException(Status.회원정보_대상회원아님, procedureVo.getData());
        }else {
            throw new GlobalException(Status.회원정보보기실패, procedureVo.getData());
        }

        return result;
    }
}
