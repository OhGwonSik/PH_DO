package com.kbph.logistics.sy.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO extends CommonColumnDTO {
	private String userkey; // 유저key
	private String useract; // 계정ID
	private String passwrd; // 비밀번호
	private String usernam; // 사용자이름
	private String address; // *Address
	private String citycod; // *City
	private String rsonkey; // *Region
	private String postcod; // *Postal code
	private String natn2ky; // *국기코드
	private String telphnm; // 휴대전화번호
	private String emailad; // 이메일
	private String departm; // 부서명
	private String emploid; // *회사 내부 사번
	private String acapdoc; // *결재문서번호

	private String ownerky; // Owner key
	private String ptnrkey; // Partner Key
	private String custkey; // Customer Key

	private String usergr1; // User group1
	private String usergr2; // User group2
	private String usergr3; // User group3
	private String langkey; // *Language key
	private String datefmt; // *Date format
	private String timefmt; // *Time format
	private String decifmt; // *Decimal format
	private int utimzon; // *Time zone by user

	private String idexpdt; // 계정 만료일
	private String pwexpdt; // 비밀번호 만료일자
	private int pwercnt; // 비번 오류횟수
	private String alocked; // 계정잠금여부
	private String alockmo; // 계정 Locked 사유

	private String polpryn; // 개인정보동의
	private String polsvyn; // 이용약관동의여부
	private String polsmyn; // 문자수신동의
	private String polemyn; // 이메일수신동의
	private String poldate; // 서비스동의날자
	private String poltime; // 서비스동의시간

	private String approyn; // VARCHAR(1) 사용자승인여부
	private String sigupyn; // VARCHAR(1) 회원가입 여부
	private String apuseyn; // VARCHAR(1) 앱사용자 여부
	private String activyn; // VARCHAR(1) 사용여부
	private String usrmemo; // VARCHAR(100) 사용자 메모

	private String sotapyn; // 탈퇴요청(Y=요청)
	private String sotdate; // 탈퇴요청일자
	private String sottime; // 탈퇴요청시간
	private String ownapyn; // 판매계약사사용자 승인여부
	private String ownapdt; // 판매계약사사용자 승인일자
	private String ownaptm; // 판매계약사사용자 승인시간
	private String ownapid; // 판매계약사사용자 승인자

	// 사용자 관리 data
	private String apnamen; // 어플리케이션 명 영어
	private String vehicky; // 차량코드
	private String vownkey; // 차주코드
	private String usertyp; // 사용자 타입
	private String rolgkey; // 권한그룹 키
	private String oldUserKey; // 이전 유저키
	private Integer updtchk2; // SUSRSH 또는 MVOWMA UPDTCHK
	private String standardUseract;
}
