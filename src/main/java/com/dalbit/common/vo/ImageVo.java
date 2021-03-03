package com.dalbit.common.vo;

import com.dalbit.common.code.Code;
import com.dalbit.mailbox.vo.procedure.P_MailBoxListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class ImageVo extends BaseVo{

    private static final long serialVersionUID = 1L;

    public ImageVo(){}

    public ImageVo(String url){
        if(!DalbitUtil.isEmpty(url)){
            this.url = url;
            setThumbs();
        }
    }

    public ImageVo(P_MailBoxListVo path, String photoServerUrl){
        if(!DalbitUtil.isEmpty(path) && !DalbitUtil.isEmpty(path.toString())){
            setPath(path.toString(), photoServerUrl);
        }
    }

    public ImageVo(Object path, String photoServerUrl){
        if(!DalbitUtil.isEmpty(path) && !DalbitUtil.isEmpty(path.toString())){
            setPath(path.toString(), photoServerUrl);
        }
    }

    public ImageVo(Object path, String gender, String photoServerUrl){
        if(DalbitUtil.isEmpty(path) || DalbitUtil.isEmpty(path.toString())){
            if(!DalbitUtil.isEmpty(gender)) {
                this.path = Code.포토_프로필_디폴트_PREFIX.getCode() + "/" + Code.프로필이미지_파일명_PREFIX.getCode() + gender + "_200327.jpg";
                this.url = photoServerUrl + Code.포토_프로필_디폴트_PREFIX.getCode() + "/" + Code.프로필이미지_파일명_PREFIX.getCode() + gender + "_200327.jpg";
                setThumbs();
            }
        }else{
            setPath(path.toString(), photoServerUrl);
        }
    }

    private String url;
    private String path;
    private String thumb50x50;
    private String thumb62x62;
    private String thumb80x80;
    private String thumb88x88;
    private String thumb100x100;
    private String thumb120x120;
    private String thumb150x150;
    private String thumb190x190;
    private String thumb292x292;
    private String thumb336x336;
    private String thumb700x700;

    public void setPath(String path, String photoServerUrl){
        if(!DalbitUtil.isEmpty(path)){
            this.path = path;
            this.url = photoServerUrl + this.path;

            setThumbs();
        }
    }

    public void setThumbs(){
        this.thumb50x50 = url + (this.url.endsWith("webp") ? "" : "?50x50");
        this.thumb62x62 = url + (this.url.endsWith("webp") ? "" : "?62x62");
        this.thumb80x80 = url + (this.url.endsWith("webp") ? "" : "?80x80");
        this.thumb88x88 = url + (this.url.endsWith("webp") ? "" : "?88x88");
        this.thumb100x100 = url + (this.url.endsWith("webp") ? "" : "?100x100");
        this.thumb120x120 = url + (this.url.endsWith("webp") ? "" : "?120x120");
        this.thumb150x150 = url + (this.url.endsWith("webp") ? "" : "?150x150");
        this.thumb190x190 = url + (this.url.endsWith("webp") ? "" : "?190x190");
        this.thumb292x292 = url + (this.url.endsWith("webp") ? "" : "?292x292");
        this.thumb336x336 = url + (this.url.endsWith("webp") ? "" : "?336x336");
        this.thumb700x700 = url + (this.url.endsWith("webp") ? "" : "?700x700");
    }
}
