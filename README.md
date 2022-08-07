# Collector - review collector

종종 생각의 저편에서 영화나 책의 줄거리가 떠오르곤 하지만 정작 제목을 몰라 다시 꺼내보지도 못한 채 기억 속에 묻어두곤 합니다. Collector은 이러한 아쉬움을 극복하기 위해 고안되었습니다. 영화, 책 등 다양한 카테고리를 리뷰하고 카드 형식으로 수집하는 앱입니다.

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
