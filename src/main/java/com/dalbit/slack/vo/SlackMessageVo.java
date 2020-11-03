package com.dalbit.slack.vo;

import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlackMessageVo {
    private String text;
    private String channel;
    private String botName;
    private String iconEmoji;
    private String iconUrl;

    public SlackMessageVo(String text, String channel, String botName, String iconEmoji, String iconUrl) {
        this.text = text;
        this.channel = channel;
        this.botName = botName;
        if (!DalbitUtil.isEmpty(iconEmoji)) {
            this.iconEmoji = iconEmoji;
        }
        if (!DalbitUtil.isEmpty(iconUrl)) {
            this.iconUrl = iconUrl;
        }
    }
}

