
![header](https://capsule-render.vercel.app/api?type=waving&color=257588&height=200&section=header&text=Street%20Fimder&fontSize=40&fontColor=fff)
![sf_logo_color_400](https://user-images.githubusercontent.com/107041228/201812159-ac12d5c4-e427-4ee3-bc5d-8e679a9fa71c.png)
### 스마트폰 카메라를 이용한 실시간 노면 이슈 Detect&Report 서비스

![](docs/pages.png)

- 광주인공지능사관학교 3기 파이널프로젝트 최우수상 수상작
- 개발기간 : 2022-11.14~12.14<br/>
- 개발목표<br/>
포장도로 유지보수 조사차량이 포장도로를 주행하는 동안, 이 차량에 탑재된 스마트폰 카메라를 이용하여 포장도로의 노면내, 실시간으로 포트홀을 검출하는 시스템을 개발한다

---

#### :wrench: 사용기술

<img src="docs/stack.png" width="500px"><br/>

- **Modeling** : Yolo5, CoreML, TFlite
- **Front** : Android Native (Kotlin), TFlite
- **Backend** : FastAPI, Google Firebase
- **Language** : Kotlin, Python

---

#### :chart_with_upwards_trend: Modeling

| Yolo V5를 위한 Test Image Training | Training된 model에 대한 평가 반복                                                                                                                                                                                                                  | Yolo V5로된 Pytoch용 model을 TFlite로 변환 |
|---------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------|
| ![](docs/modeling_01.png)       | Predict Image<br/>**( Yolov5로 Train 평가 중 Best Batch)**<br/> <img src="docs/modeling_02.png" style="width:300px;"> <br/>Labeled Image<br/>**( Yolov5로 Train 평가 중 Best Batch)**<br/> <img src="docs/modeling_02_2.png" style="width:300px;"> | ![](docs/modeling_03.png)           |


---

#### :iphone: 화면구성 및 주요기능

<img src="docs/MainActivity.png" width="300px"/>
<img src="docs/RecodingActivity.png" width="300px"/>
<img src="docs/RecResultActivity.png" width="300px"/> <br>

- **메인화면** : 실시간 노면 촬영을 위한 버튼, 등록 데이터 확인과 내정보 확인을 위한 버튼
- **촬영화면** : 실시간 노면 포트홀, 크랙 감지 및 촬영제어, 위도와 경도 표시
- **촬영결과** : 촬영 날짜와 시간, 촬영영상의 길이, 감지된 노면 포트홀과 크랙 표시 및 확인, 재촬영,촬영종료

<img src="docs/datalist_crack.png" width="300px">
<img src="docs/datalist_pothole.png" width="300px">
<img src="docs/datalist_delete.png" width="300px">

- **도로균열** : 촬영중 감지된 모든 크랙을 리스트로 표시
- **도로파임** : 촬영중 감지된 모든 포트홀을 리스트로 표시
- **등록데이터** : 촬영 후 등록된 모든 데이터를 리스트로 표시. 요소 스와이프시 삭제가능

---

#### :movie_camera: 시연영상

---

![](docs/newcoding_120.png)
#### :herb: 개발팀 : NEW CODING
  - **이호준** : Project Manager, CNN Object Model 설계, Model Translating
  - **이영웅** : CNN Object Model 설계 / 훈련 , Google App engine porting
  - **조승재** : TFlite model migration to Android, CameraX porting
  - **오지영** : Android App Ui Design, UX Scenario 설계
  
---
