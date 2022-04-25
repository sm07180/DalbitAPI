package com.dalbit.clip.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipMainLatestListVo extends P_ApiVo {

    public P_ClipMainLatestListVo(){}
    public P_ClipMainLatestListVo(HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        int listCnt = DalbitUtil.isEmpty(request.getParameter("listCnt")) ? 100 : Integer.parseInt(request.getParameter("listCnt"));
        setListCnt(listCnt);
    }

    /* Input */
    private int memLogin;
    private String mem_no;
    private int listCnt;

    /* Output */
    private String cast_no;
    private String image_background;
    private String title;
    private String nickName;
    private String subject_type;
    private String filePlayTime;
    private String memSex;
    private int badge_newdj;                //신입배지
    private int badge_special;              //스페셜배지
    private int count_play;
    private int count_good;
    private int replyCnt;
    
}
