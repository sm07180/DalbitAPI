# JKS 파일 생성

### 준비

    fullchain.pem
    key.pem
    cert.pem
    
      
### 설명
    1. openssl pkcs12 -export -in fullchain.pem -inkey key.pem -out cert_and_key.p12 -name tomcat -caname root
        - 비밀번호 : tomcat
    
    2. openssl pkcs12 -export -name dalbitlive.com -in fullchain.pem -inkey key.pem -out cert.pfx
        - 비밀번호 : tomcat
        
    3. keytool -importkeystore -srckeystore cert.pfx -srcstoretype pkcs12 -destkeystore dalbitlive.jks -deststoretype jks
        - 뭘 입력하라고 하는데 tomcat 으로 통일
        
    4. keytool -importkeystore -deststorepass tomcat -destkeypass tomcat -destkeystore dalbitlive.jks -srckeystore cert_and_key.p12 -srcstoretype PKCS12 -srcstorepass tomcat -alias tomcat
        
    5. keytool -import -trustcacerts -alias root -file fullchain.pem -keystore dalbitlive.jks
    
    6. 만들어진 jks 파일을 doc/file/ssl 아래 붙여 넣는다.
    
    7. application-{{profile}}.properties 값에 설정한다.
        server.ssl.key-store=jks파일명
        server.ssl.key-store-type=JKS
        server.ssl.key-store-password=tomcat
        server.ssl.key-password=tomcat