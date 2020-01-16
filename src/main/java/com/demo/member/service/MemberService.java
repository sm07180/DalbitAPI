package com.demo.member.service;

import com.demo.common.code.Status;
import com.demo.common.vo.ImageVo;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.exception.GlobalException;
import com.demo.member.dao.MemberDao;
import com.demo.member.vo.MemberVo;
import com.demo.member.vo.P_InfoVo;
import com.demo.member.vo.P_JoinVo;
import com.demo.member.vo.P_LoginVo;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

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

    public ProcedureVo callMemberJoin(P_JoinVo pLoginVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLoginVo);
        memberDao.callMemberJoin(procedureVo);
        return procedureVo;
    }

    public ProcedureVo callNickNameCheck(ProcedureVo procedureVo) {
        memberDao.callNickNameCheck(procedureVo);
        return procedureVo;
    }

    public ProcedureVo callChangePassword(ProcedureVo procedureVo){
        memberDao.callChangePassword(procedureVo);
        return procedureVo;
    }

    public ProcedureVo callProfileEdit(ProcedureVo procedureVo){
        return memberDao.callProfileEdit(procedureVo);
    }

    public ProcedureVo callFanstarInsert(ProcedureVo procedureVo) {
        return memberDao.callFanstarInsert(procedureVo);
    }

    public ProcedureVo callFanstarDelete(ProcedureVo procedureVo) {
        return memberDao.callFanstarDelete(procedureVo);
    }


    /**
     * 회원 정보 보기
     */
    public String callMemberInfoView(P_InfoVo pInfoVo) throws GlobalException{
        ProcedureVo procedureVo = new ProcedureVo(pInfoVo);
        memberDao.callMemberInfoView(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.debug("회원정보보기 결과 : {}", pInfoVo.toString());


        String result = "";
        if(Status.회원정보보기성공.getMessageCode().equals(procedureVo.getRet())) {
            //result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.회원정보보기성공, procedureVo.getData())));
            HashMap map = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            MemberVo memberVo = new MemberVo();//new Gson().fromJson(procedureVo.getExt(), MemberVo.class);

            memberVo.setMem_no(MemberVo.getUserInfo().getMem_no());
            memberVo.setNick_name(map.get("nickName").toString());
            memberVo.setMem_sex(map.get("memSex").toString());
            memberVo.setAge(Integer.valueOf(map.get("age").toString()));
            memberVo.setMem_id(map.get("memId").toString());
            memberVo.setLevel(Integer.valueOf(map.get("level").toString()));
            memberVo.setFan_count(Integer.valueOf(map.get("fanCount").toString()));
            memberVo.setStar_count(Integer.valueOf(map.get("starCount").toString()));
            memberVo.setEnable_fan(Boolean.valueOf(map.get("enableFan").toString()));

            ImageVo backgroundImage = new ImageVo();
            //backgroundImage.setUrl(map.get("backgroundImage").toString());
            backgroundImage.setPath(map.get("backgroundImage"), SERVER_PHOTO_URL);
            memberVo.setBackground_image(backgroundImage);

            ImageVo profileImage = new ImageVo();
            //profileImage.setUrl(map.get("profileImage").toString());
            profileImage.setPath(map.get("profileImage"), map.get("memSex").toString(), SERVER_PHOTO_URL);
            memberVo.setProfile_image(profileImage);

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
