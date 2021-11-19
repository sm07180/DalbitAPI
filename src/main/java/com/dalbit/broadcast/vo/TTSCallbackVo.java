package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TTSCallbackVo {
    private String speak_url;
    private String play_id;
    private String audio_url_no_redirect;
}
