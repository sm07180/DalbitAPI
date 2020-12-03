package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ProfileEditVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class P_ProfileEditVo extends P_ApiVo {

    public P_ProfileEditVo(){}
    public P_ProfileEditVo(ProfileEditVo profileEditVo, HttpServletRequest request){
        setMem_no(new MemberVo().getMyMemNo(request));
        setMemSex(profileEditVo.getGender());
        setNickName(profileEditVo.getNickNm().trim().replace(" "," "));
        /*setName(profileEditVo.getName());
        if(!DalbitUtil.isEmpty(profileEditVo.getName()) && profileEditVo.getName().length() > 50){
            setName(profileEditVo.getName().substring(0, 49));
        }*/
        setBirthDate(String.valueOf(LocalDate.parse(profileEditVo.getBirth(), DateTimeFormatter.BASIC_ISO_DATE)));
        setProfileImage(profileEditVo.getProfImg());
        setProfileImageGrade(DalbitUtil.isStringToNumber(profileEditVo.getProfImgRacy()));
        setProfileMsg(profileEditVo.getProfMsg());
        setProfImgDel(profileEditVo.getProfImgDel());
    }

    private String mem_no;
    private String memSex;              		   //성별 (m/f)
    private String nickName;                       //닉네임
    //private String name;                           //이름
    private String birthDate;
    private String profileImage;                   //프로필이미지 패스
    private int profileImageGrade;                 //프로필이미지 구글 선정성
    private String profileMsg;                     //메시지
    private String profImgDel;

}
