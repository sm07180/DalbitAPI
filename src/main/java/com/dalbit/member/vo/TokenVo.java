package com.dalbit.member.vo;

import com.dalbit.common.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenVo extends BaseVo {

    public TokenVo(){}

    public TokenVo(String authToken, String memNo, boolean isLogin){
        setAuthToken(authToken);
        setMemNo(memNo);
        setLogin(isLogin);
    }

    private String authToken;
    private String memNo;
    private boolean isLogin;
}
