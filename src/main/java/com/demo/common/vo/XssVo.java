package com.demo.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class XssVo extends BaseVo {

    public XssVo(String content) {
        this.content = content;
    }

    private String content;


}
