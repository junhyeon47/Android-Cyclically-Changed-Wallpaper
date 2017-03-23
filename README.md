# CC 배경화면(Cyclically Changed Wallpaper)

### 소개
여러 개의 사진 또는 이미지를 선택하여 주기적으로 배경화면을 자동으로 변경해주는 앱</br>
Play Store: [https://play.google.com/store/apps/details?id=com.boostcamp.hyeon.wallpaper](https://play.google.com/store/apps/details?id=com.boostcamp.hyeon.wallpaper)


### 기능
1. 여러 개의 배경화면 이미지 선택: 배경화면으로 사용할 여러 이미지를 선택하고, 미리보기, 확대보기 기능을 지원합니다.</br>
![Alt text](/docs/images/app_desc_01.png)
![Alt text](/docs/images/app_desc_02.png)
2. 배경화면 변경 시간 선택: 여러 개의 이미지를 배경화면으로 설정할 경우, 5분에서 24시간까지 변경 시간을 설정할 수 있으며, 스마트폰의 화면이 켜질 때마다 변경할 수 있는 기능을 지원합니다.</br>
![Alt text](/docs/images/app_desc_03.png)
![Alt text](/docs/images/app_desc_04.png)
3. 배경화면 검색 및 다운로드: 네이버 이미지 검색 API를 이용하여 쉽고 빠르게 배경화면을 다운로드합니다.</br>
![Alt text](/docs/images/app_desc_05.png)
![Alt text](/docs/images/app_desc_06.png)
4. 현재 설정된 배경화면 정보를 한눈에 볼 수 있는 기능: 현재 배경화면으로 지정된 모든 사진을 순서대로 볼 수 있고, 스위치 버튼을 통해서 배경화면 설정을 해제하는 기능, 배경화면 변경 주기를 바꾸는 기능, 무작위로 배경화면을 변경하는 기능을 지원합니다.</br>
![Alt text](/docs/images/app_desc_07.png)

### 사용한 라이브러리
* [Realm](https://realm.io/kr/)
* [Butter Knife](http://jakewharton.github.io/butterknife/)
* [Retrofit](http://square.github.io/retrofit/)
* [AV Loading Indicator View](https://github.com/81813780/AVLoadingIndicatorView)
* [Round Corner Progress Bar](https://github.com/akexorcist/Android-RoundCornerProgressBar)
* [Sports Dialog](https://github.com/d-max/spots-dialog)
* [Photo View](https://github.com/chrisbanes/PhotoView)
* [Material Intro Screen](https://github.com/TangoAgency/material-intro-screen)
* [Glide](http://gun0912.tistory.com/17)

### 사용한 Open API
* 네이버 검색 API

### 기술적인 특징
* MVP pattern 적용
* Notification 제거한 Foreground Service 개발
* Boot Complete 적용한 BroadcastReceiver 개발
* Glide 사용으로 이미지 출력에 대한 리소스를 최적화 및 캐시 적용
* 이미지의 크기에 따라서 샘플링하여 배경화면을 변경하는데 필요한 리소스를 최적화
* 머티리얼 디자인 적용
