# Collector - review collector
<img src="https://user-images.githubusercontent.com/81304917/183879854-abe70d07-488d-4871-86d1-477848ffe94b.png" width="100">  

종종 생각의 저편에서 영화나 책의 줄거리가 떠오르곤 하지만 정작 제목을 몰라 다시 꺼내보지도 못한 채 기억 속에 묻어두곤 합니다. Collector은 이러한 아쉬움을 극복하기 위해 고안되었습니다. 영화, 책, 장소 등 다양한 카테고리를 리뷰하고 카드 형식으로 수집하는 앱입니다.

플레이스토어 링크 https://play.google.com/store/apps/details?id=com.han.collector

### 기능
<img src="https://user-images.githubusercontent.com/81304917/180639588-0c882de1-3a45-440e-b739-d91a31c58c17.png" width="700">

- `로그인 화면` : 구글 계정으로 로그인합니다.  
- `홈 화면` :   
모든 카테고리의 리뷰들을 전체적으로 확인할 수 있습니다.  
메뉴를 열어 카테고리를 바꾸거나, 로그아웃, 탈퇴가 가능합니다.  
- `리스트 화면` :   
카테고리 별로 작성한 리뷰들을 모아 볼 수 있습니다.  
제목이나 리뷰 내용으로 검색이 가능합니다.  
날짜, 별점 순으로 정렬을 변경할 수 있습니다.  
좋아요를 표시한 리뷰들만 모아볼 수 있습니다.  
리뷰들을 다중 선택하여 삭제할 수 있습니다.
- `검색 화면` :   
리뷰를 작성하고자 하는 영화나 책을 실시간으로 검색할 수 있습니다.  
검색결과가 많을 시에 끊임없는 스크롤이 이루어집니다.
- `추가/편집 화면` : 리뷰를 작성하여 추가합니다.
- `상세 화면` : 작성한 리뷰를 카드를 뒤집어 확인할 수 있습니다. 


|**홈 화면**  |**검색 화면** |**리뷰 추가 + 상세화면**  |**리뷰 사진 추가 기능**  |
|---|---|---|---|
|<img src="https://user-images.githubusercontent.com/81304917/183877556-deb69fc5-0864-42b8-9a1b-81d08f9b1440.jpg" width="200">|<img src="https://user-images.githubusercontent.com/81304917/183876311-6fa9caf0-2921-4343-a5d4-6eb261757a9a.gif" width="200"> |<img src="https://user-images.githubusercontent.com/81304917/183876983-6d26bfd2-2dd9-459e-9f2d-aab1e38bccda.gif" width="200"> |<img src="https://user-images.githubusercontent.com/81304917/183875788-1b506491-7360-460f-8151-139c0f9a8560.gif" width="200"> |  

|**리뷰 정렬 기능** |**리뷰 검색 기능**  |**리뷰 삭제 기능**   |
|---|---|---|
|<img src="https://user-images.githubusercontent.com/81304917/183876133-49718758-12b9-4149-b3b3-7e80d507f735.gif" width="200"> |<img src="https://user-images.githubusercontent.com/81304917/183877170-e5cda4dc-25be-4731-a0a3-f990584ad81f.gif" width="200"> |<img src="https://user-images.githubusercontent.com/81304917/183876027-fc99ee7a-ccd8-4de0-8f33-002315a7d9d6.gif" width="200"> |
 
    

### 패키지 구조
- model
  - data
    - database
    - remote
        - api
        - dataSource
        - model
  - module
  - repository
- viewmodel
- view
  - activities
  - fragments
  - adapters
- utils

### 사용 언어
Kotlin

### 사용 기술
- MVVM
- Flow
- Data Binding
- View Binding
- Coroutine
- Room
- Retrofit
- Hilt
- Paging3
- Gson
- Glide
- Firebase, Firestore

### 사용 API
네이버 영화/책/장소 검색 API  
https://developers.naver.com/docs/serviceapi/search/movie/movie.md#%EC%98%81%ED%99%94  
https://developers.naver.com/docs/serviceapi/search/book/book.md#%EC%B1%85  
https://developers.naver.com/docs/serviceapi/search/local/local.md#%EC%A7%80%EC%97%AD



### 앞으로의 계획  
음악, 장소(✅) 등 카테고리를 확장하고, 지속적으로 유지보수할 계획입니다.  
텍스트 파일로 추출해내거나, 리뷰를 공유하는 기능 등 새로운 기능들을 계속해서 추가해나갈 예정입니다.
