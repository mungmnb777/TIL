# 1. DApp - Decentralized Application

- 탈중앙화된 P2P 네트워크 상에 백엔드 로직이 구동되는 응용프로그램
    - 블록체인 상의 스마트 컨트랙트가 기존의 중앙화된 서버에 의해 서비스를 제공하는 시스템 대체
- 좀 더 좁은 의미에서 DApp은 사용자 인터페이스를 통해 블록체인의 스마트 컨트랙트를 호출함으로써 동작하는 응용프로그램
- DApp = Frontend + Smart Contracts on Blockchain

<br>

# 2. 구성요소

1. 스마트 컨트랙트
    - 서비스 로직이 구현된 이더리움 네트워크에 배포된 바이트코드
2. 사용자 인터페이스
    - DApp의 사용자 인터페이스, 주로 HTML, CSS, JS 등 프론트엔드 기술로 구현
3. Web# API for Javascript
    - 이더리움 스마트 컨트랙트와 JS 코드 간의 상호작용 지원

<br>

# 3. web3.js

이더리움 네트워크와 상호작용할 수 있게 하는 JS 라이브러리 모음

<p align="center">
    <img src="../images/Day5_1.png"/>
    <br>
</p>

<br>

# 4. web3.js 실습

## 실습 환경

- ganache-cli 구동

```java
ganache-cli -d -m -p 7545 -a 5
```

## web3 객체 생성

```jsx
const Web3 = require('web3');

const ENDPOINT = 'http://localhost:7545';

const web3 = new Web3(new Web3.providers.HttpProvider(ENDPOINT));
```

## 네트워크 기본 정보 (1/3)

```jsx
web3.eth.net.getId()
.then(id => console.log("Network ID: ", id));
```

- 네트워크 ID - 현재 상호작용하는 노드가 속한 네트워크의 고유 번호

## 네트워크 기본 정보(2/3)

```jsx
web3.eth.net.getPeerCount()
.then(peerCount => console.log("No. of Peers: " , peerCount));
```

- 핑어 수 : 노드와 직접 연결되어 있는 피어의 수

## 네트워크 기본 정보(3/3)

```jsx
web3.eth.getBlockNumber()
.then(blockNo => console.log("Latest Block Number: ", blockNo));
```

- 현재 블록 번호 - 네트워크에서 생성된 가장 최근 블록의 번호

## 트랜잭션 생성

```jsx
web3.eth.sendTransaction({
		from: FROM_ADDRESS,
		to: TO_ADDRESS,
		value: VALUE_INF_WEI
})
.on('transactionHash', hash => {...})
.on('receipt', receipt => {...})
.on('confirmation', confirmNum => {...})
.on('error', console.error);
```

- 트랜잭션 객체
    - from : 보내는 주소
    - to : 받는 주소
    - value : 전송량 (wei 단위)
- 비동기 처리 지원
    - PromiEvent
        - sending
        - sent
        - transactionHash
        - receipt
        - confirmation
        - error

## 트랜잭션 결과 확인

```jsx
web3.eth.sendTransaction({
		from: FROM_ADDRESS,
		to: TO_ADDRESS,
		value: VALUE_INF_WEI
})
.on('confirmation', confirmNum => {
		web3.eth.getBalance(fromAddr)
		.then(balance => console.log(`${fromAddr}:${web3.utils.fromWei(balance, "ether)}
		ether`));
		...
})
.on('error', console.error);
```

- 잔액 확인
    - 보낸 주소, 받은 주소의 잔액 확인
    - 출력 시 ether 단위로 반환

<br>

# 과제 1. Web3를 통해 컨트랙트 배포

## 코드

```jsx
const Web3 = require('web3');

const ENDPOINT = 'http://localhost:7545';

const web3 = new Web3(new Web3.providers.HttpProvider(ENDPOINT));

const ACCOUNTS = [
    "0x90F8bf6A479f320ead074411a4B0e7944Ea8c9C1",
    "0xFFcf8FDEE72ac11b5c542428B35EEF5769C409f0",
    "0x22d491Bde2303f2f43325b2108D26f1eAbA1e32b",
    "0xE11BA2b4D45Eaed5996Cd0823791E0C93114882d",
    "0xd03ea8624C8C5987235048901fB614fDcA89b117"
]

const GAS_LIMIT = '6721975';

const ABI = [
	{
		"inputs": [
			{
				"internalType": "uint256",
				"name": "num",
				"type": "uint256"
			}
		],
		"name": "store",
		"outputs": [],
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [],
		"name": "retrieve",
		"outputs": [
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"stateMutability": "view",
		"type": "function"
	}
];

const BYTECODE = "608060405234801561001057600080fd5b50610150806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80632e64cec11461003b5780636057361d14610059575b600080fd5b610043610075565b60405161005091906100d9565b60405180910390f35b610073600480360381019061006e919061009d565b61007e565b005b60008054905090565b8060008190555050565b60008135905061009781610103565b92915050565b6000602082840312156100b3576100b26100fe565b5b60006100c184828501610088565b91505092915050565b6100d3816100f4565b82525050565b60006020820190506100ee60008301846100ca565b92915050565b6000819050919050565b600080fd5b61010c816100f4565b811461011757600080fd5b5056fea26469706673582212209a159a4f3847890f10bfb87871a61eba91c5dbf5ee3cf6398207e292eee22a1664736f6c63430008070033";

const contract = new web3.eth.Contract(ABI);

contract.deploy({
    data:BYTECODE
})
.send({
    from:ACCOUNTS[0],
    gas:GAS_LIMIT
})
.then(data => {
    console.log(data.options.address);
})
```

- `const ACCOUTS` : 해당 상수에는 ganache-cli를 실행시키고 나온 `Available Accounts`를 입력해주면 된다. 나의 경우에는 5개 전부 입력했지만 사실 이중 배포할 계정 하나만 입력해두어도 상관없다.


<p align="center">
    <img src="../images/Day5_2.png"/>
    <br>
    <em>그림 1) ganache-cli 실행 후 이용가능 계정 목록</em>
</p>

<br>

- `const GAS_LIMIT` : 해당 상수도 ganache-cli를 실행시키고 나온 `Gas Limit`를 입력해주면 된다.


<p align="center">
    <img src="../images/Day5_3.png"/>
    <br>
    <em>그림 2) Gas Limit</em>
</p>

<br>

- `const ABI` : `Remix`에서 `1_Storage.sol`을 컴파일한 후 ABI 버튼을 눌러 복사가 된다. 복사한 값을 코드에 그대로 붙여넣는다.


<p align="center">
    <img src="../images/Day5_4.png"/>
    <br>
    <em>그림 3) ABI 복사</em>
</p>

<br>

- `const BYTECODE` : 마찬가지로 Remix에서 Bytecode를 복사한다.

	
	<p align="center">
		<img src="../images/Day5_5.png"/>
		<br>
		<em>그림 4) Bytecode 복사</em>
	</p>

	<br>
    
    Bytecode를 그대로 붙여넣으면 이러한 `key-value` 값이 붙여넣어지는데 우리에게 필요한 값은 여러 값 중 `object`에 대한 `value`이다.
    
    따라서 다 지운 후

	<p align="center">
		<img src="../images/Day5_6.png"/>
		<br>
		<em>그림 5) object의 value값만 남긴 후 그림</em>
	</p>

	<br>
    
    이러한 형태로 사용하면 된다.
    
- 배포 코드
    
    ```jsx
    const contract = new web3.eth.Contract(ABI);
    
    contract.deploy({
        data:BYTECODE
    })
    .send({
        from:ACCOUNTS[0],
        gas:GAS_LIMIT
    })
    .then(data => {
        console.log(data.options.address);
    })
    ```
    
    - ABI를 이용하여 contract를 생성한다.
    - `deploy()` 메서드를 이용하여 contract를 로컬 ganache에 배포한다.
    - 이 때 ganache의 0번째 계정을 사용한다.

## 결과

<p align="center">
	<img src="../images/Day5_7.png"/>
	<br>
	<em>그림 6) 배포 완료!</em>
</p>

<br>


정상적으로 배포된 것을 확인할 수 있다.

<br>

# 과제 2. FundRaising 연동하기

## 배포

1. Remix에서 `FundRaising.sol`을 컴파일한다.

<p align="center">
	<img src="../images/Day5_8.png"/>
	<br>
	<em>그림 7) FundRaising 컴파일</em>
</p>

<br>

2. `DEPLOY & RUN TRANSACTIONS`에서 환경을 `Injected Provider - Metamask`로 설정한다.

<p align="center">
	<img src="../images/Day5_9.png"/>
	<br>
	<em>그림 8) Injected Provider 설정</em>
</p>

<br>

3. Metamask와 연동한다.

<p align="center">
	<img src="../images/Day5_10.png"/>
	<br>
	<em>그림 9) 메타마스크와 연동</em>
</p>

<br>

4. Metamask 계정으로 배포한다.
    
    `_DURATION` : 모금 시간 (단위 : 초)
    
    `_BENEFICIARY` : 모금 주인

<p align="center">
	<img src="../images/Day5_11.png"/>
	<br>
	<em>그림 10) 메타마스크 배포</em>
</p>

<br>

`transact` 버튼을 누르고 메타마스크에서 허용하면 여기까지 하면 배포가 완료되었다!

<p align="center">
	<img src="../images/Day5_12.png"/>
	<br>
	<em>그림 11) 배포 완료</em>
</p>

<br>

## 호출

```jsx
const Web3 = require('web3');

const ENDPOINT = 'https://ropsten.infura.io/v3/e032d12a06af4ecca636812a18b355d9';

const web3 = new Web3(new Web3.providers.HttpProvider(ENDPOINT));

const ACCOUNTS = [
    "0xE586AFAD4696b00Fb37c172E76ddc05c1b492b68"
]

const ABI = [
	{
		"inputs": [],
		"name": "func",
		"outputs": [],
		"stateMutability": "payable",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "uint256",
				"name": "_duration",
				"type": "uint256"
			},
			{
				"internalType": "address",
				"name": "_beneficiary",
				"type": "address"
			}
		],
		"stateMutability": "nonpayable",
		"type": "constructor"
	},
	{
		"inputs": [],
		"name": "withdraw",
		"outputs": [],
		"stateMutability": "payable",
		"type": "function"
	},
	{
		"inputs": [],
		"name": "beneficiary",
		"outputs": [
			{
				"internalType": "address",
				"name": "",
				"type": "address"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [],
		"name": "currentCollection",
		"outputs": [
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "address",
				"name": "",
				"type": "address"
			}
		],
		"name": "funderInfos",
		"outputs": [
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [],
		"name": "fundRaisingcloses",
		"outputs": [
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [],
		"name": "MINIMUM_AMOUNT",
		"outputs": [
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [],
		"name": "selectRandomFunder",
		"outputs": [
			{
				"internalType": "address",
				"name": "",
				"type": "address"
			},
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"stateMutability": "view",
		"type": "function"
	}
]

const call = () => {

    const CONTRACT_ADDRESS = '0x94074E232BA089398224e31C37bAA925b5dAA3f3';

    const contract = new web3.eth.Contract(ABI, CONTRACT_ADDRESS);

    contract.methods.beneficiary().call({from: ACCOUNTS[0]}).then(beneficiary => {
		console.log("모금 수혜자 ADDRESS : ", beneficiary);
	});
}

call();
```

### [Infura.io](http://Infura.io)에서 Ropsten 테스트 네트워크 엔드포인트 생성

Ropsten 테스트 네트워크 엔드포인트를 생성하려면 infura.io에서 프로젝트를 생성해야 한다.

1. Infura.io에 회원가입 후 대시보드 페이지에 들어온다. 우측 상단의 `CREATE NEW KEY`를 누른다.

	<p align="center">
		<img src="../images/Day5_13.png"/>
		<br>
		<em>그림 12) [Infura.io](http://Infura.io) 대시보드</em>
	</p>

	<br>
    

1. NETWORK는 `Web3 API`로 해주고 NAME은 원하는 걸로 한다. 나의 경우에는 그냥 `FundRaising`으로 했다.

	<p align="center">
		<img src="../images/Day5_14.png"/>
		<br>
		<em>그림 13) 프로젝트 생성</em>
	</p>

	<br>

1. 그러면 생성된 프로젝트 페이지로 들어오게 된다. `NETWORK ENDPOINTS`의 Ethereum이 초기에는 `MAINNET`으로 되어있는데 `ROPSTEN`으로 변경해준다. 그 후 주소를 복사한다.

	<p align="center">
		<img src="../images/Day5_15.png"/>
		<br>
	</p>

	<br>


1. 해당 주소를 `const ENDPOINT`에 넣어준다.
    
    ```jsx
    const ENDPOINT = 'https://ropsten.infura.io/v3/e032d12a06af4ecca636812a18b355d9';
    ```
    

1. `ACCOUNTS`와 `ABI`, `CONTRACT_ADDRESS`는 과제 1과 똑같은 방식으로 넣어준다.
2. 호출 코드를 입력한다.
    
    ```jsx
    const call = () => {
    
        const CONTRACT_ADDRESS = '0x94074E232BA089398224e31C37bAA925b5dAA3f3';
    
        const contract = new web3.eth.Contract(ABI, CONTRACT_ADDRESS);
    
        contract.methods.beneficiary().call({from: ACCOUNTS[0]}).then(beneficiary => {
    		console.log("모금 수혜자 ADDRESS : ", beneficiary);
    	});
    }
    
    call();
    ```
    

## 결과

호출이 잘 되는 것을 확인할 수 있다.

<p align="center">
	<img src="../images/Day5_16.png"/>
	<br>
	<em>그림 14) 호출 결과</em>
</p>

<br>