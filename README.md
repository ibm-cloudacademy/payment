# payment microservice
Payment Microservice for Cloud Native Post Academy Deep Dive

개요
분산된 마이크로서비스에서 일관성을 유지하기 위해 잘 알려준 방법으로 크리스 리차드처럼
이벤트 기반으로 하는것과 REST Try-Confirm/Cancel 기반의 분산 트랜잭션을 처리하는 방법이 있다.

이 학습에서는 REST 기반 분산트랜잭션을 처리하여 일관성을 유지하는 방법으로 구현된 3 개의 마이크로서비스를
컨테이터화 작업하는 것을 학습합니다.

![alt text](img/rest_tcc.png)

## 필수작업
### 사전 설치 Solution
 default namespace에 backing service solution이 설치되어 있습니다.

 * postgresSQL : orderdb, stockdb, paymentdb
 * kafka
 * redis

![alt text](img/backing_service.png)

 1. App 관련 작성 규칙
   labname을 기준으로 namespace로 만들고 자신의 앱을 배포하십시요
```
   1.1. namespace 작성 규칙 :
      lab + NN( 일련번호 2자리)

      ex)
      - lab01
      - lab02
          :
      - lab40

   1.2. app name 작성 규칙 :
      lab + NN + "-" + post-fix (order|stock|payment  하나 선택)

      ex)
      - lab01-order
      - lab01-stock
      - lab01-payment

   1.3. service name 작성 규칙
      app name과 동일하게 작성

      ex)
      - lab01-payment

   1.4. configmap 작성 규칙
      app name + "-" + "config"
      app name 에 postfix("config" 명 추가)

      ex)
      - lab01-payment-config

   1.5. YAML 파일 규칙 :
      labNN + appname(order|stock|payment 하나 선택) + ".yaml
      labNN + appname-service + ".yaml"
      labNN + appname-config + ".yaml"

      ex)
      - lab01-payment.yaml
      - lab01-payment-service.yaml
      - lab01-payment-config.yaml
```

 2. postgres db 접속 결제 결과 확인
    사용자가 상품주문한 주문id로 결제정보를 확인합니다.
    * 결제 테이블 : payment

![alt text](img/select_payment.png)
