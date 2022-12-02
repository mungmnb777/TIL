# VPC 네트워크 환경 구성

날짜: 2022년 11월 17일
태그: 공부

## VPC 생성

1. `VPC` → `Virtual Private Cloud` → `VPC` → `VPC 생성`
    
    ![Untitled](../images/VPC%20%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC%20%ED%99%98%EA%B2%BD%20%EA%B5%AC%EC%84%B1_1.png)
    
2. VPC 설정
    
    ![Untitled](../images/VPC%20%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC%20%ED%99%98%EA%B2%BD%20%EA%B5%AC%EC%84%B1_2.png)
    
    - 생성할 리소스 : `VPC만`
    - 이름 태그 : 원하는 이름 태그를 설정한다. 나의 경우에는 `final-project-vpc`로 설정했다.
    - IPv4 CIDR 블록 : `IPv4 CIDR 수동 입력`을 선택한다.
    - IPv4 CIDR : `10.1.0.0/16`을 선택한다.
    - IPv6 CIDR 블록 : `IPv6 CIDR 블록 없음`을 선택한다.
    - 테넌시 : `기본값`

## 서브넷 생성

1. `VPC` → `Virutal Private Cloud` → `서브넷` → `서브넷 생성`
    
    ![Untitled](../images/VPC%20%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC%20%ED%99%98%EA%B2%BD%20%EA%B5%AC%EC%84%B1_3.pngg)
    
2. VPC ID에 아까 생성한 VPC를 선택한다.
3. 서브넷 설정에서는 총 4개의 서브넷을 생성할 것이다. (public-subnet 2개, private-subnet 2개)
    1. 첫 번째 서브넷 (public-subnet)
        - 서브넷 이름 : `public-subnet-a1`으로 입력한다.
        - 가용 영역 : `아시아 태평양 (서울) / ap-northeast-2a`로 선택한다.
        - IPv4 CIDR 블록 : `10.1.1.0/24`로 입력한다.
    2. 두 번째 서브넷 (public-subnet)
        - 서브넷 이름 : `public-subnet-c1`으로 입력한다.
        - 가용 영역 : `아시아 태평양 (서울) / ap-northeast-2c`로 선택한다.
        - IPv4 CIDR 블록 : `10.1.2.0/24`로 입력한다.
    3. 세 번째 서브넷 (public-subnet)
        - 서브넷 이름 : `private-subnet-a1`으로 입력한다.
        - 가용 영역 : `아시아 태평양 (서울) / ap-northeast-2a`로 선택한다.
        - IPv4 CIDR 블록 : `10.1.3.0/24`로 입력한다.
    4. 네 번째 서브넷 (public-subnet)
        - 서브넷 이름 : `private-subnet-c1`으로 입력한다.
        - 가용 영역 : `아시아 태평양 (서울) / ap-northeast-2c`로 선택한다.
        - IPv4 CIDR 블록 : `10.1.4.0/24`로 입력한다.
4. 작업이 완료되면 서브넷을 생성한다.

## 인터넷 게이트웨이 생성

1. `VPC` → `Virtual Private Cloud` → `인터넷 게이트웨이` → `인터넷 게이트웨이 생성
    
    ![Untitled](../images/VPC%20%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC%20%ED%99%98%EA%B2%BD%20%EA%B5%AC%EC%84%B1_4.png)
    
2. 이름만 입력하고 바로 생성한다. 나의 경우에는 `final-project-vpc-igw`로 입력했다.
    
    ![Untitled](../images/VPC%20%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC%20%ED%99%98%EA%B2%BD%20%EA%B5%AC%EC%84%B1_5.png)
    
3. 생성한 후 인터넷 게이트웨이 인스턴스에 우클릭하여 `VPC에 연결`을 클릭한다.
    
    ![Untitled](../images/VPC%20%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC%20%ED%99%98%EA%B2%BD%20%EA%B5%AC%EC%84%B1_6.png)
    
4. VPC에 위에서 생성한 VPC를 선택한 후 저장한다.

## NAT 게이트웨이 생성

1. `VPC` → `Virutal Private Cloud` → `NAT 게이트웨이` → `NAT 게이트웨이 생성`
2. NAT 게이트웨이를 설정한다. 총 2개의 게이트웨이를 생성해야 한다. (public-subnet-a1 NAT, public-subnet-c1 NAT)
    - 이름 : 원하는 이름을 입력한다. 나의 경우에는 `nat-gw-a1`으로 입력했다.
    - 서브넷 : `public-subnet-a1`
    - 연결 유형 : 퍼블릿
    - 탄력적 IP 할당 ID : 오른쪽에 있는 `탄력적 IP 할당` 버튼을 클릭한다.
3. `public-subnet-c1`의 NAT 게이트웨이도 생성해준다.

## 라우팅 테이블 생성

1. `VPC` → `Virutal Private Cloud` → `라우팅 테이블` → `라우팅 테이블 생성`
    
    ![Untitled](../images/VPC%20%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC%20%ED%99%98%EA%B2%BD%20%EA%B5%AC%EC%84%B1_7.png)
    
2. 총 3개의 라우팅 테이블을 생성해야 한다. (`public-subnet-rt,` `private-subnet-a1-rt`, `private-subnet-c1-rt`)
    1. public-route-table
        1. 이름과 VPC를 입력한다. VPC는 생성한 VPC를 선택한다.
        2. 라우팅 테이블 생성 후 라우팅 탭에서 우측에 있는 `라우팅 편집`을 클릭한다.
        3. `라우팅 추가` 버튼을 클릭한다. 첫번째 대상은 `0.0.0.0/0`, 두번째 대상은 `인터넷 게이트웨이` 선택 후 위에서 생성한 인터넷 게이트웨이로 설정한다.
        4.  저장 후 서브넷 연결 탭을 클릭한다. 그 후 `서브넷 연결 편집` 버튼을 클릭한다.
        5. `public-subnet-a1`과 `public-subnet-c1`을 선택한 후 저장한다.
    2. private-route-table
        1. 우선 위와 같이 라우팅 테이블을 생성한다. 참고로 private-route-table은 a1, c1 각각 총 두 개 생성해야 하며 우선 `private-subnet-a1`에 대한 라우팅 테이블을 먼저 생성한다.
        2. 이번에는 `라우팅 편집`에서 첫번째 대상은 `0.0.0.0/0`, 두번째 대상은 `NAT 게이트웨이` 선택 후 `nat-gw-a1`을 선택한다.
        3. 서브넷 연결은 `private-subnet-a1`을 선택해준다.
        4. 저장한다. 위의 과정을 `private-subnet-c1`에 대해서도 똑같이 해준다.