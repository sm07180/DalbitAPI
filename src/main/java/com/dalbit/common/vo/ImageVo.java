package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageVo {

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

    public void setPath(Object path, String photoServerUrl){
        if(path != null){
            setPath(path.toString(), photoServerUrl);
        }

    }
    public void setPath(String path, String photoServerUrl){
        if(path != null){
            this.path = path;
            this.url = photoServerUrl + this.path;
            setThumbs();
        }
    }

    public void setPath(Object path, String gender, String photoServerUrl){
        if(path == null){
            this.url = photoServerUrl + "/default/profile_" + gender + ".jpg";
            setThumbs();
        }else{
            setPath(path.toString(), photoServerUrl);
        }
    }

    public void setThumbs(){
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
