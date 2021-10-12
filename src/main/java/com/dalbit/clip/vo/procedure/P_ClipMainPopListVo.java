package com.dalbit.clip.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipMainPopListVo extends P_ApiVo {

    public P_ClipMainPopListVo(){}
    public P_ClipMainPopListVo(HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        int listCnt = DalbitUtil.isEmpty(request.getParameter("listCnt")) ? 20 : Integer.parseInt(request.getParameter("listCnt"));
        setListCnt(listCnt);
    }

    /* Input */
    private int memLogin;
    private String mem_no;
    private int listCnt;

    /* Output */
    private String cast_no;
    private String image_background;
    private String nickName;
    private String subject_type;
    private String title;
    private String filePlayTime;
    private String memSex;
    private int replyCnt;
    private int count_good;
    private int badge_special;
    private int badge_newdj;
    private int count_play;
    private int isConDj;

}
