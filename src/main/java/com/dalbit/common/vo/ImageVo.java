package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageVo extends BaseVo{

    private static final long serialVersionUID = 1L;

    public ImageVo(){}

    public ImageVo(Object path, String photoServerUrl){
        if(path != null){
            setPath(path.toString(), photoServerUrl);
        }
    }

    public ImageVo(Object path, String gender, String photoServerUrl){
        if(gender != null){
            if(path == null){
                this.url = photoServerUrl + "/profile_3/profile_"+gender+".jpg";
                setThumbs();
            }else{
                setPath(path.toString(), photoServerUrl);
            }
        }

    }

    private String url;
    private String path;
    private String thumb62x62;
    private String thumb80x80;
    private String thumb88x88;
    private String thumb120x120;
    private String thumb150x150;
    private String thumb190x190;
    private String thumb292x292;
    private String thumb336x336;
    private String thumb700x700;

    public void setPath(String path, String photoServerUrl){
        if(path != null){
            this.path = path;
            this.url = photoServerUrl + this.path;
            setThumbs();
        }
    }

    public void setThumbs(){
        this.thumb62x62 = url + "?62x62";
        this.thumb80x80 = url + "?80x80";
        this.thumb88x88 = url + "?88x88";
        this.thumb120x120 = url + "?120x120";
        this.thumb150x150 = url + "?150x150";
        this.thumb190x190 = url + "?190x190";
        this.thumb292x292 = url + "?292x292";
        this.thumb336x336 = url + "?336x336";
        this.thumb700x700 = url + "?700x700";
    }
}
