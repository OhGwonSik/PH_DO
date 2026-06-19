package com.kbph.logistics.sy.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserVO implements Serializable {
	private static final long serialVersionUID = 5598343544119175224L;

	// PK
	private String userkey; // VARCHAR(10) user unique sequence;
	private String useract; // VARCHAR(60) User Account ID

	// Column
	private String passwrd; // VARCHAR(100) Password
	private String usernam; // VARCHAR(60) User name
	private String address; // VARCHAR(100) Address
	private String citycod; // VARCHAR(20) City
	private String rsonkey; // VARCHAR(10) Region
	private String postcod; // VARCHAR(10) Postal code
	private String natn2ky; // VARCHAR(3) Country key
	private String telphnm; // VARCHAR(20) Telephone number
	private String emailad; // VARCHAR(50) Electric mail address
	private String departm; // VARCHAR(50) Department
	private String emploid; // VARCHAR(50) Employee identifier in a company
	private String acapdoc; // VARCHAR(50) Account approval document
	private String usertyp; // VARCHAR(10) User Type
	private String usergr1; // VARCHAR(20) User group1
	private String usergr2; // VARCHAR(20) User group2
	private String usergr3; // VARCHAR(20) User group3
	private String langkey; // VARCHAR(2) Language key
	private String datefmt; // VARCHAR(20) Date format
	private String timefmt; // VARCHAR(20) Time format
	private String decifmt; // VARCHAR(20) Decimal format
	private String utimzon; // NUMERIC(10,3) Time zone by user
	private String idexpdt; // VARCHAR(8) 계정 만료일
	private String pwexpdt; // VARCHAR(8) 비밀번호 만료일자
	private Integer pwercnt; // number(10,3) 비밀번호 오류횟수
	private String alocked; // VARCHAR(1) Act Locked 계정잠금여부
	private String alockmo; // VARCHAR(100) Act Locked memo
	private String rolgkey; // 권한그룹키
	private String polpryn; // 개인정보동의
	private String polsvyn; // 이용약관동의여부
	private String polsmyn; // 문자수신동의
	private String polemyn; // 이메일수신동의
	private String poldate; // 서비스동의날자
	private String poltime; // 서비스동의시간
	private String approyn; // VARCHAR(1) 사용자승인여부
	private String sigupyn; // VARCHAR(1) 회원가입 여부
	private String apuseyn; // VARCHAR(1) 앱사용자 여부
	private String activtn; // VARCHAR(1) 사용여부
	private String usrmemo; // VARCHAR(100) 사용자 메모
	private String sotapyn; // VARCHAR(1) 탈퇴요청여부
	private String sotdate; // VARCHAR(8) 탈퇴요청날짜
	private String sottime; // VARCHAR(6) 탈퇴요청시간
	private String fcmtokn; // fcm 토큰

	private String credate; // VARCHAR(8) Creation date
	private String cretime; // VARCHAR(6) Creation time
	private String creuser; // VARCHAR(20) Created by
	private String lmodate; // VARCHAR(8) Last modified date
	private String lmotime; // VARCHAR(6) Last modified time
	private String lmouser; // VARCHAR(20) Last modified by
	private String updtchk; // INT(11) update check

	// data
	private String schema;
	private String commonSchema;
	private String progrid;
	private String apnamen;
	private transient List<SchemaDTO> schemaList;
	private String isAppUser;
	private String usrlgip;		// ip주소
	private String usrlgdm; 	// 서브 도메인	
	private String uschema;		// 유저 스키마 
}
