package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipMainSubjectTop3Vo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipMainSubjectTop3ListVo {

    public P_ClipMainSubjectTop3ListVo(){}
    public P_ClipMainSubjectTop3ListVo(ClipMainSubjectTop3Vo clipMainSubjectTop3Vo, HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setSubjectType(clipMainSubjectTop3Vo.getSubjectType());
    }

    /* Input */
    private int memLogin;
    private String mem_no;
    private String subjectType;

    /* Output */
    private int rank;
    private String cast_no;
    private String image_background;
    private String title;
    private String nickName;
    private String subject_type;
}
