package com.kbph.logistics.configuration.security;

import java.util.Calendar;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.kbph.logistics.common.constant.Constants;
import com.kbph.logistics.sy.domain.UserVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-05
 * @note : jwt provider
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-08-05					t.s.park							create jwt provider
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Slf4j
@Component
public class JwtProvider {
	private final String acSecret; // access token
//	private final String rfSecret; // refresh token
	private final int acExpireTime; // access token ExpireTime
//	private final int rfExpireTime; // refresh token ExpireTime
	private final CustomUserDetailService customUserDetailService;

	public JwtProvider(@Value("${jwt.secret.accessToken}") String acSecret,
//								@Value("${jwt.secret.refreshToken}") String rfSecret,
								@Value("${jwt.secret.accessToken-expire-time}") int acExpireTime,
//								@Value("${jwt.secret.refreshToken-expire-time}") int rfExpireTime,
								@Autowired CustomUserDetailService customUserDetailService) {

		this.acSecret = acSecret;
//		this.rfSecret = rfSecret;
		this.acExpireTime = acExpireTime;
//		this.rfExpireTime = rfExpireTime;
		this.customUserDetailService = customUserDetailService;
	}

	public int getAcExpireTime() {
		return this.acExpireTime;
	}

//	public int getRfExpireTime() {
//		return this.rfExpireTime;
//	}

	public String generateAccessToken(Authentication auth) {
		UserVO userInfo = customUserDetailService.loadUserByUsername(auth.getName()).getUserInfo();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, acExpireTime);

		SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(acSecret));
		JwtBuilder jb = Jwts.builder()
									.claim("userkey", userInfo.getUserkey())
									.claim("useract", userInfo.getUseract())
									.claim("usernam", userInfo.getUsernam())
									.claim("langkey", userInfo.getLangkey())
									.claim("usertyp", userInfo.getUsertyp())
									.claim("commonSchema", userInfo.getCommonSchema())
									.claim("schema", userInfo.getSchema())
									.claim("schemaList", userInfo.getSchemaList())
									.claim("authorities", auth.getAuthorities())
									.claim("fcmtoken", userInfo.getFcmtokn())
									.expiration(cal.getTime())
									.signWith(secretKey);

		log.info("Token generated : {} / Time : {}", auth.getName(), Calendar.getInstance().getTime());

		return jb.compact();
	}

	public Claims getAccessTokenClaims(String token) {
		SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(acSecret));

		return Jwts.parser()
						.verifyWith(secretKey)
						.build()
						.parseSignedClaims(token)
						.getPayload();
	}

	public String getAccessTokenUseract(String token) {
		return getAccessTokenClaims(token).get(Constants.Auth.USER_NAME).toString();
	}

	public boolean validateAccessToken(String token) {
		try {
			getAccessTokenClaims(token);

			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			e.getStackTrace();
            throw new MalformedJwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
			e.getStackTrace();
        	throw new ExpiredJwtException(null, null, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
			e.getStackTrace();
        	throw new UnsupportedJwtException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
			e.getStackTrace();
        	throw new IllegalArgumentException("JWT 토큰이 잘못되었습니다.");
        }
	}

	/** refresh token 나중에 사용하면 */
//	public String generateRefreshToken(Authentication auth) {
//		UserVO userInfo = customUserDetailService.loadUserByUsername(auth.getName()).getUserInfo();
//
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MILLISECOND, rfExpireTime * 1000);
//
//		SecretKey secretKey = Keys.hmacShaKeyFor(rfSecret.getBytes());
//		JwtBuilder jb = Jwts.builder()
//									.claim("id", "id")
//									.claim("useract", userInfo.getUseract())
//									.claim("refreshJwt", secretKey)
//									.claim("expired", "N")
//									.claim("revoked", "N")
//									.expiration(cal.getTime())
//									.signWith(secretKey);
//
//		return jb.compact();
//	}
//
//	public Claims getRefreshTokenClaims(String token) {
//		SecretKey secretKey = Keys.hmacShaKeyFor(rfSecret.getBytes());
//
//		return Jwts.parser()
//						.verifyWith(secretKey)
//						.build()
//						.parseSignedClaims(token)
//						.getPayload();
//	}
//
//	public String getRefreshTokenClaim(String token, String claimName) {
//		return getAccessTokenClaims(token).get(claimName).toString();
//	}
//
//	public boolean validateRefreshToken(String token) {
//		try {
//			getRefreshTokenClaims(token);
//
//			return true;
//		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            throw new MalformedJwtException("잘못된 JWT 서명입니다.");
//        } catch (ExpiredJwtException e) {
//        	throw new ExpiredJwtException(null, null, "만료된 JWT 토큰입니다.");
//        } catch (UnsupportedJwtException e) {
//        	throw new UnsupportedJwtException("지원되지 않는 JWT 토큰입니다.");
//        } catch (IllegalArgumentException e) {
//        	throw new IllegalArgumentException("JWT 토큰이 잘못되었습니다.");
//        }
//	}
}
