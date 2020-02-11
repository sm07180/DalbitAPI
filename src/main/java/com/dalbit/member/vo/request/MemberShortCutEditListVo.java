package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@Getter
@Setter
public class MemberShortCutEditListVo {

    ArrayList<MemberShortCutEditVo> shortCutList;
}
