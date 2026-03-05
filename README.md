<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=2,4,30&height=180&section=header&text=LP_SemiProject&fontSize=50&fontColor=fff&animation=fadeIn&fontAlignY=38&desc=Java%20%7C%20JSP%20%7C%20Oracle%20%7C%20MVC%20쇼핑몰%20웹%20프로젝트&descAlignY=60&descSize=16" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/JSP-007396?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white"/>
  <img src="https://img.shields.io/badge/Apache_Tomcat-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black"/>
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white"/>
</p>

---

## 📌 프로젝트 소개

> **LP_SemiProject**는 Java MVC 패턴 기반의 **쇼핑몰 웹 애플리케이션**입니다.
> 회원 관리, 상품 조회, 장바구니, 주문, 리뷰, 관리자 페이지 등 쇼핑몰 핵심 기능을 구현했습니다.

- 📅 개발 기간: 2025.01.04~01.15 (세미 프로젝트)
- 👥 팀 구성: 3팀
- 🏗 아키텍처: FrontController 패턴 (순수 Java Servlet + JSP)

---

## 👥 팀 구성

| 이름 | 역할 |
|------|------|
| dorapizza1234 | Full-Stack (회원, 인증, 주문, DB 설계) |

---

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| **Language** | Java 11 |
| **View** | JSP, HTML5, CSS3, JavaScript |
| **Architecture** | FrontController Pattern (MVC) |
| **Server** | Apache Tomcat 9 |
| **Database** | Oracle DB |
| **Build** | Maven |
| **Security** | 커스텀 암호화 유틸 (`util/security`) |

---

## ✨ 주요 기능

### 👤 회원 (Member)
| 기능 | 설명 |
|------|------|
| 회원가입 | 취향 설문 포함 (`taste_check`) |
| 로그인 / 로그아웃 | 세션 기반 인증 |
| 아이디 찾기 | 이메일 인증 |
| 비밀번호 찾기 / 변경 | 메일 발송 연동 |
| 유휴 세션 해제 | 자동 로그아웃 방지 |
| 회원 탈퇴 | 탈퇴 처리 |


### 📋 마이페이지 (My Info)
| 기능 | 설명 |
|------|------|
| 내 정보 | 회원 정보 조회·수정 |
| 주문 내역 | 주문 목록 및 배송 추적 팝업 |
| 위시리스트 | 찜한 상품 관리 |
| 리뷰 | 리뷰 작성·삭제 |
| 취향 관리 | 음식 취향 재설정 |
| 문의 내역 | 1:1 문의 확인 |
|

### 🗺 기타
- 매장 위치 지도 (`storeLocation`)
- 메일 발송 기능

---

## 📁 프로젝트 구조

```
LP_SemiProject/
└── src/main/
    ├── java/
    │   ├── admin/          # 관리자 (Controller + Model)
    │   ├── common/         # FrontController, AbstractController
    │   ├── index/          # 메인 페이지
    │   ├── login/          # 로그인/인증
    │   ├── mail/           # 메일 발송
    │   ├── map/            # 매장 위치
    │   ├── member/         # 회원 (Controller + Domain + Model)
    │   ├── my_info/        # 마이페이지
    │   ├── order/          # 주문 (Controller + Domain + Model)
    │   ├── product/        # 상품 (Controller + Domain + Model)
    │   └── util/security/  # 보안 유틸
    └── webapp/WEB-INF/
        ├── admin/          # 관리자 JSP
        ├── login/          # 로그인 JSP
        ├── member/         # 회원 JSP
        ├── my_info/        # 마이페이지 JSP
        ├── order/          # 주문 JSP
        └── map/            # 지도 JSP
```

---

## 🚀 실행 방법

### 사전 요구사항
- Java 11+
- Apache Tomcat 9.x
- Oracle DB
- Maven



---

<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=2,4,30&height=100&section=footer" />
</p>
