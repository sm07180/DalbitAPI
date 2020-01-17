package com.dalbit.member.vo;

import com.dalbit.common.vo.BaseVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Date;
import java.util.HashMap;

@Getter @Setter
public class MypageVo extends BaseVo {

    private static final long serialVersionUID = 1L;

    public static MypageVo getUserInfo() {
        //SecurityUserVo user = (SecurityUserVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HashMap MypageInfoMap = (HashMap)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MypageVo sessionMypageVo = new Gson().fromJson(DalbitUtil.getStringMap(MypageInfoMap, "MypageInfo"), MypageVo.class);

        return sessionMypageVo;
    }


    private String mem_no;
    private String memSex;
    private String nickName;
    private String name;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private String profileImage;
    private String backgroundImage;
    private String profileMsg;

    private String fanMemNo;
    private String starMemNo;



}
