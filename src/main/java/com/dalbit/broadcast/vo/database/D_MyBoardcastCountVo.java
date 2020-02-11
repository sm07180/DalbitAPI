package com.dalbit.broadcast.vo.database;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class D_MyBoardcastCountVo {

    public D_MyBoardcastCountVo(){}
    public D_MyBoardcastCountVo(String memNo, int auth, int state){
        setMemNo(memNo);
        setAuth(auth);
        setState(state);
    }

    private String memNo;
    private int auth;
    private int state;
}
