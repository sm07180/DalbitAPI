package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReplyAddVo {

    @NotBlank
    private String memNo;		//이벤트 댓글 작성 회원번호
    @NotNull @Min(value = 1, message = "1(댓글) 또는 2(대댓글) 를 입력해주세요.") @Max(value = 2, message = "1(댓글) 또는 2(대댓글) 를 입력해주세요.")
    private Integer depth;			//1: 댓글, 2:대댓글
    @NotNull
    private Integer eventIdx;		//이벤트 번호
    @NotNull
    private String content;		//내용

}
