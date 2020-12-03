package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.main.vo.request.FaqDetailVo;
import com.dalbit.main.vo.request.NoticeDetailVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_FaqDetailVo extends P_ApiVo {

    private int faqIdx;

    public P_FaqDetailVo(){}
    public P_FaqDetailVo(FaqDetailVo faqDetailVo){
        setFaqIdx(faqDetailVo.getFaqIdx());
    }
}
