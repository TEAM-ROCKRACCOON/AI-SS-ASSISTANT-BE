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

<details>
<summary>디렉토리 구조 보기</summary>

```text
src
 └─ main
     └─ java
         └─ com.example.napzak
             ├─ auth            # 인증/인가 관련 모듈 (Google OAuth)
             ├─ common          # 공통 유틸, 상수, response wrapper 등
             ├─ domain
             │   ├─ routine     # 루틴 생성/조회/달성 처리
             │   ├─ feedback    # 사용자 피드백 수집 도메인
             │   └─ calendar    # Google Calendar 연동 도메인
             ├─ external        # 외부 API 연동 (OpenWeather, AI 서버 등)
             ├─ global          # 예외 처리, 설정 클래스, 응답 필터 등 전역 구성
             ├─ swagger         # Swagger 문서화 관련 설정
             └─ NapzakApplication.java  # Main Application Class
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