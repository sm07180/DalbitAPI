# 프로젝트 세팅

## 프로젝트 환경설정

### git 정보

#### git 사용 전
    IDC에 GIT 서버를 구축하여 사용중이고
    회사 GIT서버에 접속하기 위해선 ssh key를 생성하여 public 키를 전달해야함.
    
[SSH 키 생성방법 링크](https://git-scm.com/book/ko/v2/Git-%EC%84%9C%EB%B2%84-SSH-%EA%B3%B5%EA%B0%9C%ED%82%A4-%EB%A7%8C%EB%93%A4%EA%B8%B0)

    ssh key 생성 후 
    광주에 계신 황보진호 차장님께 전달해야 등록 후 사용 가능.

#### repository 정보

    gitrepo@devj.dalbitcast.com:radio_mobile.git
 
#### branch 정보

    master : 운영환경에 올라갈 형상 관리용 (일부만 권한부여)
    dev    : 개발, 테스트 시 사용할 branch
    
---


### workspace 경로

    D:/workspace/radio_mobile
    
---

### encoding 설정

    ctrl + alt + s (Settings 메뉴)
    Editor > File Encodings 으로 이동
    Project Encoding : UTF-8
    Properties Files : UTF-8 로 설정한다.
    

### spring profile 설정

    Run/Debug Configurations (tomcat 설정) 화면에서
    Active profiles에 "local", "dev", "prod" 중
    local 로 설정
    
    springboot에서 application-{profile}.properties을 자동으로 읽어줌.
    
---

### AES 암복호화 관련 설정

    복호화 시 
    javax.crypto.BadPaddingException: Given final block not properly padded. Such issues can arise if a bad key is used during decryption.
    이런 에러가 발생 시
    
    doc/file/암복호화 폴더 내에 _readme.md 파일 참고하여 세팅
    

## 기타 설정

### springboot auto reload 설정

#### static autoreload

    static(html, js, css 등..)한 파일을 자동으로 reload
    1) registry 설정
    ctrl + shift + a 를 누르면 IntelliJ에서 검색창이 나온다.
    'registry' 입력 후 'registry...'메뉴 선택
    compiler.automake.allow.when.app.running 항목에 체크 후 close
    
#### compiler 설정

    ctrl + alt + s (settings)
    Build, Excution, Development > Compiler
    build project automatically 체크 후 OK
    
### ssl local 설정

> 현재 1~7까지 실행 한 후 생성된 jks 파일을 doc/file/ssl/local_ssl_cert.jks 에 넣어놔서 따로 실행할 필요 없음.

    1.cmd 창을 실행한다
    2.pem파일이 있는 곳으로 이동한다
    3.openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out cert_and_key.p12 -name tomcat -caname root 을 실행한다.
    4.password를 설정한다. (tomcat으로 설정함)
    5.password를 다시 입력 후 cert_and_key.p12 파일이 생성 된 것을 확인한다.
    
    6.keytool -importkeystore -deststorepass tomcat -destkeypass tomcat -destkeystore  local_ssl_cert.jks -srckeystore cert_and_key.p12 -srcstoretype PKCS12 -srcstorepass tomcat -alias tomcat 입력한다.
    7.keytool -import -trustcacerts -alias root -file fullchain.pem -keystore local_ssl_cert.jks 를 입력한다.
    
    
### Swagger UI 개발 가이드

 > SampleRestController 주석 Swagger Sample 참고(서버가동 후 URL: 개별호스트/swagger-ui.html)
 
    * 사용하지 않는 컨트롤러 또는 메소드명에 어노테이션 @ApiIgnore 추가 -> swagger-ui.html 에 비노출
    * @ApiOperation(value = "방송정보입니다.") -> 메소드 설명
      @ApiImplicitParams({
          @ApiImplicitParam(name = "title", value = "제목", required = false, dataType = "string", paramType = "header", defaultValue = "asfasf"),

          @ApiImplicitParam(name = "gubun", value = "방송종류", required = true, dataType = "string", paramType = "query", defaultValue = "1"),
          @ApiImplicitParam(name = "gubunNM", value = "방송종류명", required = true, dataType = "string", paramType = "path", defaultValue = "2"),
          @ApiImplicitParam(name = "intro", value = "환영인사말", required = true, dataType = "string", paramType = "path", defaultValue = "3")
  
      })
      -> Parameter 설정: @ApiImplicitParam 에 paramType 를 구분, header는 우선순위로 가장 먼저 작성하는걸로..
         - paramType = "query" 는 Curl에 /brodTest?gubun=1' parameter 표시, 그 외 path 기본값 
         - required 설정은 true, false 로 필수값 구분
         - defaultValue 에 값 설정시 Value 자동입력  
      
   