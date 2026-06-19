package com.kbph.logistics.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.api.domain.ApiCommonDTO;
import com.kbph.logistics.api.domain.CommonInfoDTO;
import com.kbph.logistics.api.domain.OubEntranceCarDTO;
import com.kbph.logistics.api.domain.OubEntranceDongDTO;
import com.kbph.logistics.api.domain.OutboundOperationDTO;
import com.kbph.logistics.api.domain.OutboundTaskDTO;
import com.kbph.logistics.om.domain.OoubhdDTO;
import com.kbph.logistics.om.domain.OoubitDTO;
import com.kbph.logistics.om.domain.WshphdDTO;
import com.kbph.logistics.om.domain.WshpitDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;

@Mapper
public interface ApiOutboundMapper {
	//---- API ----//
	public int updateEntranceCar(@Param("apiCommon") ApiCommonDTO<OubEntranceCarDTO> apiCommon); // 입차정보
	public int updateEntranceDong(@Param("apiCommon") ApiCommonDTO<OubEntranceDongDTO> apiCommon); // 입동지시정보
	public List<OutboundTaskDTO> getApiOutStockInfo (@Param("param") OutboundTaskDTO outboundTaskDTO, @Param("apiCommon") ApiCommonDTO<OutboundTaskDTO> apiCommon); // 출고조업갱신- 실시간 재고 정보 조회
	public int updateAPiOutTask(@Param("param") OutboundTaskDTO outboundTaskDTO, @Param("apiCommon") ApiCommonDTO<OutboundTaskDTO> apiCommon); // 출고조업갱신 - 작업테이블 update
	public int updateAPiOutboundOrder(@Param("param") OutboundTaskDTO outboundTaskDTO, @Param("apiCommon") ApiCommonDTO<OutboundTaskDTO> apiCommon); // 출고조업갱신 - 출고오더테이블 update
	public int updateAPiOutboundShip(@Param("param") OutboundTaskDTO outboundTaskDTO, @Param("apiCommon") ApiCommonDTO<OutboundTaskDTO> apiCommon); // 출고조업갱신 - 출고문서테이블 update
	public List<OutboundTaskDTO> getApiOutTaskInfo(@Param("param") OutboundTaskDTO outboundTaskDTO, @Param("apiCommon") ApiCommonDTO<OutboundTaskDTO> apiCommon); // 출고조업갱신- 변경된 작업정보
	public int updateOutboundStart(@Param("apiCommon") ApiCommonDTO<OutboundOperationDTO> apiCommon); // 출고조업시작
	public int updateOutboundTask(@Param("apiCommon") ApiCommonDTO<OutboundTaskDTO> apiCommon); // 출고작업완료
	public int updateOutboundComplete(@Param("apiCommon") ApiCommonDTO<OutboundOperationDTO> apiCommon); // 출고조업완료

	//----조업, 작업 벨리데이션 및 쿼리 ----//
	public <T1, T2> int checkOutboundStart(@Param("param")T1 requestDTO ,@Param("apiCommon") ApiCommonDTO<T2> apiCommon);
	public List<OoubitDTO> getApiShpdckyAndIt(@Param("param") OutboundOperationDTO requestDTO, @Param("apiCommon") ApiCommonDTO<OutboundOperationDTO> apiCommon);
	public int updateApiWshpitStat(@Param("param") OoubitDTO requestDTO, @Param("apiCommon") ApiCommonDTO<OutboundOperationDTO> apiCommon);
	public int getApiWshpitStatCount(@Param("param") OoubitDTO requestDTO, @Param("apiCommon") ApiCommonDTO<OutboundOperationDTO> apiCommon);
	public int updateApiWshphdStat(@Param("param") OoubitDTO requestDTO, @Param("apiCommon") ApiCommonDTO<OutboundOperationDTO> apiCommon);
	public List<OoubitDTO> selectOoubitListForApiValidation(@Param("param") OoubhdDTO ooubhdDTO, @Param("apiCommon") ApiCommonDTO<OutboundOperationDTO> apiCommon);

	//---- API 이후 자동화용 쿼리(insert & update & delete) ----//
	public int updateObostatForApi(@Param("param") OoubhdDTO ooubhdDTO, @Param("common") CommonInfoDTO commonInfo); // 출고오더헤더 상태 업데이트
	public int updateOboitstForApi(@Param("param") OoubitDTO ooubitDTO, @Param("common") CommonInfoDTO commonInfo); // 출고오더아이템 상태 업데이트
	public int updateObostatCMPForApi(@Param("param") OoubhdDTO ooubhdDTO, @Param("common") CommonInfoDTO commonInfo);// 출고오더헤더 상태(완료) 업데이트
	public int updateOboitstCMPForApi(@Param("param") WtakitDTO wtakitDTO, @Param("common") CommonInfoDTO commonInfo); // 출고오더아이템 상태(완료) 업데이트
	public int updateTpwpwgShpcfynForApi(@Param("param") WshphdDTO wshphdDTO, @Param("common") CommonInfoDTO commonInfo); // 중량테이블 플래그 수정
	public int insertWshphdForApi(@Param("param") OoubhdDTO ooubhdDTO, @Param("common") CommonInfoDTO commonInfo); // 출고문서 헤더 생성
	public int insertWshpitForApi(@Param("param") OoubitDTO ooubitDTO, @Param("common") CommonInfoDTO commonInfo); // 출고문서 아이템 생성
	public int updateWplnhdStatForApi(@Param("param") WshphdDTO wshphdDTO, @Param("common") CommonInfoDTO commonInfo); // 출고계획 헤더 상태 업데이트
	public int updateWplnitStatForApi(@Param("param") WshphdDTO wshphdDTO, @Param("common") CommonInfoDTO commonInfo); // 출고계획 아이템 상태 업데이트
	public int updateShpdcstCMPForApi(@Param("param") WshpitDTO wshpitDTO, @Param("common") CommonInfoDTO commonInfo); //  출고문서 헤더 상태(완료) 업데이트
	public int updateShpitstCMPForApi(@Param("param") WtakitDTO wtakitDTO, @Param("common") CommonInfoDTO commonInfo); //  출고문서아이템 상태(완료) 업데이트
	public int updateWshpitShpDateForApi(@Param("param") WtakitDTO wtakitDTO, @Param("common") CommonInfoDTO commonInfo); // 출고문서 출고시간 변경
	public int updateTplnhdStatForApi(@Param("param") WshphdDTO wshphdDTO, @Param("status")String status, @Param("common") CommonInfoDTO commonInfo); // 배차계획헤더 업데이트
	public int updateTplnitStatForApi(@Param("data") WshpitDTO wshpitDTO, @Param("status")String status, @Param("common") CommonInfoDTO commonInfo); // 배차계획아이템 업데이트
	public <T> int insertWtakitForApi(@Param("param") T paramDTO, @Param("common") CommonInfoDTO commonInfo); // 작업문서 생성
	public int updateWtakitTaskstsForApi(@Param("param") WtakitDTO wtakitDTO, @Param("common") CommonInfoDTO commonInfo); // 작업문서 상태 변경
	public int updateWtakitStatOperCmpForApi(@Param("param") WtakitDTO wtakitDTO, @Param("common") CommonInfoDTO commonInfo); // 작업문서 OPERCMP 상태 변경
	public List<WstkkyDTO> getApiLocationStockInfo(@Param("param") WstkkyDTO wstkkyDTO, @Param("common") CommonInfoDTO commonInfo); // from베드 재고 조회
	public int updateApiStockLayerSort(@Param("param") WstkkyDTO wstkkyDTO, @Param("common") CommonInfoDTO commonInfo); // 단수 조정
	public int updateWtakitStatusToShpdckyAndShpdcitForApi(@Param("param") WtakitDTO wtakitDTO, @Param("status") String status, @Param("common") CommonInfoDTO commonInfo); // 작업리스트 상태 업데이트(출고문서 기준)
	public int deleteStockForApi(@Param("param") WtakitDTO wtakitDTO, @Param("common") CommonInfoDTO commonInfo); // 재고삭제
	public int updateOoubhdEntdoyn(@Param("param") OoubhdDTO ooubhd, @Param("common") CommonInfoDTO commonInfo); // 입동지시여부 갱신
}