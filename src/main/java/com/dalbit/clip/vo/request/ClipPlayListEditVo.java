package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ClipPlayListEditVo {

    @NotNull(message = "{\"ko_KR\" : \"정렬방식을\"}")
    @Min(message = "{\"ko_KR\" : \"정렬방식을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"정렬방식을\"}", value = 5)
    private Integer sortType;

    private String deleteClipNoList;    //삭제된 클립번호 리스트 '|' 구분
    private String sortClipNoList;      //정렬된 클립번호 리스트 '|' 구분
}
