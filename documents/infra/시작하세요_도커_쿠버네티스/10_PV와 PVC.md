# 퍼시스턴트 볼륨과 퍼시스턴트 볼륨 클레임

날짜: 2023년 2월 21일
책: 시작하세요! 도커/쿠버네티스
카테고리: infra

## hostPath, emptyDir

- `hostPath` : 호스트와 볼륨 공유
    
    → 유용한 경우 : 모든 노드에 연결해야하는 볼륨인 경우
    
- `emptyDir` : 포드의 컨테이너 간 볼륨 공유

## 네트워크 볼륨

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: nfs-pod
spec:
  containers:
    - name: nfs-mount-container
      image: busybox
      args: [ "tail", "-f", "/dev/null" ]
      volumeMounts:
      - name: nfs-volume
        mountPath: /mnt
  volumes:
  - name : nfs-volume
    nfs:
      path: /
      server: {NFS_SERVICE_IP}
```

```yaml
/ # df -h
Filesystem                Size      Used Available Use% Mounted on
overlay                 251.0G      9.3G    228.9G   4% /
tmpfs                    64.0M         0     64.0M   0% /dev
tmpfs                     3.1G         0      3.1G   0% /sys/fs/cgroup
10.96.76.145:/          251.0G      9.3G    228.9G   4% /mnt
/dev/sdc                251.0G      9.3G    228.9G   4% /etc/hosts
/dev/sdc                251.0G      9.3G    228.9G   4% /dev/termination-log
/dev/sdc                251.0G      9.3G    228.9G   4% /etc/hostname
/dev/sdc                251.0G      9.3G    228.9G   4% /etc/resolv.conf
shm                      64.0M         0     64.0M   0% /dev/shm
tmpfs                     6.1G     12.0K      6.1G   0% /var/run/secrets/kubernetes.io/serviceaccount
tmpfs                     3.1G         0      3.1G   0% /proc/acpi
tmpfs                    64.0M         0     64.0M   0% /proc/kcore
tmpfs                    64.0M         0     64.0M   0% /proc/keys
tmpfs                    64.0M         0     64.0M   0% /proc/timer_list
tmpfs                    64.0M         0     64.0M   0% /proc/sched_debug
tmpfs                     3.1G         0      3.1G   0% /sys/firmware
```

## PV, PVC

### 왜 쓸까?

MySQL에서 NFS를 쓴다고 가정해보자. 만약에 Yaml 파일을 공유해야한다면? Yaml 파일에 NFS의 정보가 들어가기 때문에 파드를 올리기 위해서는 NFS 정보를 알아야만 한다.

→ 즉, 볼륨과 애플리케이션을 분리할 수 없다.

PV와 PVC는 볼륨의 세부적인 사항을 추상화하여 볼륨이 무엇이든 사용하게 해줌

약간 자바로 따지면 인터페이스로 다형성을 적용한 느낌인듯

### 과정

1. 퍼시스턴트 볼륨을 등록한다. 이 때 NFS나 AWS EBS 등 다양한 스토리지 서비스를 퍼시스턴트 볼륨으로 설정할 수 있다.
2. 개발자가 퍼시스턴트 볼륨 클레임을 요청한다. 요청할 때 볼륨 클레임에 원하는 볼륨 조건을 작성해 전달한다.
3. 퍼시스턴트 볼륨이 해당 조건에 부합한다면 두 리소스가 연결된다.
4. 파드의 컨테이너에 해당 볼륨 클레임으로 전달받은 볼륨을 마운트한다.

### 볼륨 조건

| accessModes | 축약어 | 설명 |
| --- | --- | --- |
| ReadWriteOnce | RWO | 1:1 마운트만 가능, 읽기 쓰기 가능 |
| ReadOnlyMany | ROX | 1:N 마운트 가능, 읽기 전용 |
| ReadWriteMany | RWX | 1:N 마운트 가능, 읽기 쓰기 가능 |

근데 설정을 해놔도 엉뚱한 설정으로 볼륨이 생성될 수도 있음 → 이러면 예측하기 어려울 거 같은데 왜 이렇게 만들었을까???

→ 설정은 인프라 관리자가 제대로 잘해야함.

### PV의 라이프사이클

- Available : PV를 생성한 직후
- Bound : PVC와 연결되었을 때
- Released : 연결된 PVC가 삭제되었을 때

### Reclaim Policy

- Retain : 원격 스토리지에 데이터를 계속해서 보존
- Delete : 퍼시스턴트 볼륨 클레임의 사용이 끝나면 PV도 삭제
- Recycle : 볼륨의 데이터를 모두 삭제한 후 PV를 Available로 만들어줌 → Deprecated

### Dynamic Provisioning

퍼시스턴트 볼륨을 사용하려면 미리 외부 스토리지를 준비해야함
(EBS 생성 후 Yaml 파일에 EBS 볼륨 ID 입력)

다이나믹 프로비저닝은 PVC가 요구하는 조건과 잋리하는 PV가 없으면 자동으로 퍼시스턴트 볼륨과 외부 스토리지를 프로비저닝하여 생성해줌

다이나믹 프로비저닝은 스토리지 클래스의 정보를 참고해 외부 스토리지를 생성함(스토리지 클래스의 provisioner에 외부 스토리지 종류를 입력해야함)
→ 개발자가 PVC를 요청할 때 스토리지 클래스를 입력 → 스토리지 클래스에 PV가 없으면 다이나믹 프로비저닝

```yaml
kind: StorageClass
apiVersion: storage.k8s.io/v1
metadata:
  name: fast
provisioner: kubernetes.io/aws-ebs
parameters:
  type: gp2
  fsType: ext4
  zones: ap-northeast-2a # 여러분의 쿠버네티스 클러스터가 위치한 가용 영역을 입력합니다.
```

AWS, GCP를 사용하지 않으면 다이나믹 프로비저닝을 어떻게 활성화할 수 있을까?

→ ceph 활용