package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class ClipAddVo {
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

    @NotBlank(message = "{\"ko_KR\" : \"파일명을\"}")
    @NotNull(message = "{\"ko_KR\" : \"파일명을\"}")
    private String fileName;

    @NotBlank(message = "{\"ko_KR\" : \"파일을\"}")
    @NotNull(message = "{\"ko_KR\" : \"파일을\"}")
    private String filePath;

    @NotBlank(message = "{\"ko_KR\" : \"파일재생 시간을\"}")
    @NotNull(message = "{\"ko_KR\" : \"파일재생 시간을\"}")
    private String filePlayTime;    //파일재생시간

    @NotBlank(message = "{\"ko_KR\" : \"파일용량을\"}")
    @NotNull(message = "{\"ko_KR\" : \"파일용량을\"}")
    private String fileSize;
}
