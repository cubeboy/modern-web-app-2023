# docker 를 이용한 DBMS 구성
---

## MariaDB 와 CloudBeaver 설치
프로젝트에 사용할 DBMS 는 MariaDB 를 사용할 것이며 설치형이 아닌 docker-cmopose 를 이용해서 구성한다.   
로컬 환경에서의 개발을 목표로 할 것이기때문에 보안 및 성능 구성 등은 최소한으로만 구성되었다.   
터미널환경에서 프로젝트 경로 docker-compose.yml 파일이 있는 위치로 이동해서 [ *docker compose up -d* ] 명령어를 실행 한다.   
해당 구성에는 MariaDB 와 함께 CloudBeaver 를 구성한다.   

``` yaml
  services:
    mariadb:
      image: mariadb:10
      container_name: taskagile-mariadb
      restart: always
      ports:
        - 10002:3306
      networks:
        taskagile-net:
          aliases:
            - taskagile-mariadb
      environment:
        MYSQL_DATABASE: "task_agile"
        MYSQL_USER: "taskagile"
        MYSQL_PASSWORD: "taskagile3306"
        MYSQL_ROOT_PASSWORD: "taskagile3306"
        TZ: Asiz:Seoul
      volumes:
        - ./dataRepository/mariadb/config/initdb.d:/docker-entrypoint-initdb.d:ro
        - ./dataRepository/mariadb/config/conf.d:/etc/mysql/conf.d:ro
        - ./dataRepository/mariadb/data:/var/lib/mysql
  cloudbeaver:
    image: dbeaver/cloudbeaver:latest
    container_name: taskagile-dbeaver
    restart: always
    ports:
      - 10001:8978
    networks:
      taskagile-net:
        aliases:
          - taskagile-dbeaver
    environment:
      CB_SERVER_NAME: "taskagile-dbeaver"
      CB_ADMIN_NAME: "taskagile"
      CB_ADMIN_PASSWORD: "taskagile3306"
    volumes:
      - ./dataRepository/mariadb/workspace:/opt/cloudbeaver/workspace
```

CloudBeaver 는 별도의 설치 없이 웹 브라우저 만으로 MySql, MariaDB 를 관리 할 수 있는 환경을 제공한다.   
docker compose 를 실행 하고 [http://localhost:10001](http://localhost:10001) 로 접속하면 CloudBeaver 화면을 확인 할 수 있다.   
우측 상단의 메뉴버튼을 클릭하고 Login 을 선택한 후 미리 설정한 CB_ADMIN_NAME, CB_ADMIN_PASSWORD 값을 입력해서 로그인 한다.   
   
주의 할 점은 New Connection 을 통해서 연결 설정을 할때 compose 에서 설정된 network alias(taskagile-mariadb) 와 내부 네트워크 포트(3306) 를 사용해야 한다.   
CloudBeaver 는 docker 내부 네트워크에서 실행 되고 있기 때문에 내부 네트워크 정보를 사용 해야 한다.   
만약 설치형 DBeaver 을 사용해서 MariaDB 에 접속 하고자 할때는 외부로 노출된 10002 포트를 사용해야 한다.(localhost:10002)   
docker 내부의 가상 네트워크에 대한 자세한 정보는 아래 링크를 참조 하도록 한다.   
가상 네트워크에 대한 지식은 클라우드 서비스 구축에서 매우 중요한 기술이 될 수 있으므로 해당 개념에 대해서 익숙해 지도록 한다.   
> [Docker Networking overview](https://docs.docker.com/network/)

## Initialize task_agile DB
프로젝트 폴더의 아래 파일에 task_agile DB 를 생성하고 이전에 만든 ERD 의 테이블을 생성하는 스크립트가 등록 되어 있다.   
*./dataRepository/mariadb/config/initdb.d/init-database.sql*    
mariadb 는 */docker-entrypoint-initdb.d* 의 Query Script 를 실행해서 초기화 설정을 할 수 있게 한다.   
현재는 DB 와 Table 만 생성 하도록 되어 있지만 이후 필요한 기초 데이터(사용자 정보, 공통코드 정보 등)를 자동으로 등록하는 Query Script 를 추가 하도록 한다.   
향후 새로운 개발 환경을 구축 하려는 개발자에게 별도의 작업 없이 시작 할 수 있도록 DBMS 환경을 제공 하는 것을 목적으로 한다.   
