package com.kbph.logistics.om.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.om.domain.OmReportDTO;
import com.kbph.logistics.sy.domain.UserVO;
/**
 * @author : s.h.kim
 * @version : 1.0.0
 * @since : 2024-09-19
 * @note : OmReportMapper
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-09-19					s.h.kim        						create mapper interface
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Mapper
public interface OmReportMapper {
	// 출고지시서 (테스트용)
	List<OmReportDTO> selectShippingOrder(@Param("param") OmReportDTO param, @Param("userData") UserVO userData);
	OmReportDTO selectShippingOrderParam(@Param("param") OmReportDTO param, @Param("userData") UserVO userData);

	// 출고송장
	OmReportDTO selectShippingInvoiceHead(@Param("param") OmReportDTO param, @Param("userData") UserVO userData);
	List<OmReportDTO> selectShippingInvoiceHeadList(@Param("param") OmReportDTO param, @Param("userData") UserVO userData);
	List<OmReportDTO> selectShippingInvoiceItem(@Param("param") OmReportDTO param, @Param("userData") UserVO userData);

	// 인쇄 로그
	int selectOmPrintCnt(@Param("param") OmReportDTO param, @Param("userData") UserVO userData);
	int insertOmPrintLog(@Param("saveData") OmReportDTO param, @Param("userData") UserVO userData);
}