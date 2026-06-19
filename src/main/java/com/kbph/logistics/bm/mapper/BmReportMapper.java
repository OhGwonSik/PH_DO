package com.kbph.logistics.bm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.bm.domain.BmReportDTO;
import com.kbph.logistics.sy.domain.UserVO;
/**
 * @author : s.h.kim
 * @version : 1.0.0
 * @since : 2024-09-19
 * @note : BmReportMapper
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-09-19					s.h.kim        						create mapper interface
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Mapper
public interface BmReportMapper {
	 // 지급내역서
	List<BmReportDTO> selectTransferPayment(@Param("param") BmReportDTO param, @Param("userInfo") UserVO userData); // 이송
	List<BmReportDTO> selectTransportPayment(@Param("param") BmReportDTO param, @Param("userInfo") UserVO userData); // 운송

	 // 청구내역서
	List<BmReportDTO> selectTransferCharge(@Param("param") BmReportDTO param, @Param("userInfo") UserVO userData); // 이송
	List<BmReportDTO> selectTransferSurCharge(@Param("param") BmReportDTO param, @Param("userInfo") UserVO userData); // 이송추가청구

	List<BmReportDTO> selectTransportCharge(@Param("param") BmReportDTO param, @Param("userInfo") UserVO userData); // 운송
	List<BmReportDTO> selectTransportSurCharge(@Param("param") BmReportDTO param, @Param("userInfo") UserVO userData); // 운송 추가청구

	// 프린트 로그
	int selectPaymentPrintCnt(@Param("param") BmReportDTO param, @Param("userData") UserVO userData);
	int selectChargePrintCnt(@Param("param") BmReportDTO param, @Param("userData") UserVO userData);
	int insertPaymentPrintLog(@Param("saveData") BmReportDTO param, @Param("userData") UserVO userData);
	int insertChargePrintLog(@Param("saveData") BmReportDTO param, @Param("userData") UserVO userData);
}