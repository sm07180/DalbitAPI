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
    public TokenVo(String authToken, String memNo, boolean isLogin, int isAdmin){
        setAuthToken(authToken);
        setMemNo(memNo);
        setLogin(isLogin);
        setAdmin(isAdmin > 0);
    }
    public TokenVo(String authToken, String memNo, boolean isLogin, boolean isAdmin){
        setAuthToken(authToken);
        setMemNo(memNo);
        setLogin(isLogin);
        setAdmin(isAdmin);
    }

    private String authToken;
    private String memNo;
    private boolean isLogin;
    private boolean isAdmin = false;
}
