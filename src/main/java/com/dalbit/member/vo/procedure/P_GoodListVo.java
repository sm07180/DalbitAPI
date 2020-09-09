package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.GoodListVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_GoodListVo {
    /* Input */
    private String mem_no;
    private String target_mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int good;
    private String mem_nick;
    private String mem_sex;
    private String image_profile;
    private boolean isFan;

    public P_GoodListVo(){}
    public P_GoodListVo(GoodListVo goodListVo, HttpServletRequest request){
        this.mem_no = MemberVo.getMyMemNo(request);
        this.target_mem_no = goodListVo.getMemNo();
        this.pageNo = goodListVo.getPage();
        this.pageCnt = goodListVo.getRecords();
    }
}
