package com.dalbit.main.vo.procedure;

import com.dalbit.main.vo.request.QnaVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Getter @Setter
public class P_QnaVo {

    private String mem_no;
    private int slctType;
    private String title;
    private String contents;
    private String addFile;
    private String addFile1;
    private String addFile2;
    private String addFile3;
    private String addFileName1;
    private String addFileName2;
    private String addFileName3;
    private String email;
    private String browser;
    private String platform;
    private String nickName;
    private String phone;
    private int qnaIdx;

    public P_QnaVo(){}
    public P_QnaVo(QnaVo qnaVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(qnaVo.getQnaType());
        setTitle(qnaVo.getTitle());
        setContents(qnaVo.getContents());
        setAddFile(qnaVo.getQuestionFile());
        setAddFile1(qnaVo.getQuestionFile1());
        setAddFile2(qnaVo.getQuestionFile2());
        setAddFile3(qnaVo.getQuestionFile3());
        setAddFileName1(qnaVo.getQuestionFileName1());
        setAddFileName2(qnaVo.getQuestionFileName2());
        setAddFileName3(qnaVo.getQuestionFileName3());
        setEmail(qnaVo.getEmail());
        setBrowser(DalbitUtil.getUserAgent(request));
        setQnaIdx(qnaVo.getQnaIdx());
        setNickName(qnaVo.getNickName());
        setPhone(qnaVo.getPhone());

        String customHeader = request.getHeader(DalbitUtil.getProperty("rest.custom.header.name"));
        customHeader = java.net.URLDecoder.decode(customHeader);
        HashMap<String, Object> headers = new Gson().fromJson(customHeader, HashMap.class);
        int os = DalbitUtil.getIntMap(headers,"os");
        String isHybrid = DalbitUtil.getStringMap(headers,"isHybrid");
        if(os == 1){
            setPlatform("Android-Mobile");
        }else if(os == 2){
            setPlatform("IOS-Mobile");
        } else if(os == 3){
            setPlatform("PC");
        } else if(isHybrid.equals("Y")){
            setPlatform("Web-Mobile");
        }
    }

}
