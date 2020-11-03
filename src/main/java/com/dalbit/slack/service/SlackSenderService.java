package com.dalbit.slack.service;

import com.dalbit.main.vo.procedure.P_QnaVo;
import com.dalbit.slack.vo.SlackMessageVo;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Slf4j
@Service
public class SlackSenderService {

    @Value("${notification.slack.enabled}")
    private boolean slackEnabled;
    @Value("${notification.slack.webhook.url}")
    private String webhookUrl;
    @Value("${notification.slack.channel}")
    private String channel;
    @Value("${notification.slack.botName}")
    private String botName;
    @Value("${notification.slack.icon.emoji}")
    private String iconEmoji;
    @Value("${notification.slack.icon.url}")
    private String iconUrl;
    @Value("${server.mobile.url}")
    private String mobileUrl;

    @Autowired
    private GsonUtil gsonUtil;

    public void sendSlack(String contents) {
        if (slackEnabled) {
            try {
                // create slack Message
                SlackMessageVo slackMessage = new SlackMessageVo(contents, channel, botName, iconEmoji, iconUrl);
                String payload = gsonUtil.toJson(slackMessage);

                RestTemplate restTemplate = new RestTemplate();
                //한글방지 인코딩 설정
                restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                // send the post request
                HttpEntity<String> entity = new HttpEntity<>(payload, headers);
                restTemplate.postForEntity(webhookUrl, entity, String.class);
            } catch (Exception e) {
                log.error("Unhandled Exception occurred while send slack. [Reason] : ", e);
            }
        }
    }

    public void sendQnaMessage(P_QnaVo pQnaVo){

        StringBuilder sb = new StringBuilder();
        sb.append("1:1 문의가 접수되었습니다.")
                .append(System.getProperty("line.separator"))
                .append("[제목] : ")
                .append(pQnaVo.getTitle())
                .append(System.getProperty("line.separator"))
                .append("[내용] : ")
                .append(pQnaVo.getContents())
                .append(System.getProperty("line.separator"))
                .append("[바로가기링크] : ")
                .append(mobileUrl)
                .append("/admin/question/detail?qnaIdx=")
                .append(pQnaVo.getQnaIdx());

        sendSlack(sb.toString());
    }
}
