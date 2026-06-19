package com.kbph.logistics.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.api.domain.ApiCommonDTO;
import com.kbph.logistics.api.domain.ConversionWasnifDTO;
import com.kbph.logistics.api.domain.ExcelWasnifInfo;
import com.kbph.logistics.api.domain.InbEntranceCarDTO;
import com.kbph.logistics.api.domain.InbEntranceDongDTO;
import com.kbph.logistics.api.domain.InboundOperationDTO;
import com.kbph.logistics.api.domain.InboundTaskDTO;
import com.kbph.logistics.api.domain.InspectionDTO;
import com.kbph.logistics.im.domain.WasnitDTO;
import com.kbph.logistics.im.domain.WasnitSeacrhParamDTO;
import com.kbph.logistics.im.domain.WrcvhdDTO;
import com.kbph.logistics.im.domain.WrcvitDTO;
import com.kbph.logistics.im.domain.WrcvitSeacrhParamDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sy.domain.UserVO;

@Mapper
public interface ApiInboundMapper {
	List<ConversionWasnifDTO> selectConversionInfo(@Param("apiCommon") ApiCommonDTO<ExcelWasnifInfo> apiCommon);
	int insertConversionAdjustment(@Param("param") ConversionWasnifDTO conversionWasnif, @Param("schema") String schema);
	int updateConversionInfo(@Param("param") ConversionWasnifDTO conversionWasnif, @Param("schema") String schema);
	int updateTempTableFlags(@Param("param") ConversionWasnifDTO conversionWasnif, @Param("schema") String schema);

	int checkEntranceCar(@Param("param") InbEntranceCarDTO inbEntranceCarDTO, @Param("apiCommon") ApiCommonDTO<InbEntranceCarDTO> apiCommon);
	int updateEntranceCar(@Param("apiCommon") ApiCommonDTO<InbEntranceCarDTO> apiCommon); // 입차정보

	int updateEntranceDong(@Param("param") InbEntranceDongDTO inbEntranceDongDTO, @Param("apiCommon") ApiCommonDTO<InbEntranceDongDTO> apiCommon);

	int checkItemInspection(@Param("param") InspectionDTO inspectionDTO, @Param("apiCommon") ApiCommonDTO<InspectionDTO> apiCommon);
	int updateItemInspection(@Param("apiCommon") ApiCommonDTO<InspectionDTO> apiCommon); // 검수완료실적
	int updateHeadInspection(@Param("apiCommon") ApiCommonDTO<InspectionDTO> apiCommon); // 검수완료실적
	int updateOstrhdStat(@Param("param") WasnitDTO wasnitDTO, @Param("apiCommon") ApiCommonDTO<InspectionDTO> apiCommon);
	int deleteInspectionFailMskuwc(@Param("param") WasnitDTO wasnitDTO, @Param("apiCommon") ApiCommonDTO<InspectionDTO> apiCommon);
	int updateOstritStat(@Param("param") WasnitDTO wasnitDTO, @Param("apiCommon") ApiCommonDTO<InspectionDTO> apiCommon);
	int updateWasnhdStat(@Param("data") WasnitDTO wasnitDTO, @Param("apiCommon") ApiCommonDTO<InspectionDTO> apiCommon);
	<T> List<WasnitDTO> selectWasnitStateList(@Param("data") WasnitSeacrhParamDTO seacrhParamDTO, @Param("apiCommon") ApiCommonDTO<T> apiCommon);

	<T1, T2> int checkInboundStart(@Param("param")T1 requestDTO ,@Param("apiCommon") ApiCommonDTO<T2> apiCommon);
	int updateInboundStart(@Param("apiCommon") ApiCommonDTO<InboundOperationDTO> apiCommon); // 입고조업시작
	List<WrcvitDTO> selectApiTaskRcvkeyList(@Param("data") InboundOperationDTO apiCommon);
	int updateApiWrcvhdStatus(@Param("param") WrcvhdDTO param, @Param("status") String status, @Param("userData") UserVO userVO);
	int updateApiWrcvitStatus(@Param("updateList") List<WrcvitDTO> updateList, @Param("status") String status, @Param("withoutCancel") boolean withoutCancel, @Param("userData") UserVO userVO);
	List<WasnitDTO> selectApiWasnitCancelList(@Param("data") InspectionDTO data, @Param("apiCommon") ApiCommonDTO<InspectionDTO> apiCommon);
	List<WasnitDTO> selectApiWasnitList(@Param("data") InspectionDTO data, @Param("apiCommon") ApiCommonDTO<InspectionDTO> apiCommon);
	WasnitDTO selectApiWasnitAndTaskData(@Param("data") InboundTaskDTO apiCommon);
	String getApiStockky(@Param("apiCommon") ApiCommonDTO<InboundTaskDTO> apiCommon);
	String getApiLotnmky(@Param("apiCommon") ApiCommonDTO<InboundTaskDTO> apiCommon);
	int insertApiWskky(@Param("item") WasnitDTO wasnitDTO);
	int updateInboundTask(@Param("apiCommon") ApiCommonDTO<InboundTaskDTO> apiCommon); // 입고작업완료
	<T> int updateInboundApiWstkky(@Param("apiCommon") ApiCommonDTO<T> apiCommon);
	List<WrcvhdDTO> selectApiWrcvitList(@Param("data") WrcvitSeacrhParamDTO wrcvitSeacrhParamDTO, @Param("apiCommon") ApiCommonDTO<InboundOperationDTO> apiCommon);
	<T1, T2> WstkkyDTO isApiSameLocationAndLayer(@Param("data") T1 paramDTO, @Param("apiCommon") T2 apiCommon);
	int updateApiStockLocAndLayer(@Param("data")InboundOperationDTO inboundOperationDTO, @Param("apiCommon") ApiCommonDTO<InboundOperationDTO> apiCommon);
	int isApiAsnTaskAllOperCmp(@Param("data")InboundOperationDTO inboundOperationDTO, @Param("apiCommon") ApiCommonDTO<InboundOperationDTO> apiCommon);
	int updateInboundComplete(@Param("apiCommon") ApiCommonDTO<InboundOperationDTO> apiCommon); // 입고조업완료
}
