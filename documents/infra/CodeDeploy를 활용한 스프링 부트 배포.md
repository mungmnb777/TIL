# CodeDeploy를 활용한 스프링부트 서버 배포

날짜: 2022년 11월 19일
태그: 공부

## 배포용 S3 버킷 생성

1. `S3` → `버킷` → `버킷 만들기`
2. 일반 구성
    - 버킷 이름 : 원하는 걸로 설정한다.
    - AWS 리전 : `아시아 태평양(서울) ap-northeast-2`
3. 객체 소유권은 `ACL 비활성화됨(권장)`
4. 이 버킷의 퍼블릭 액세스 차단 설정은 모든 퍼블릭 액세스 차단을 체크 해제한다.
    
    ![Untitled](../images/CodeDeploy%20BE_1.png)
    
5. 버킷 버전 관리는 `비활성화`
6. 태그
    - 키 : Name
    - 값 : 원하는 이름
7. 기본 암호화는 `비활성화`
8. 버킷 생성 후 해당 버킷 클릭 → `권한` → `버킷 정책`에서 우측에 있는 편집 버튼을 누른다.
9. 버킷 정책 편집에서 우측에 있는 `정책 생성기`를 누른다.
10. 다음과 같이 입력한다.
    
    ![Untitled](../images/CodeDeploy%20BE_2.png)
    
    - Select Type of Policy : `S3 Bucket Policy`
    - Effect : `Allow`
    - Principal : `*`
    - AWS Service : `Amazon S3`
    - Actions : `GetObject`
    - Amazon Resource Name (ARN) : 이전 정책 편집 페이지에서 `버킷 ARN`을 복사한다. 추가로 `/*`를 붙여준다. 즉, 다음과 같은 형태가 된다. `버킷 ARN/*`
11. 정책을 생성한 후 나오는 JSON Document를 복사한 뒤 이전 버킷 정책 편집 페이지에 입력한다.
    
    ![Untitled](../images/CodeDeploy%20BE_3.png)
    

## CodeDeploy 설정

### 애플리케이션 생성

1. `CodeDeploy` → `애플리케이션` → `애플리케이션 생성`
2. 애플리케이션 이름은 원하는 이름으로 설정한다.
3. 컴퓨팅 플랫폼은 `EC2/온프레미스`로 설정한다.

### IAM 역할 생성

1. `IAM` → `액세스 관리` → `역할` → `역할 만들기`
2. 신뢰할 수 있는 엔티티 유형 : `AWS 서비스`
3. 사용 사례 : 다른 AWS 서비스의 사용 사례 → `CodeDeploy`
4. 권한 추가에서는 바로 `다음` 버튼을 누른다.
5. 권한을 생성한다.

### 배포 그룹 생성

1. `CodeDeploy` → `애플리케이션` → `애플리케이션 생성` → `생성한 애플리케이션` → `배포 그룹` → `배포 그룹 생성`
2. 배포 그룹 이름은 원하는 이름으로 입력한다.
3. 서비스 역할은 위에서 생성한 CodeDeployRole로 설정해준다.
4. 배포 유형은 `현재 위치`로 한다.
5. 환경 구성은 `Amazon EC2 Auto Scaling 그룹`으로 설정하고 이전에 생성한 오토스케일링 그룹으로 설정한다.
6. 배포 설정은 `CodeDeployDefault.AllAtOnce`로 한다.
7. 로드 밸런서는 활성화하고 `Application Load Balancer 또는 Network Load Balancer` 체크 후 이전에 생성한 대상 그룹을 선택한다.

### 배포 구성 생성

1. `CodeDeploy` → `배포 구성` → `배포 구성 생성`
2. 배포 구성 이름은 원하는 것으로 설정한다.
3. 컴퓨팅 플랫폼은 `EC2/온프레미스`
4. 최소 정상 호스트는 `숫자`
5. 값은 `1`로 설정한 후 생성한다.

## Jenkins 설정

1. 우선 `Jenkins 관리` → `플러그인 관리` → `Available plugins`로 들어가서 CodeDeploy 플러그인을 설치한다.
2. 설치 후 `새로운 Item`에서 `Freestyle project`를 선택하고 OK를 누른다.
3. 소스 코드 관리는 `Git`을 체크한다. 
    
    ![Untitled](../images/CodeDeploy%20BE_4.png)
    
    - Repository URL : 깃랩의 리포지토리 URL을 입력한다.
    - Credentials : 계정 정보를 입력한다. ID와 비밀번호로 해도 되고, 토큰을 이용하여 인증해도 된다.
    - Branches to build : develop으로 설정한다.
    - 나머지는 그대로 둔다.
4. 빌드 유발에서 `Build when a change is pushed to Gitlab`을 체크한다.
    - Enabled Gitlab triggers : Push Events만 체크한다.
    - 그리고 고급 버튼을 눌러 아래쪽의 Allowed branches를 `Filter branches by name`으로 체크하고 Include에 `develop`을 입력한다. 에러메시지가 떠도 무시한다.
    - 맨 아래의 Secret token의 Generate 버튼을 눌러 복사한다.
5. Gitlab 리포지토리 페이지로 이동한다. `Settings` → `Webhooks`에 가서 URL과 Secret token을 입력한다. URL은 `Build when a change is pushed to Gitlab` 우측에 적혀있다.
    
    ![Untitled](../images/CodeDeploy%20BE_5.png)
    
6. 빌드 환경은 그대로 둔다.
7. Build Steps에서 Execute Shell을 열고 다음과 같은 코드를 입력한다.
    
    ```docker
    cd BE/shall-we-meet-then
    
    cp /var/jenkins_home/properties/application-aws.properties ${WORKSPACE}/BE/shall-we-meet-then/src/main/resources/application-aws.properties
                            
    cp /var/jenkins_home/properties/application-mail.properties ${WORKSPACE}/BE/shall-we-meet-then/src/main/resources/application-mail.properties
    
    chmod +x gradlew
    
    ./gradlew clean build -x test
    
    cp build/libs/*.jar /var/jenkins_home/workspace/final-project-aws-cicd
    cp appspec.yml /var/jenkins_home/workspace/final-project-aws-cicd
    cp scripts -r /var/jenkins_home/workspace/final-project-aws-cicd
    ```
    
8. 빌드 후 조치에서 `Deploy an application to AWS CodeDeploy`를 선택한다.
    - AWS CodeDeploy Application Name : 위에서 생성한 CodeDeploy 애플리케이션의 이름을 입력한다.
    - AWS CodeDeploy Deployment Group : 위에서 생성한 배포 그룹의 이름을 입력한다.
    - AWS CodeDeploy Deployment Config : 위에서 생성한 배포 구성의 이름을 입력한다.
    - AWS Region : `AP_NORTHEAST_2`를 입력한다.
    - S3 Bucket : 위에서 생성한 S3 버킷의 이름을 입력한다.
    - Include Files : `*.jar, appspec.yml, scripts/*`을 입력한다.
    - Use Access/Secret Keys는 다음과 같은 권한을 가지고 있는 IAM 사용자를 생성해 해당 사용자의 Access Key와 Secret Key를 입력해준다.
        
        ```docker
        {
            "Version": "2012-10-17",
            "Statement": [
                {
                    "Effect": "Allow",
                    "Action": [
                        "autoscaling:*",
                        "codedeploy:*",
                        "ec2:*",
                        "lambda:*",
                        "elasticloadbalancing:*",
                        "s3:*",
                        "cloudwatch:*",
                        "logs:*",
                        "sns:*"
                    ],
                    "Resource": "*"
                }
            ]
        }
        ```