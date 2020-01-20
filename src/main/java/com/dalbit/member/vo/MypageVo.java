package com.dalbit.member.vo;

import com.dalbit.common.vo.BaseVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Date;
import java.util.HashMap;

@Getter @Setter
public class MypageVo extends BaseVo {

    private static final long serialVersionUID = 1L;

    public static MypageVo getUserInfo() {
        //SecurityUserVo user = (SecurityUserVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HashMap MypageInfoMap = (HashMap)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MypageVo sessionMypageVo = new Gson().fromJson(DalbitUtil.getStringMap(MypageInfoMap, "MypageInfo"), MypageVo.class);

        return sessionMypageVo;
    }

    private String memSex;          	        //성별
    private String nickName;                    //닉네임                
    private String name;                        //이름                  
    private int birthYear;                      //생년                  
    private int birthMonth;                     //생월                  
    private int birthDay;                       //생일                  
    private String profileImage;                //프로필이미지패스      
    private String profileMsg;                  //메시지
    private String subject_type;    			//방주제
    private String title;                       //방제목                
    private String image_background;            //방배경이미지          
    private int msg_welcom;                     //환영메세지            
    private int restrict_entry;                 //입장제한              
    private int age;                            //나이대
    private String memId;                       //자동생성된아이디8자   
    private Object backgroundImage;             //배경이미지            
    private int level;                          //레벨
    private int fanCount;                       //팬으로등록된회원수    
    private int starCount;                      //스타로등록된회원수    
    private int enableFan;                      //팬등록가능여부(1:가능)
    private String blocked_mem_no;  			//차단회원번호
    private int all_ok;          				//전체알림
    private int fan_reg;                        //팬등록알림            
    private int fan_board;                      //팬보드알림            
    private int star_broadcast;                 //스타방송알림          
    private int star_notice;                    //스타공지사항알림      
    private int event_notice;                   //이벤트공지알림        
    private int search;                         //검색허용              
}
