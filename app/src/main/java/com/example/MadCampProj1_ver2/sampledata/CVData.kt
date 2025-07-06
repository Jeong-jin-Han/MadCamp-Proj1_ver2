import com.example.MadCampProj1_ver2.sampledata.CVDto

object CVData {
    fun getCVDataList(): List<CVDto> {
        return listOf(
            CVDto(
                memberId = 1,
                edu = "경기고등학교 졸업\n서울대학교 졸업\n- 기계공학과 주전공\n- 산업디자인과 복수전공\n- 4.20/4.5 GPA",
                experience = "삼성전자, 인턴\n- 제품 설계 보조\n- CAD 소프트웨어 사용",
                qualification = "석사",
                intro = "안녕하세요, 컴퓨터공학을 공부하며 AI 개발자가 되는 꿈을 키우고 있습니다."
            , studentID = "20192342"),
            CVDto(
                memberId = 2,
                edu = "서울과학기술고등학교 졸업\nKAIST 졸업\n- 전기전자공학과 주전공\n- 인공지능과 복수전공\n- 3.85/4.5 GPA",
                experience = "KAIST 연구실, 연구 보조\n- 데이터 수집 및 분석\n- 실험 도구 준비",
                qualification = "인턴",
                intro = "안녕하세요, 경영학을 전공하며 스타트업에 대한 연구를 하고 있습니다."
            , studentID = "20240123"),
            CVDto(
                memberId = 3,
                edu = "부산외국어고등학교 졸업\n연세대학교 졸업\n- 컴퓨터공학과 주전공\n- 경제학과 복수전공\n- 4.30/4.5 GPA",
                experience = "학생 프로젝트, 팀 리더\n- 웹 애플리케이션 개발\n- 팀원 관리 및 일정 조율",
                qualification = "인턴",
                intro = "안녕하세요, 디자인과 예술을 사랑하는 산업디자인과 학생입니다."
            , studentID = "20230143"),
            CVDto(
                memberId = 4,
                edu = "대전고등학교 졸업\nPOSTECH 졸업\n- 화학공학과 주전공\n- 경영학과 복수전공\n- 3.90/4.5 GPA",
                experience = "POSTECH 연구소, 실험 보조\n- 실험 자료 기록\n- 실험 장비 세팅",
                qualification = "석사",
                intro = "안녕하세요, 전기공학을 공부하며 스마트 그리드 기술에 관심이 많습니다."
            , studentID = "20200605"),
            CVDto(
                memberId = 5,
                edu = "수원고등학교 졸업\n서울대학교 졸업\n- 물리학과 주전공\n- 수학과 복수전공\n- 4.00/4.5 GPA",
                experience = "네이버, 인턴\n- 데이터 분석 보조\n- 데이터 정리 및 리포트 작성",
                qualification = "인턴",
                intro = "안녕하세요, 환경문제 해결에 기여하고 싶은 환경공학도입니다."
            , studentID = "20220231"),
            CVDto(
                memberId = 6,
                edu = "청주고등학교 졸업\n고려대학교 졸업\n- 생명과학과 주전공\n- 의학과 복수전공\n- 4.10/4.5 GPA",
                experience = "학생 프로젝트, 연구원\n- 실험 결과 분석\n- 연구 자료 정리",
                qualification = "인턴",
                intro = "안녕하세요, 심리학을 전공하며 인간 행동에 대한 깊은 이해를 추구하고 있습니다."
            , studentID = "20210348"),
            CVDto(
                memberId = 7,
                edu = "인천고등학교 졸업\n경북대학교 졸업\n- 기계공학과 주전공\n- 전기공학과 복수전공\n- 3.75/4.5 GPA",
                experience = "LG전자, 인턴\n- 고객 서비스 개선 프로젝트 참여\n- 설문조사 및 분석",
                qualification = "석사",
                intro = "안녕하세요, 국제관계를 전공하며 글로벌 이슈를 다루는 일을 하고 싶습니다."
            , studentID = "20200404"),
            CVDto(
                memberId = 8,
                edu = "대구외국어고등학교 졸업\n한양대학교 졸업\n- 토목공학과 주전공\n- 환경공학과 복수전공\n- 4.25/4.5 GPA",
                experience = "학생 프로젝트, 팀원\n- 환경 공학 관련 연구\n- 자료 분석 및 발표",
                qualification = "인턴",
                intro="안녕하세요, 음악을 사랑하며 클래식 작곡을 공부하고 있는 학생입니다."
            , studentID = "20220222"),
            CVDto(
                memberId = 9,
                edu = "서울과학고등학교 졸업\nKAIST 졸업\n- 소프트웨어공학과 주전공\n- 인공지능과 복수전공\n- 4.50/4.5 GPA",
                experience = "KAIST 연구실, 연구 보조\n- 머신러닝 데이터셋 준비\n- 코드 테스트 및 디버깅",
                qualification = "인턴",
                intro = "안녕하세요, 기계공학을 전공하며 로봇 공학에 열정을 가진 학생입니다."
            , studentID = "20220223"),
            CVDto(
                memberId = 10,
                edu = "경기외국어고등학교 졸업\n성균관대학교 졸업\n- 전자공학과 주전공\n- 산업공학과 복수전공\n- 4.00/4.5 GPA",
                experience = "학생 프로젝트, 팀 리더\n- 전자 회로 설계\n- 팀원 조율 및 실험 관리",
                qualification = "석사",
                intro = "안녕하세요, 법학을 전공하며 인권 관련 활동에 참여하고 있습니다."
            , studentID = "20180234"),
            CVDto(
                memberId = 11,
                edu = "대전외국어고등학교 졸업\n이화여자대학교 졸업\n- 영어영문학과 주전공\n- 미디어학과 복수전공\n- 3.95/4.5 GPA",
                experience = "학생 단체, 마케팅 팀\n- 소셜 미디어 관리\n- 캠페인 결과 분석\n- 생산 공정 분석 보조\n- 전자 회로 설계\n- 머신러닝 데이터셋 준비\n" +
                        "- 코드 테스트 및 디버깅",
                qualification = "박사",
                intro = "안녕하세요, 수학과 학생으로 데이터 분석에 관심이 많습니다."
            , studentID = "20140834"),
            CVDto(
                memberId = 12,
                edu = "광주과학고등학교 졸업\n경기대학교 졸업\n- 화학공학과 주전공\n- 환경학과 복수전공\n- 3.80/4.5 GPA",
                experience = "현대자동차, 인턴\n- 생산 공정 분석 보조\n- 실험 데이터 정리",
                qualification = "인턴",
                intro = "안녕하세요, 의학을 공부하며 미래의 소아과 의사를 꿈꾸고 있습니다."
            , studentID = "20240133"),
            CVDto(
                memberId = 13,
                edu = "서울국제고등학교 졸업\n중앙대학교 졸업\n- 경영학과 주전공\n- 경제학과 복수전공\n- 4.10/4.5 GPA",
                experience = "학생회, 경제학 팀\n- 캠퍼스 경제 분석\n- 데이터 수집 및 리포트 작성",
                qualification = "석사",
                intro = "안녕하세요, 문학을 사랑하며 작가의 꿈을 키우고 있는 학생입니다."
            , studentID = "20170894"),
            CVDto(
                memberId = 14,
                edu = "광주고등학교 졸업\n부산대학교 졸업\n- 환경공학과 주전공\n- 생물공학과 복수전공\n- 3.90/4.5 GPA",
                experience = "부산대학교 연구소, 연구 보조\n- 환경 데이터 분석\n- 실험 장비 유지보수",
                qualification = "박사",
                intro = "안녕하세요, 경제학을 전공하며 금융 정책에 대해 공부하고 있습니다."
            , studentID = "20153094"),
            CVDto(
                memberId = 15,
                edu = "대전과학고등학교 졸업\n서울대학교 졸업\n- 수학과 주전공\n- 물리학과 복수전공\n- 4.35/4.5 GPA",
                experience = "서울대학교 연구소, 조교\n- 수학 논문 분석\n- 연구 자료 관리",
                qualification = "인턴",
                intro = "안녕하세요, 화학공학을 공부하며 신소재 개발을 목표로 하고 있습니다."
            , studentID = "20240934"),
            CVDto(
                memberId = 16,
                edu = "인천과학고등학교 졸업\n연세대학교 졸업\n- 화학과 주전공\n- 경영학과 복수전공\n- 4.00/4.5 GPA",
                experience = "연세대학교 연구실, 실험 보조\n- 화학 실험 데이터 수집\n- 실험 기록 관리",
                qualification = "석사",
                intro = "안녕하세요, 사회학을 전공하며 현대 사회 문제를 탐구하고 있습니다."
            , studentID = "20183334"),
            CVDto(
                memberId = 17,
                edu = "대구고등학교 졸업\n경북대학교 졸업\n- 전자공학과 주전공\n- 산업공학과 복수전공\n- 4.20/4.5 GPA",
                experience = "삼성전자, 인턴\n- 제조 공정 최적화 연구 보조\n- 데이터 수집 및 보고",
                qualification = "박사",
                intro = "안녕하세요, 교육학을 공부하며 혁신적인 교육 방법을 연구하고 있습니다."
            , studentID = "20150893"),
            CVDto(
                memberId = 18,
                edu = "광명고등학교 졸업\n중앙대학교 졸업\n- 건축공학과 주전공\n- 도시공학과 복수전공\n- 3.75/4.5 GPA",
                experience = "중앙대학교 연구소, 연구 보조\n- 건축 데이터 분석\n- 프로젝트 자료 준비",
                qualification = "인턴",
                intro = "안녕하세요, 철학을 공부하며 인간의 존재에 대해 고민하는 철학도입니다."
            , studentID = "20230224"),
            CVDto(
                memberId = 19,
                edu = "청주외국어고등학교 졸업\n고려대학교 졸업\n- 경제학과 주전공\n- 통계학과 복수전공\n- 4.15/4.5 GPA",
                experience = "고려대학교 학생회, 팀원\n- 경제 데이터 분석\n- 발표 자료 준비",
                qualification = "석사",
                intro = "안녕하세요, 항공우주공학을 공부하며 우주 탐사에 열정을 가진 학생입니다."
            , studentID = "20170437"),
            CVDto(
                memberId = 20,
                edu = "서울국제고등학교 졸업\n이화여자대학교 졸업\n- 국제관계학과 주전공\n- 영어영문학과 복수전공\n- 3.85/4.5 GPA",
                experience = "학생 단체, 국제회의 준비\n- 회의 자료 번역 및 관리\n- 국제 손님 응대",
                qualification = "박사",
                intro = "안녕하세요, 컴퓨터 그래픽스와 게임 디자인에 관심이 많은 디지털미디어 전공 학생입니다."
            , studentID = "20090834"),
            CVDto(
                memberId = 21,
                edu = "부산과학고등학교 졸업\nKAIST 졸업\n- 컴퓨터공학과 주전공\n- 로봇공학과 복수전공\n- 4.50/4.5 GPA",
                experience = "KAIST 연구실, 프로그래머\n- 로봇 소프트웨어 개발\n- 코드 리뷰 및 테스트",
                qualification = "인턴",
                intro = "안녕하세요, 약학을 전공하며 신약 개발 연구에 참여하고 있습니다."
            , studentID = "20172939"),
            CVDto(
                memberId = 22,
                edu = "대구외국어고등학교 졸업\n한양대학교 졸업\n- 전기공학과 주전공\n- 기계공학과 복수전공\n- 4.10/4.5 GPA",
                experience = "한양대학교 연구소, 연구원\n- 전기 장비 설계\n- 데이터 수집 및 분석",
                qualification = "석사",
                intro = "안녕하세요, 전자공학을 공부하며 반도체 기술 개발에 힘쓰고 있습니다."
            , studentID = "20163444"),
            CVDto(
                memberId = 23,
                edu = "광주과학고등학교 졸업\nPOSTECH 졸업\n- 생명공학과 주전공\n- 환경공학과 복수전공\n- 4.30/4.5 GPA",
                experience = "POSTECH 연구소, 실험 조교\n- 생명공학 실험 준비\n- 데이터 정리",
                qualification = "박사",
                intro = "안녕하세요, 동물학을 전공하며 야생 동물 보호에 기여하고 싶습니다."
            , studentID = "20182083"),
            CVDto(
                memberId = 24,
                edu = "부산외국어고등학교 졸업\n경기대학교 졸업\n- 사회학과 주전공\n- 통계학과 복수전공\n- 4.05/4.5 GPA",
                experience = "학생회, 사회학 팀\n- 사회 데이터 분석\n- 발표 자료 준비",
                qualification = "인턴",
                intro = "안녕하세요, 정치학을 공부하며 정책 분석과 입법 과정에 관심을 두고 있습니다."
            , studentID = "20230434"),
            CVDto(
                memberId = 25,
                edu = "서울과학고등학교 졸업\n성균관대학교 졸업\n- 물리학과 주전공\n- 수학과 복수전공\n- 4.25/4.5 GPA",
                experience = "성균관대학교 연구소, 실험 보조\n- 물리학 데이터 분석\n- 실험 기록 관리",
                qualification = "석사",
                intro = "안녕하세요, 생명과학을 전공하며 암 연구에 힘쓰고 있는 학생입니다."
            , studentID = "20162882"),
            CVDto(
                memberId = 26,
                edu = "경기외국어고등학교 졸업\n고려대학교 졸업\n- 경영학과 주전공\n- 경제학과 복수전공\n- 3.90/4.5 GPA",
                experience = "고려대학교 학생회, 팀장\n- 경제 데이터 분석\n- 발표 자료 준비",
                qualification = "박사",
                intro = "안녕하세요, 미디어와 커뮤니케이션을 전공하며 방송 작가를 꿈꾸고 있습니다."
            , studentID = "20153434"),
            CVDto(
                memberId = 27,
                edu = "대전과학고등학교 졸업\n연세대학교 졸업\n- 의학과 주전공\n- 생물학과 복수전공\n- 4.40/4.5 GPA",
                experience = "연세대학교 연구소, 조교\n- 실험 자료 관리\n- 연구 데이터 분석",
                qualification = "인턴",
                intro = "안녕하세요, 도시공학을 공부하며 스마트 시티 개발에 참여하고 싶습니다."
            , studentID = "20240673"),
            CVDto(
                memberId = 28,
                edu = "광주고등학교 졸업\n부산대학교 졸업\n- 컴퓨터공학과 주전공\n- 정보보안과 복수전공\n- 4.15/4.5 GPA",
                experience = "부산대학교 연구소, 연구원\n- 네트워크 보안 연구\n- 데이터 수집",
                qualification = "석사",
                intro = "안녕하세요, 예술학을 전공하며 미술사를 깊이 탐구하고 있습니다."
            , studentID = "20152222"),
            CVDto(
                memberId = 29,
                edu = "대구과학고등학교 졸업\n서울대학교 졸업\n- 전산학과 주전공\n- 정보보호학과 복수전공\n- 4.35/4.5 GPA",
                experience = "서울대학교 연구실, 연구 보조\n- 암호 알고리즘 개발 지원\n- 데이터 분석 및 보고서 작성",
                qualification = "박사",
                intro = "안녕하세요, 데이터 과학에 관심이 많은 통계학 전공 학생입니다."
            , studentID = "20132843"),
            CVDto(
                memberId = 30,
                edu = "서울과학고등학교 졸업\nPOSTECH 졸업\n- 신소재공학과 주전공\n- 물리학과 복수전공\n- 4.20/4.5 GPA",
                experience = "POSTECH 연구소, 연구 보조\n- 신소재 개발 실험\n- 실험 데이터 분석 및 정리",
                qualification = "석사",
                intro = "안녕하세요, 한의학을 공부하며 건강한 삶을 연구하는 학생입니다."
            , studentID = "20132342")
        )
    }
}
