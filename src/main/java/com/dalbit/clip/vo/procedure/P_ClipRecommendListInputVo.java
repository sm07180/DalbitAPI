package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipRecommendListInputVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Getter
@Setter
public class P_ClipRecommendListInputVo extends P_ApiVo {

    public P_ClipRecommendListInputVo() {
    }

    public P_ClipRecommendListInputVo(ClipRecommendListInputVo clipRecommendListInputVo, HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
        setMemLogin(clipRecommendListInputVo.getIsLogin() == false ? 0 : 1);
        setClickYn(clipRecommendListInputVo.getIsClick() == null || clipRecommendListInputVo.getIsClick() == false ? 0 : 1);
        setRecDate(clipRecommendListInputVo.getRecDate());
        if(DalbitUtil.isEmpty(this.recDate)){
            setRecDate("2021-04-12");
        }else{
            Calendar reqDate = Calendar.getInstance();
            Calendar maxDate = Calendar.getInstance();
            maxDate.set(Calendar.YEAR, 2021);
            maxDate.set(Calendar.MONTH, Calendar.APRIL);
            maxDate.set(Calendar.DAY_OF_MONTH, 12);
            String[] reqD = this.recDate.split("-");
            try {
                reqDate.set(Calendar.YEAR, Integer.parseInt(reqD[0]));
                reqDate.set(Calendar.MONTH, Integer.parseInt(reqD[1]) - 1);
                reqDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(reqD[2]));
                if (reqDate.after(maxDate)) {
                    setRecDate("2021-04-12");
                }
            }catch(Exception e){
                setRecDate("2021-04-12");
            }
        }
    }

    private String mem_no;
    private Integer memLogin;  // 로그인 : 1, 비로그인 : 0
    private String recDate;
    private Integer clickYn;    // 클릭 : 1, 클릭x : 0 (조회수 카운트를 위한 클릭 여부)

}
