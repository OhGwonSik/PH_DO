# 경북포항 스마트물류플랫폼 (PH_DO)

포스코 철강 물류 스마트플랫폼 백엔드. 재고관리 전체 담당 및 외부 설비(크레인) API 연동.

---

## 기술 스택

- **Backend**: Java 17, Spring Boot 3.2.5, MyBatis
- **DB**: MariaDB
- **기타**: SSE, Jasper Reports, Firebase
- **Build**: Gradle
- **Infra**: Docker, Jenkins

---

## 프로젝트 개요

| 항목 | 내용 |
|------|------|
| 기간 | 2024.07 ~ 2025.02 (약 8개월) |
| 팀 구성 | PM 없이 동기 위주 팀 자율 운영 |
| 도메인 | 입고관리(IM) / 재고관리(SM) / 출고관리(OM) |

---

## 요구사항

- Java 17
- Gradle (또는 내장된 gradlew 사용)
- MariaDB

---

## 실행 방법

```bash
git clone https://github.com/OhGwonSik/PH_DO.git
cd PH_DO

# application.yml.example을 참고하여 application.yml 생성 후 값 입력

./gradlew bootRun
```

---

## 주요 기능

### 재고관리 (SM) - 주담당
- 선별지시 / 정보등록 / 완료확정
- 재고실사 / 재고조정 / 재고블락 / 블락해제

### 입고관리 (IM) - 담당
- 입고예정확정 / 입고검수 (PC/태블릿) / 입고정보등록 / 입고완료확정

### 출고관리 (OM) - 일부 담당
- 출고계획 / 출고지시 / 출고완료확정

### 외부 설비 API 연동 - 서비스/쿼리 전담
- ApiInfo enum으로 입고/재고/출고 전 모듈 API 코드/URL 중앙 관리
- ApiConfig.requestToDC()로 DC 중계 서버 통해 외부설비(크레인) 연동
- 입고/재고/출고 조업 3단계 (조업시작 → 작업완료 → 조업완료) 상태 관리

### 기타
- SSE 기반 실시간 관제 알림
- 태블릿 전용 UI 화면 개발
- prod 전용 일재고백업 스케줄러

---

## 트러블슈팅

### 외부 설비 API 파라미터 불일치
- **문제**: 외부설비 측에서 파라미터 문제가 없다고 주장
- **해결**: API 로그 직접 추가하여 파라미터 기록 → 데이터로 외부설비 측 문제임을 입증

### 단수 중복 오류 (DuplicateLayerException)
- **문제**: 동일 위치/단에 재고 중복 적재 시도
- **해결**: 동일 위치+단 체크 로직 추가 후 예외 처리

### 입고예정확정 쿼리 성능 저하
- **문제**: 아이템 조회 시 느린 응답
- **해결**: 쿼리 최적화로 응답 속도 개선

---

## 환경 설정

민감 정보는 `application.yml`에 관리되며 별도 제공되지 않습니다.

```yaml
spring:
  datasource:
    hikari:
      operation:
        jdbc-url: jdbc:mariadb://localhost:3306
        username:
        password:

firebase:
  credentials:
    path:
```
