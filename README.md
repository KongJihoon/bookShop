# 📖 도서 쇼핑몰 프로젝트

> 도서 쇼핑몰 시스템의 핵심 백엔드 기능 설계 및 구현

Spring Boot 기반의 도서 쇼핑몰 백엔드 프로젝트입니다.  
회원가입, 로그인, 도서 관리, 장바구니, 주문/결제, 리뷰 등 쇼핑몰 서비스에 필요한 핵심 기능을 구현하였습니다.

### 🗓️개발 기간 및 인원
> - 2025/05/02 ~ 2025/6/13(5주)


---
## 1️⃣ ERD



## 🛠 기술 스택

### 👨‍💻 Backend
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security & JWT (Access/Refresh)
- QueryDSL
- JavaMailSender (이메일 인증)
- OAuth2 (Kakao Login)

### 🛒 Payment & Infra
- KakaoPay API (실 결제 연동)
- Toss Payments API (예정)
- MySQL
- Redis (Token 관리, 조회수 캐싱)
- AWS (EC2, RDS, S3)
- Docker

### 🧰 Tools
- IntelliJ IDEA Ultimate
- Postman
- Git & GitHub
- Notion (기획, 일정 관리)

---

## 🧩 주요 기능

### 👤 사용자 (User)
- [ ] 회원가입 (로그인 ID, 이메일, 닉네임 중복 검증)
- [ ] 이메일 인증 (JavaMailSender)
- [ ] 로그인 (JWT 토큰 발급)
- [ ] 카카오 소셜 로그인 (OAuth2)
- [ ] 마이페이지 (회원 정보 조회/수정/탈퇴)

---

### 📚 도서 (Book)
- [ ] 도서 등록 / 수정 / 삭제 (판매자 권한)
- [ ] 도서 목록 / 상세 조회
- [ ] 카테고리별 조회 및 정렬
- [ ] 도서 별 리뷰/평점, 좋아요 수 기준 정렬

---

### 🛒 장바구니 (Cart)
- [ ] 도서 장바구니 추가 / 수정 / 삭제
- [ ] 장바구니 조회
- [ ] 수량 조절 및 총합 가격 확인

---

### 📦 주문 (Order)
- [ ] 주문 생성
  - 장바구니 기반: 담아둔 상품을 선택하여 주문 생성
  - 바로구매 기반: 장바구니 없이 상품 1건 즉시 주문 생성
- [ ] 주문 상세 조회 (도서 목록, 수량, 가격, 상태)
- [ ] 주문 상태 관리 (`READY`, `PAID`, `CANCELLED` 등)

---

### 💳 결제 (Payment)
- [ ] KakaoPay API 연동 (실 결제 요청/승인/취소 흐름 구현)
- [ ] 결제 이력 저장, 실패/취소/승인 시 상태 관리
- [ ] 결제 응답 로그 (`pg_tid`, `raw_response`, `approved_at` 등 저장)

---

### 📝 리뷰 & 댓글 & 좋아요
- [ ] 도서 리뷰 작성 / 수정 / 삭제 (1인 1리뷰 제한)
- [ ] 리뷰 댓글
- [ ] 리뷰 좋아요 기능 (중복 방지)
- [ ] 리뷰 정렬 (최신순 / 좋아요순)


