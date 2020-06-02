package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class NoticeEditVo {

    @NotBlank
    private String roomNo;

    @NotBlank @Size(max = 200)
    private String notice;
}
