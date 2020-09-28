package com.dalbit.clip.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipMainPopListVo {

    public P_ClipMainPopListVo(){}
    public P_ClipMainPopListVo(HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
    }

    /* Input */
    private int memLogin;
    private String mem_no;

    /* Output */
    private String cast_no;
    private String image_background;
    private String nickName;
    private String subject_type;
    private String title;
}
