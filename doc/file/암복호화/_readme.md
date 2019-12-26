# 암복호화 시 자바 기본 패키지 key size 버그로 인한 패치

    
## java8 이상 버전

    1. <java-home>/jdk{version}/jre/lib/security/java.security 파일을 연다.
    2. #crypto.policy=unlimited  (829 Line) 의 주석을 제거한다.
    3. crypto.policy=unlimited
     
    4. <java-home>jrd{version}/lib/security/java.security 파일을 연다.
    5. #crypto.policy=unlimited  (829 Line) 의 주석을 제거한다.
    6. crypto.policy=unlimited

    or
    
    자바 소스 코드에서
    Security.setProperty("crypto.policy", "unlimited");
    라고 지정해 줄 수도 있다. (안해봄..)

## java8 이전 버전

    1.{자바설치경로}/jre{version}/lib/security 로 이동
    2.local_policy.jar / US_export_policy.jar 2개 jar를 붙여넣는다.
    
    3.{자바설치경로}/jdk{version}/jre/lib/security 로 이동
    4.local_policy.jar / US_export_policy.jar 2개 jar를 붙여넣는다.
    
    