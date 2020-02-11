# 포토서버 관련

## Photo 서버 Path 암복호화

### 구성

    /업로드타입_구분/중간경로/파일명
    ex.) 암호화 경로 : /bg_1/20556432000/20200210130740799302.jpg
         복호화 경로 : /dalbit_bg_temp/2020/02/10/1/20200210130740799302.jpg
      
### 설명
    1. 업로드타입
        - bg, profile 로써 업로드 이미지 타입을 표시
    2. 구분
        - 0 : Real 이미지
        - 1 : Temp 이미지
        - 2 : ThumbNail 이미지
        - 3 : Default 이미지
    3. 중간경로
        - 실제 경로는 yyyy/MM/dd/hh(앞1자리) 총 12자리로 구성
    3-1. 중간경로 암호화
        - 암호화 로직 : (실제경로의 Milliseconds) / 1000 * 13
    3-2. 중간경로 복호화
        - 복호화 로직 : (실제경로의 Milliseconds) * 1000 / 13
    4. 파일명
        - 파일명과 확장자는 암호화 하지 않음.
    
---


## Photo 서버 Default 이미지 경로

    프로필
        - profile_3/profile_f.jpg
        - profile_3/profile_m.jpg
    배경
        - bg_3/roombg_0.jpg
        - bg_3/roombg_1.jpg
        - bg_3/roombg_2.jpg
        - bg_3/roombg_3.jpg
        - bg_3/roombg_4.jpg
        - bg_3/roombg_5.jpg
        - bg_3/roombg_6.jpg
        - bg_3/roombg_7.jpg
        - bg_3/roombg_8.jpg
        - bg_3/roombg_9.jpg
    
---