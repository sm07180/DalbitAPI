package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.RubyVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_RubyVo {

    public P_RubyVo(){}
    public P_RubyVo(RubyVo rubyVo){
        setMem_no(MemberVo.getMyMemNo());
        setGifted_mem_no(rubyVo.getMemNo());
        setRuby(rubyVo.getDal());
    }

    private String mem_no;
    private String gifted_mem_no;
    private int ruby;
}
