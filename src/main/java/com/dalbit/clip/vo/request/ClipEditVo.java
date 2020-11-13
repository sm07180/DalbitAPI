package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class ClipEditVo {
    @NotBlank(message = "{\"ko_KR\" : \"클립 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 번호를\"}")
    private String clipNo;

    @NotBlank(message = "{\"ko_KR\" : \"클립 주제를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 주제를\"}")
    private String subjectType;

    @NotBlank(message = "{\"ko_KR\" : \"클립 제목을\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 제목을\"}")
    @Size(message = "{\"ko_KR\" : \"클립 제목을\"}", max = 20)
    private String title;

    @NotNull(message = "{\"ko_KR\" : \"청취제한을\"}")
    @Min(message = "{\"ko_KR\" : \"청취제한을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"청취제한을\"}", value = 2)
    private Integer entryType;

    @NotNull(message = "{\"ko_KR\" : \"공개여부를\"}")
    @Min(message = "{\"ko_KR\" : \"공개여부를\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"공개여부를\"}", value = 1)
    private Integer openType;

    private String bgImg;

    @Size(message = "{\"ko_KR\" : \"커버제목을\"}", max = 20)
    private String coverTitle;
    @Size(message = "{\"ko_KR\" : \"커버가수를\"}", max = 20)
    private String coverSinger;

}
