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
## 시스템 아키텍처
<img src="https://github.com/leehyeji319/Android-Kotlin-Study/assets/64738942/48c59dfb-f186-454d-b235-201fc75ff6bb" title="시스템 아키텍처"/>

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

### 부모페이지


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
