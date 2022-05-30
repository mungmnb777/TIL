# 1. 3-way handshake

<p align="center">
    <a href="https://ko.wikipedia.org/wiki/%EC%A0%84%EC%86%A1_%EC%A0%9C%EC%96%B4_%ED%94%84%EB%A1%9C%ED%86%A0%EC%BD%9C">
        <img src="../images/TCP_1.png"><br>
        <em>그림 1) TCP 헤더 구조</em>
    </a>
</p>

3-way handshake는 TCP가 두 엔드포인트간의 전송 과정에서 시퀀스 넘버를 동기화해서, 신뢰성 있는 연결을 하기 위해 가장 처음에 진행되는 프로세스이다.

TCP 헤더에서 Sequence number와 Acknowledgement number를 활용한다.

<p align="center">
    <a href="https://www.ccna6rs.com/secfnd/tcp-three-way-handshake/">
        <img src="../images/TCP_2.png"><br>
        <em>그림 2) 3-way handshake</em>
    </a>
</p>

Host A에서 Host B의 리소스를 얻기 위해 네트워크 통신을 하고자 할 때 3-way handshake가 어떤 프로세스로 이루어지는지 알아보자.

1. Host A에서 세그먼트의 SEQ 넘버를 100으로 설정하고 패킷, 프레임으로 감싸 Host B로 전송한다.
2. Host B에서 수신한 세그먼트의 SEQ 넘버가 100이라는 것을 확인하고, 확인했다는 의미로 ACK 넘버를 101(수신한 SEQ 넘버에 1을 더한 값)으로 설정하고, 자신의 SEQ 넘버인 300을 헤더에 설정한 후 Host A에게 전송한다.
3. Host A에서도 이를 확인하고 SEQ 넘버를 101, ACK 넘버를 301로 설정한 후 다시 Host B에 전송하면 TCP의 가상 연결이 완료된다.

이를 [Microsoft Docs](https://docs.microsoft.com/ko-kr/troubleshoot/windows-server/networking/three-way-handshake-via-tcpip)에서 제공하는 예시를 통해 실제 TCP 헤더에서는 어떻게 적용되는지 한번 알아보자.

```
1 2.0785 NTW3 --> BDC3 TCP ....S., len: 4, seq: 8221822-8221825, ack: 0,
win: 8192, src: 1037 dst: 139 (NBT Session) NTW3 --> BDC3 IP

TCP: ....S., len: 4, seq: 8221822-8221825, ack: 0, win: 8192, src: 1037
dst: 139 (NBT Session)

TCP: Source Port = 0x040D
 TCP: Destination Port = NETBIOS Session Service
 TCP: Sequence Number = 8221822 (0x7D747E)
 TCP: Acknowledgement Number = 0 (0x0)
 TCP: Data Offset = 24 (0x18)
 TCP: Reserved = 0 (0x0000)
 TCP: Flags = 0x02 : ....S.

TCP: ..0..... = No urgent data
 TCP: ...0.... = Acknowledgement field not significant
 TCP: ....0... = No Push function
 TCP: .....0.. = No Reset
 TCP: ......1. = Synchronize sequence numbers
 TCP: .......0 = No Fin

TCP: Window = 8192 (0x2000)
 TCP: Checksum = 0xF213
 TCP: Urgent Pointer = 0 (0x0)
 TCP: Options

TCP: Option Kind (Maximum Segment Size) = 2 (0x2)
 TCP: Option Length = 4 (0x4)
 TCP: Option Value = 1460 (0x5B4)

TCP: Frame Padding

00000: 02 60 8C 9E 18 8B 02 60 8C 3B 85 C1 08 00 45 00 .`.....`.;....E.
00010: 00 2C 0D 01 40 00 80 06 E1 4B 83 6B 02 D6 83 6B .,..@....K.k...k
00020: 02 D3 04 0D 00 8B 00 7D 74 7E 00 00 00 00 60 02 .......}t~....`.
00030: 20 00 F2 13 00 00 02 04 05 B4 20 20 .........
```

`NTW3`이라는 클라이언트는 `BDC3`이라는 서버와 TCP/IP 통신을 하기 위해 3-way handshake를 통해 연결을 하고자 한다. 운영체제가 세그먼트를 위와 같이 생성해준다. 이 때, 초기 시퀀스 넘버(ISN)을 지정해준다. 현재 ISN은 8221822로 서버에 초기 시퀀스 넘버로 보내질 정보이다.
