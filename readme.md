# 🌜 잘자라

## 📅 프로젝트 진행 기간
- 2023.04.10(월) ~ 2023.05.19(금)

## 👓기획 배경
- 처음 스마트폰을 접하는 시기가 점점 빨라지면서 이에 따른 문제도 많아지고 있습니다. 특히, 어린 나이에 스마트폰을 사용하는 경우에 스스로 절제하지 못 하고 과도하게 사용함으로써 성장기의 아이들에게 필수적인 충분한 수면을 취하는 데에도 방해가 되고 있습니다. 실제로, 스마트폰을 과도하게 사용하는 아동이 그렇지 않은 아동에 비해 수면시간도 적고, 수면의 질도 좋지 못한 것으로 나타났습니다.
저희는 과도한 스마트폰 사용으로 수면에 불편을 겪고 있는 아이들과 그 부모들을 대상으로 부모의 도움과 아이들의 자발적인 참여를 통해 올바른 수면 습관을 길러주는 서비스를 제공하고자 합니다.

## 🥅 개요
- 스마트폰을 과도하게 사용하면서 충분한 수면을 취하지 못 하거나, 올바른 수면 습관을 기르고 싶은 아이들과 그러한 아이들을 키우는 부모님들을 위한 내 자녀의 올바른 수면 습관 서포팅 서비스입니다. 


## 🎯 타겟
- 유아 ~ 초등학교 저학년의 자녀를 둔 부모님과 자녀
- 수면 관리가 필요로한 자녀를 두고 있는 부모님과 자녀


## Usage

````
git clone https://lab.ssafy.com/s08-final/S08P31A802.git

이후 exec폴더의 포팅메뉴얼을 따라 진행

````

## 📱 기술 스택

### Frontend

<img width="665" alt="front" src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/f6ed52da-f265-4495-b293-1d7bf6fc8456">

### Backend

<img width="1222" alt="backend" src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/b5f6dab2-4ccc-4b4f-92f9-db8aa51e4b81">

### CI/CD

![KakaoTalk_Photo_2023-05-18-16-14-03](https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/7efa6260-b790-47b5-b442-4aad4cfbe789)

## 개발환경
<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/64738942/a815d69c-d80a-4a87-b593-183125e7966d" title="개발환경"/>

## 📂디렉토리 구조

<details>
  <summary>
  백엔드 디렉토리 구조
  </summary>

      jaljara
      ┣ api
      ┃ ┣ controller
      ┃ ┣ dto
          ┣ request
          ┗ response
      ┃ ┗ service
      ┣ common
      ┣ config
      ┣ db
      ┃ ┣ entity
      ┃ ┗ repository
      ┗ jaljaraApplication.java
 </details>


<details>
  <summary>
  프론트엔드 디렉토리 구조
  </summary>

    Jaljara
      ┗ app
        ┗ src
          ┗ main
            ┣ java
            ┃ ┗ com
            ┃   ┗ ssafy
            ┃     ┗ jaljara
            ┃       ┣ activity
            ┃       ┃ component
            ┃       ┃ data
            ┃       ┃ manager
            ┃       ┃ network
            ┃       ┃ receiver
            ┃       ┃ ui
            ┃       ┃ ┣ enumType
            ┃       ┃ ┃ screen
            ┃       ┃ ┃ ┣ child
            ┃       ┃ ┃ ┃ common
            ┃       ┃ ┃ ┃ parent
            ┃       ┃ ┃ theme
            ┃       ┃ ┗ vm
            ┃       ┗ utils
            ┗ res
              ┣ drawable
              ┃ drawable-nodpi
              ┃ drawable-v24
              ┃ font
              ┃ mipmap-anydpi-v26
              ┃ mipmap-hdpi
              ┃ mipmap-mdpi
              ┃ mipmap-xhdpi
              ┃ mipmap-xxhdpi
              ┃ mipmap-xxxhdpi
              ┃ values
              ┗ xml
    

</details>

## ✔️ 주요기능 및 화면

### 아이페이지

|회원가입| 로그인 | 미션 수행 |
| :---: |  :---: | :---: |
|<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/d34387a8-124a-48c3-aa2f-5dace9007cf9" width="100%" height="100%"/> |<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/5e27a39e-6d15-4932-a1a6-a6ca9f1b9292" width="100%" height="100%"/>|<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/6d5ccf14-99ed-4ef2-a2b4-8a18a947f18f" width="100%" height="100%"/>|
|구글과 카카오로 회원가입을 할 수 있습니다.|구글 혹은 카카오로 로그인을 할 수 있습니다.|오늘의 미션을 확인하고, 미셔 리롤, 미션을 수행을 할 수 있습니다.|

<br/>

|보상 등록 전| 보상 등록 후| 보상 획득 |
| :---: |  :---: | :---: |
|<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/1d0a80ac-240b-459d-b5f6-4c2d03f873d6" width="100%" height="100%"/> |<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/6651e9f2-1265-4732-9e5d-0d9f1cb6c052" width="100%" height="100%"/>|<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/cdace5ba-79c8-4a44-9689-b0d5cd70c09d" width="100%" height="100%"/>|
|부모가 보상을 등록하기 전 화면입니다.| 부모가 보상을 등록했을 시 화면입니다.|보상 획득이 가능하고 보상이 등록되어있으면, 보상(쿠폰)을 획득합니다.|


<br/>

|쿠폰 사용 화면| 컨텐츠 화면 | 수면 시간 알림 |
| :---: |  :---: | :---: |
|<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/4ecf29f2-0fa5-4336-9340-d57311bbe3ba" width="100%" height="100%"/> |<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/2822c692-414f-41e5-a4fe-2652a054c502" width="100%" height="100%"/> | <img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/9c678d1a-f73c-4e30-bef7-d03a201cc5a7" width="100%" height="100%"/> |
|획득한 보상을 쿠폰함에서 확인이 가능하고 사용할 수 있습니다.| 수면에 도움이 되는 영상을 시청할 수 있습니다.| 아이에게 설정된 수면시간이 되면 알림을 통해 어플에 접속할 수 있습니다. |

<br/>



### 부모페이지

</br>


|로그인| 자녀 초대 | 아이 미션 승인|
| :---: |  :---: | :---: |
|<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/8d3fe45e-2cac-4f0f-bb08-13eacd0d3cf3" width="100%" height="100%"/> |<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/2b6929ae-ed52-4ddd-a423-b8b48de10c9c" width="100%" height="100%"/>|<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/3b333667-6a58-44f8-8027-485ac33dbc02" width="100%" height="100%"/>|
|구글과 카카오로 로그인할 수 있습니다.|자녀가 회원가입시 부모의 코드를 입력하면 부모와 자녀가 연결됩니다.|아이별 수행한 미션을 확인하고 수행할 수 있습니다.|

</br>


|자녀 보상 등록| 자녀 수면 시간 설정 | 자녀 수면 기록 확인|
| :---: |  :---: | :---: |
|<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/63650440-5d2c-43c4-b0cc-ca8e8643a3cc" width="100%" height="100%"/> |<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/8d7b9bbf-06f3-486f-a0dd-63f3392ae999" width="100%" height="100%"/>|<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/50399088/cab3ad35-80a5-4be8-a847-8e1d469e2926" width="100%" height="100%"/>|
|부모는 자녀에게 보상을 등록할 수 있습니다.|자녀의 수면 시간을 설정할 수 있습니다.|아이 수면 기록을 달력에서 확인할 수 있습니다. 해당 화면에서 그날의 수면 기록과 수행한 미션을 확인할 수 있습니다.|


## 🎨 화면 설계서

<a href="https://www.figma.com/file/zJijKfweApIWq2YzWEv7Bh/Untitled?node-id=1%3A414&t=30yaRC1NTQS1BKok-1">
    <img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/49721857/c63453cb-5dac-4f88-96ab-12dfb8dae962" title="화면설계서로 이동"/>
</a>

## 💭 요구사항 정의서

<a href="https://docs.google.com/spreadsheets/d/1ZO1scKWQl4XKs21f9Sp6lwJidCWj_17RKK71B_5C41U/edit?usp=sharing">
    <img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/49721857/89d98836-1b02-4cd6-9d97-eef74c83df58" title="요구사항 정의서로 이동"/>
</a>

## 🛢︎ ERD

![ERD](https://github.com/leehyeji319/Android-Kotlin-Study/assets/49721857/bd29b7f3-3572-499c-aa06-cc12605fc5e3)

## 📜 API 설계서

<a href="https://www.notion.so/3a578dcf522548628c7175fcb87e572f?v=93cfbff1d0e847348aee8c74faffd9f0">
    <img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/64738942/5bfdfeb9-f337-495a-b9ec-a46afe5c049d" title="API 설계서로 이동"/>
</a>



## Team Members

<div align="left">
  <table>
    <tr>
        <td align="center">
        <a href="">
          <img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/49721857/39cac67c-9040-42b9-b0f5-1840826bf4e9" alt="이혜지 프로필" width=120 height=120 />
        </a>
      </td>
      <td align="center">
        <a href="">
          <img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/49721857/46917169-7d9c-49ec-a6c2-a6935d80a3a9" alt="고진석 프로필" width=120 height=120 />
        </a>
      </td>
      <td align="center">
        <a href="">
          <img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/49721857/f83a4f64-9c36-42c3-ae1c-4d501729a030" alt="변준우 프로필" width=120 height=120 />
        </a>
      </td>
      <td align="center">
        <a href="">
          <img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/49721857/f5511ca9-9338-4c01-ad6a-fb9d4653f699" alt="오종석 프로필" width=120 height=120 />
        </a>
      </td>
      <td align="center">
        <a href="">
          <img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/49721857/2fe8acb6-cace-43f4-ac29-1f9e9a6b62c4" alt="최지연 프로필" width=120 height=120 />
        </a>
      </td>
      <td align="center">
        <a href="">
          <img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/49721857/a459c191-614a-4ac2-918b-2dc76de13704" alt="황수정 프로필" width=120 height=120 />
        </a>
      </td>
    </tr>
    <tr>
      <td align="center">
        <a href="https://github.com/leehyeji319">
          이혜지
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/9jinseok">
          고진석
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/movebxeax">
          변준우
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/jongseok-oh">
          오종석
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/jiyeon5">
          최지연
        </a>
      </td>
        <td align="center">
        <a href="https://github.com/sujunghwang">
          황수정
        </a>
      </td>
    </tr>
    <tr>
      <td align="center">
          팀장, BE, Android
      </td>
      <td align="center">
          BE, Android
      </td>
      <td align="center">
          BE, Android, Infra
      </td>
      <td align="center">
          Android, BE
      </td>
      <td align="center">
          Android, BE
      </td>
      <td align="center">
          Android, BE
      </td>
    </tr>
  </table>
</div>
