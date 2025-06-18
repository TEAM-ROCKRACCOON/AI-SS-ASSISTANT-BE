# 🧹 AI 쓱싹비서 백엔드 서버

AI 쓱싹비서는 사용자 생활 패턴, 일정, 날씨 데이터를 기반으로 **AI가 맞춤형 청소 루틴을 추천**하는 스마트 청소 도우미 서비스입니다.  
이 저장소는 백엔드(Spring Boot) 서버로, 프론트엔드 및 AI 서버와 연동하여 전체 서비스의 중추 역할을 수행합니다.

---

## ✅ 주요 기능

- Google OAuth 2.0 로그인 및 인증 처리
- Google Calendar 일정 데이터 연동
- OpenWeatherMap API 기반 실시간 날씨 조회
- 사용자 루틴 생성, 조회, 수정, 삭제
- 루틴 달성률 및 피드백 저장
- AI 서버와의 연동을 통한 루틴 추천 요청 및 분석 결과 수신
- AWS 기반의 EC2, RDS, S3 활용 인프라 구성

---

## 🛠 기술 스택

- Java 17
- Spring Boot 3.x
- Spring Security (OAuth2)
- JPA (Hibernate)
- MySQL (Amazon RDS)
- AWS EC2 / S3
- Gradle
- OpenFeign (외부 API 통신)
- Swagger / Springdoc
- FastAPI (Python 기반 AI 연동 서버, 별도 구성)

---

## 🗂 프로젝트 구조 (DDD 기반, Bounded Context 중심)

```text
src
 └─ main
     ├─ java
     │   └─ com.ai_ss
     │       ├─ domain                # 핵심 도메인 모듈
     │       │   ├─ ai                # AI 서버 호출 및 전송 모델
     │       │   ├─ cleaningTask      # 전체 청소 종류 및 소요시간 관리
     │       │   ├─ feedback          # 사용자 피드백 관리
     │       │   ├─ routine           # 루틴 생성 및 관리
     │       │   └─ user              # 사용자 정보 및 설정 관리
     │       │       ├─ api
     │       │       │   ├─ controller  # 사용자 API 요청 처리
     │       │       │   ├─ dto         # 사용자 요청/응답 데이터 객체
     │       │       │   ├─ exception   # 사용자 도메인 전용 예외
     │       │       │   └─ service     # 사용자 도메인 비즈니스 로직
     │       │       └─ core
     │       │           ├─ entity      # JPA Entity 및 Enum 정의
     │       │           └─ vo          # Value Object (Address, Preferences 등)
     │       └─ global
     │           ├─ auth             # 인증 관련 전역 모듈
     │           │   ├─ annotation
     │           │   ├─ client        # Google/Kakao OAuth 클라이언트
     │           │   ├─ jwt           # JWT 생성, 필터, 서비스
     │           │   ├─ redis         # Redis 기반 토큰 잠금 처리
     │           │   ├─ resolver      # 로그인 유저 정보 Argument Resolver
     │           │   ├─ role          # 권한/역할 처리 유틸
     │           │   └─ security      # Spring Security 설정
     │           ├─ common           # 공통 유틸/설정/응답 구조
     │           │   ├─ annotation    # 커스텀 어노테이션
     │           │   ├─ config        # 전역 설정 (Security, Redis, Swagger 등)
     │           │   ├─ controller    # HealthCheck 등 공통 컨트롤러
     │           │   ├─ converter     # Enum 변환 등
     │           │   ├─ entity        # 공통 엔티티 (예: BaseTimeEntity)
     │           │   ├─ exception     # 전역 에러 코드 및 예외 처리
     │           │   └─ handler       # GlobalExceptionHandler
     │           ├─ external.s3      # S3 업로드 등 외부 연동
     │           └─ swagger          # Swagger 관련 구성
     ├─ resources
     │   ├─ application.yml
     │   ├─ application-local.yml
     │   └─ application-prod.yml
     └─ test
         └─ java
             └─ com.ai_ss
                 └─ AiSsApplicationTests
```

---

### ☁️ 배포 인프라 구성

| 구성 요소       | 내용                                     |
|----------------|------------------------------------------|
| **백엔드**       | AWS EC2 (Ubuntu + Spring Boot)           |
| **DB**           | Amazon RDS (MySQL)                       |
| **파일 저장소**   | Amazon S3 (청소 인증 사진, 비교 이미지 등) |
| **AI 서버**       | FastAPI (별도 EC2 또는 Docker Container) |

---

## 📌 협업 컨벤션

[📝 TEAM-ROCKRACCOON Ground Rule 문서 보기](https://github.com/TEAM-ROCKRACCOON/.github/blob/main/GroundRule.MD)