package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TTSSpeakVo {
    public TTSSpeakVo() {}
    public TTSSpeakVo(String ttsText, String actorId) {
        this.ttsText = ttsText;
        this.actorId = actorId;
    }

    private String ttsText = "";
    private String lang = "auto";
    private String actorId = "";
    private int maxSeconds = 15;
}
