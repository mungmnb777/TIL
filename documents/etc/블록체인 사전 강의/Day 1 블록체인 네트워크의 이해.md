
# 1. 이더리움 블록체인 네트워크의 분류

## 프라이빗 네트워크

로컬 PC에서 이더리움을 구동하거나, 몇몇의 사용자들만이 접속할 수 있는 이더리움 네트워크

테스트용이나 실습용으로만 사용한다.

## 퍼블릭 네트워크

### 메인넷

거래소 상에서 거래를 하거나, 스마트 컨트랙트를 배포를 실제로 하게 되는 네트워크

### 테스트넷

메인넷에 무언가를 하기 전에 미리 이더리움을 경험해 볼 수 있는 테스트용 네트워크

우리는 `롭슨`이라는 테스트넷을 이용해서 실습한다.

# 2. 이더리움 네트워크 개념도

<p align="center">
    <img src="../images/Day1_1.png"/>
</p>

# 3. 환경설정

## Chocolatey

윈도우 패키지 매니저인 `Chocolatey`를 먼저 설치한다.

1. `Win + R`을 누른 후 `Powershell`을 입력한다.
2. [`https://chocolatey.org/install`](https://chocolatey.org/install)으로 이동하여 중간에 위치한 Command를 복사해서 Powershell에 입력한다.

    <p align="center">
        <img src="../images/Day1_2.png"/>
    </p>
    

## Prerequisites

- 다음의 명령어를 Powershell에 입력하여 전부 설치해준다.
    
    ```
    choco install git -y
    
    choco install golang -y
    
    choco install mingw -y
    ```
    

## Geth

1. 다음의 명령어를 입력하여 Geth를 설치한다.
    
    ```
    mkdir src\github.com\ethereum
    
    git clone https://github.com/ethereum/go-ethereum --branch v1.9.24 src\github.com\ethereum\go-ethereum
    
    cd src\github.com\ethereum\go-ethereum
    
    go get -u -v golang.org/x/net/context
    
    go install -v ./cmd/...
    ```
    
2. Geth가 성공적으로 설치되었는지 확인한다.
    
    ```
    geth version
    ```
    

## Ganache

- node.js를 설치한다.
    
    ```
    choco install nodejs-lts
    ```
    
- ganache-cli를 설치한다.
    
    ```
    npm install -g ganache-cli
    ```
    
- 설치가 완료되었는지 확인한다
    
    ```
    ganache-cli --version
    ```
    

## 지갑 프로그램 설치 - Metamask

<p align="center">
    <img src="../images/Day1_3.png"/>
</p>

[여기](https://metamask.io/)를 눌러 설치하세용

# 4. 로컬 네트워크 활용 및 실습

## 가나슈 구동

- 로컬 테스트 넷 구동
    
    ```
    ganache-cli -d -m -p 7545 -a 5
    ```
    
    - `-d -m (--deterministic --mnemonic)` HD Wallet 생성시 니모닉 구문 사용
    - `-p (--port)` 포트 지정 (default는 8545)
    - `-a (--acount)` 구동 시 생성할 계정 수 (default는 10개)
- 명령어 옵션 확인
    
    ```
    ganache-cli --help
    ```
    

## Geth로 네트워크에 접속

- geth 명령어로 가나슈 테스트넷에 접속 (새로운 명령 프롬프트로 켜야함!)
    
    ```
    geth attach http://localhost:7545
    ```
    

## 네트워크 기본 사항 확인

- 연결성 확인
    
    ```
    net.listening
    
    net.peerCount
    ```
    
- 계정 목록 확인
    
    ```
    eth.accounts
    ```
    
- 계정 보유 잔액 확인
    
    ```
    web3.fromWei(eth.getBalance(eth.accounts[0]))
    ```
    

## 네트워크 정보 입력

- metamask에서 네트워크를 현재 URL로 매핑시켜준다.
- RPC URL : http://localhost:7545
- geth console에서 chain id 확인
    
    ```
    eth.chainId()
    ```
    

## 메타마스크 계정으로 이더 전송

```
tx = { from: "가나슈 제공 계정 중 하나", to: "메타마스크 계정", value: 1e18"}

eth.sendTransaction(tx)
```

<p align="center">
    <img src="../images/Day1_4.png"/>
</p>

메타마스크에 1 ETH가 도착해있다는 것을 확인할 수 있다!

## 단위 환산