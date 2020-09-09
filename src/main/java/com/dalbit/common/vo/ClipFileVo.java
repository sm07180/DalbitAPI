package com.dalbit.common.vo;

import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClipFileVo extends BaseVo{

    private static final long serialVersionUID = 1L;

    public ClipFileVo(){}

    public ClipFileVo(Object path, String photoServerUrl){
        if(!DalbitUtil.isEmpty(path) && !DalbitUtil.isEmpty(path.toString())){
            setPath(path.toString(), photoServerUrl);
        }
    }

    private String url;
    private String path;

    public void setPath(String path, String photoServerUrl){
        if(path != null){
            this.path = path;
            this.url = photoServerUrl + this.path;
        }
    }
}
