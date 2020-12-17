package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipRecommendListInputVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_ClipRecommendListInputVo extends P_ApiVo {

    public P_ClipRecommendListInputVo() {
    }

    public P_ClipRecommendListInputVo(ClipRecommendListInputVo clipRecommendListInputVo, HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
        setMemLogin(clipRecommendListInputVo.getIsLogin() == false ? 0 : 1);
        setRecDate(clipRecommendListInputVo.getRecDate());
        setClickYn(clipRecommendListInputVo.getIsClick() == null || clipRecommendListInputVo.getIsClick() == false ? 0 : 1);
    }

    private String mem_no;
    private Integer memLogin;  // 로그인 : 1, 비로그인 : 0
    private String recDate;
    private Integer clickYn;    // 클릭 : 1, 클릭x : 0 (조회수 카운트를 위한 클릭 여부)

}
