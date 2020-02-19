package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShortCutVo {

    private ShortCutSubVo[] data;

    @Getter @Setter
    public class ShortCutSubVo {

        private String order;
        private String text;
        private String isOn;

    }
}
