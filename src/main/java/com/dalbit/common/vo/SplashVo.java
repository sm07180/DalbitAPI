package com.dalbit.common.vo;

import com.dalbit.common.code.Status;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SplashVo extends JsonOutputVo {
    private Object member;

    public SplashVo(Status status, Object data, Object member){
        setStatus(status);
        setData(data);
        setTimestamp(DalbitUtil.getTimeStamp());
        setMember(member);
    }
}
