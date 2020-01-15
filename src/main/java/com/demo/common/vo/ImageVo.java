package com.demo.common.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class ImageVo {
    @Value("${server.photo.url}")
    private String photoServer;

    private String url;
    private String path;
    private String thumbs1;
    private String thumbs2;
    private String thumbs3;
    private String thumbs4;
    private String thumbs5;
    private String thumbs6;
    private String thumbs7;
    private String thumbs8;
    private String thumbs9;

    private void setPath(String path){
        this.path = path;
        this.url = photoServer + this.path;
        this.thumbs1 = url + "?20x20";
        this.thumbs2 = url + "?30x30";
        this.thumbs3 = url + "?50x50";
        this.thumbs4 = url + "?100x100";
        this.thumbs5 = url + "?100x120";
        this.thumbs6 = url + "?120x120";
        this.thumbs7 = url + "?160x120";
        this.thumbs8 = url + "?200x150";
        this.thumbs9 = url + "?200x200";
    }
}
