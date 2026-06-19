package com.kbph.logistics.configuration.security;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kbph.logistics.common.enums.SubDomain;
import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.common.util.RequestUtil;
import com.kbph.logistics.smp.holder.SchemaAndFlagHolder;
import com.kbph.logistics.sy.domain.SchemaDTO;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.sy.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 유저 데이터 인증시 방식
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
	private final UserService userService;
	private final SchemaAndFlagHolder schemaAndFlagHolder;
	@Value("${spring.datasource.hikari.operation.common-schema}")
	private String commonSchema;

	@Override
	public CustomUserDetails loadUserByUsername(String useract) {
		log.info("** userDetails 회원 정보 조회 : {} **", useract);
		UserVO reqUser = new UserVO();
		reqUser.setUseract(useract);
		reqUser.setCommonSchema(commonSchema);

		String userApplication = userService.getUserApplication(reqUser);
		if(userApplication == null) {
			throw new UsernameNotFoundException("User not found"); // to-do 메세지로 변경
		}

		UserVO userInfo = null;
		String smpSchema = schemaAndFlagHolder.getSchemaHolder();
		reqUser.setIsAppUser(schemaAndFlagHolder.getIsAppUser(useract));
		List<SchemaDTO> schemaList = userService.getSchemaList(userApplication);
		if(smpSchema == null) {
			String subDomainToSchema = RequestUtil.extractSubDomain();
			if(subDomainToSchema != null) { // 운영서버로 구동 시
				reqUser.setSchema(subDomainToSchema.toUpperCase().replace("-", "_"));
				userInfo = userService.selectUserCompanyAccount(reqUser);
				if(userInfo == null) {
					throw new UsernameNotFoundException("계정을 찾을 수 없습니다.");
				}
				userInfo.setUschema(reqUser.getSchema()); 	//user schema
				userInfo.setUsrlgdm(subDomainToSchema);		//userInfo subDomainSchema set
			}else {	// localhost 환경, 개발서버
				if(!(Useyn.USE.getString().equals(reqUser.getIsAppUser()))) {
					for(SchemaDTO schema : schemaList) {
						reqUser.setSchema(schema.getSchemaName());
						userInfo = userService.selectUserCompanyAccount(reqUser);
						if(userInfo != null) {
							userInfo.setUschema(reqUser.getSchema()); 	//user schema
							break;
						}
					}
				}else {
					userInfo = userService.selectUserCompanyAccount(reqUser);
				}
				userInfo.setUsrlgdm(SubDomain.LOCAL.getString());
			}
		} else { // SMP 연계
			reqUser.setSchema(smpSchema);
			userInfo = userService.selectUserCompanyAccount(reqUser);
		}

        if (userInfo == null) {
        	schemaAndFlagHolder.removeUserInfo(useract);
            throw new UsernameNotFoundException("User not active"); // to-do 메세지로 변경
        }
        LocaleContextHolder.setLocale(new Locale(userInfo.getLangkey()));
        Locale.setDefault(new Locale(userInfo.getLangkey()));

        userInfo.setSchema(reqUser.getSchema());
        userInfo.setCommonSchema(commonSchema);
        userInfo.setSchemaList(schemaList);
        userInfo.setApnamen(userApplication);
        userInfo.setIsAppUser(reqUser.getIsAppUser());
        userInfo.setUsrlgip(RequestUtil.getRemoteIp());		//remoteIp

		return new CustomUserDetails(userInfo);
	}
}