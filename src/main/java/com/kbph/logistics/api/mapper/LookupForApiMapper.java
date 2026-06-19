package com.kbph.logistics.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.api.domain.CommonInfoDTO;
import com.kbph.logistics.om.domain.OoubhdDTO;
import com.kbph.logistics.om.domain.OoubitDTO;
import com.kbph.logistics.om.domain.WshphdDTO;
import com.kbph.logistics.om.domain.WshpitDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;
import com.kbph.logistics.tm.domain.TmDispatchDTO;

@Mapper
public interface LookupForApiMapper {
	// 차량정보
	public TmDispatchDTO selectVehicleInformationForApi(@Param("param") TmDispatchDTO dispatch, @Param("common") CommonInfoDTO commonInfo);
	// 차량-기사 매핑
	public List<TmDispatchDTO> selectVehicleDriverMappingForApi(@Param("param") TmDispatchDTO dispatch, @Param("common") CommonInfoDTO commonInfo);
	// 출고오더 헤더 조회
	public OoubhdDTO selectOutboundOrderHeaderListForApi(@Param("param") OoubhdDTO ooubhdDTO, @Param("common") CommonInfoDTO commonInfo);
	// 출고오더 아이템 조회
	public List<OoubitDTO> selectOutboundOrderItemListForApi(@Param("param") OoubhdDTO ooubhdDTO, @Param("common") CommonInfoDTO commonInfo);
	// 재고 여부 확인
	public <T> WstkkyDTO hasStockForApi(@Param("param") T paramDTO, @Param("common") CommonInfoDTO commonInfo);
	// 상태 변경을 위한 중량 테이블 검색
	public WshphdDTO checkShipmentCompletionStatusForApi(@Param("param") WshphdDTO wshphdDTO, @Param("common") CommonInfoDTO commonInfo);
	// 출고문서 번호 채번
	public String selectShpdckyForApi(@Param("common") CommonInfoDTO commonInfo);
	// 출고문서 아이템 번호 채번
	public Integer selectShpdcitForApi(@Param("shpdcky") String shpdcky, @Param("common") CommonInfoDTO commonInfo);
	// 타겟 출고문서  조회
	public List<WshpitDTO> selectCmpTargetWshpit(@Param("param") WtakitDTO wtakitDTO, @Param("common") CommonInfoDTO commonInfo);
	// 출고문서의 배차계획번호와 출고계획번호 조회
	public List<OoubitDTO> selectShpplkyAndVhplnkyForApi(@Param("param") WtakitDTO wtakitDTO, @Param("common") CommonInfoDTO commonInfo);
	// 작업문서번호 채번
	public String selectTaskokyForApi(@Param("common") CommonInfoDTO commonInfo);
	// 작업문서아이템번호 채번
	public Integer selectTaskoitForApi(@Param("taskoky") String taskoky, @Param("common") CommonInfoDTO commonInfo);
	// 배차아이템리스트 상태 개수 조회
	public Integer selectTplnitCountValidationListForApi(@Param("param") WshphdDTO wshphdDTO, @Param("common") CommonInfoDTO commonInfo);
	// 작업리스트 조회
	public List<WtakitDTO> selectWtakitListForApiValidation(@Param("param") WtakitDTO wtakitDTO, @Param("common") CommonInfoDTO commonInfo);
}
