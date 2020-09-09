package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ClipMainSubjectTop3Vo {
    @NotBlank(message = "{\"ko_KR\" : \"클립 주제를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 주제를\"}")
    private String subjectType;
}
