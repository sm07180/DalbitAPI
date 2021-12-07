package com.dalbit.main.vo;

import lombok.Data;

@Data
public class ServerListVO {
    private String name = "";   //서버명
    private String host = "";   //호스트 주소
    private String api = "";    //api 서버 주소
    private String photo = "";  //photo 서버 주소
    private String socket = ""; //소켓 서버 주소

    public ServerListVO(){}

    //모바일용
    public ServerListVO(String name, String host, String api, String photo, String socket){
        this.name = name;
        this.host = host;
        this.api = api;
        this.photo = photo;
        this.socket = socket;
    }

    //웹용
    public ServerListVO(String name, String host){
        this.name = name;
        this.host = host;
    }

}
