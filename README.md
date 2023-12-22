# vuejs.spring-boot.mysql [taskagile] 최신화 프로젝트
---

## 프로젝트 목표
**[스프링 5와 Vue.js 2로 시작하는 모던 웹 애플리케이션 개발]**   
**(원제: Building Applications with Spring 5 and Vue.js 2 - James J. Ye)**
이 책은 Vue.js 와 Spring Boot, MySQL 을 기술적인 기반으로 설계부터, 구현, 테스트, 배포, 모니터링 까지 포함하는 방대한 내용을 설명한다.
초기에 SPA에 대한 경험이 부족할 때 이 책을 통해서 체계적인 SPA App 구현에 대한 개념을 배울 수 있었다.
하지만 개인적으로 몇가지 부족한 부분이 있어서 이를 보완하는 프로젝트를 진행 하고자 한다.

### 적용 기술의 최신화
이책은 2018년 작성된 것으로 현재 기준으로는 조금 낡은 기술들이 사용되었다.
이 책에서 제공되는 개발 환경과 소스코드를 2023.12 기준으로 최신화 합니다.
> java runtime version 1.8 => 17
> spring boot 2.0.4.RELEASE => 3.2.1
> npm 8.0.x => 10.2.3
> webpack => vite
> vue2 => vue3 composition API
> bootstrap => vuetify3
> jest => vites

### 개발 언어 변경
본 프로젝트를 진행 하면서 kotlin 에 대한 실제 프로젝트 적용 능력을 습득하기 위해 개발 언어를 java 에서 kotlin 으로 변경 하여 진행 합니다.

### Spring Web 을 WebFlux 로 변경
기존의 동기 방식의 Web

### Docker 기반의 개발 환경 구축
개발 환경 표준화를 목표로 git 코드 배포 만으로 모두 동일한 환경을 구축 하는 것을 목표로 한다.
따라서 개발에 필요한 도구(MySQL, Workbench, Prometheus ...)들을 직접 설치하지 않고 docker 명령어 또는 docker-compose 를 이용해서 동일한 개발 환경을 구성 할 수 있도록 한다.

### NoSQL, Redis 등을 추가
이 책에서 제공하는 내용을 처음부터 끝까지 진행 한 후에는 NoSQL(Mongo), Redis 기능들을 추가 할 수 있도록 한다.

---

## 목차

### 1. 설계
### 2. 환경 구성
### 3. Project Scafolding
