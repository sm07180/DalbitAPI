package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class P_RankingLiveOutputVo extends P_ApiVo {
    /* Output */
    private int rank;				        //순위
    private String mem_no;				//회원번호
    private String profileImage;		//프로필이미지
    private String memSex;				//성별
    private String nickName;			//닉네임
    private int level;				        //레벨
    private int gainPoint;			        //달성경험치
    private String fanRank1;			//팬랭킹1위의 회원번호
    private String fanImage;			//팬랭킹1위 프로필이미지
    private String fanSex;				//팬랭킹1위 성별
    private String fanNick;				//팬랭킹1위 닉네임

}
