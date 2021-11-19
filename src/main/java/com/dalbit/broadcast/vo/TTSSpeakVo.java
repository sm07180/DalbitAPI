package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TTSSpeakVo {
    private String text;
    private String lang = "auto";
    private String actor_id;
    private int max_seconds = 10;
}
