package com.kbph.logistics.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.api.domain.ApiCommonDTO;
import com.kbph.logistics.api.domain.SelectionOperationCancelDTO;
import com.kbph.logistics.api.domain.SelectionOperationComplDTO;
import com.kbph.logistics.api.domain.SelectionOperationDTO;
import com.kbph.logistics.api.domain.SelectionTaskDTO;
import com.kbph.logistics.api.domain.WstkkyUploadDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;

@Mapper
public interface ApiInventoryMapper {
	void spUpdWstkkyExcel(@Param("data") WstkkyUploadDTO wstkkyUploadDTO, @Param("apiCommon")ApiCommonDTO<WstkkyUploadDTO> apiCommon);
	List<WstkkyDTO> getApiExcelUploadUpdateList(@Param("apiCommon")ApiCommonDTO<WstkkyUploadDTO> apiCommon);
	int updateStockFlag(@Param("apiCommon")ApiCommonDTO<WstkkyUploadDTO> apiCommon);
	int checkSelectionCancelVail(@Param("data") SelectionOperationCancelDTO selectionOperationCancelDTO ,@Param("apiCommon")ApiCommonDTO<SelectionOperationCancelDTO> apiCommon);
	int updateApiSelectionCancelForWstkky(@Param("data") SelectionOperationCancelDTO selectionOperationCancelDTO ,@Param("apiCommon")ApiCommonDTO<SelectionOperationCancelDTO> apiCommon);
	int updateSelectionStateCancel(@Param("data") SelectionOperationCancelDTO selectionOperationCancelDTO ,@Param("apiCommon")ApiCommonDTO<SelectionOperationCancelDTO> apiCommon);
	<T1, T2> int checkSelectionStart(@Param("data")T1 selectionOperationDTO, @Param("apiCommon") ApiCommonDTO<T2> apiCommon);
	List<SelectionOperationDTO> getApiSelectionBeforeLocaList(@Param("data")SelectionOperationDTO selectionOperationDTO, @Param("apiCommon") ApiCommonDTO<SelectionOperationDTO> apiCommon);
	int updateApiSelectionBeforeLoca(@Param("data")SelectionOperationDTO selectionOperationDTO, @Param("apiCommon") ApiCommonDTO<SelectionOperationDTO> apiCommon);
	int updateSelectionStart(@Param("apiCommon") ApiCommonDTO<SelectionOperationDTO> apiCommon); // 조업 시작
	<T1,T2> WstkkyDTO getFromLocStock(@Param("data") T1 data , @Param("apiCommon") ApiCommonDTO<T2> apiCommon);
	<T> List<WstkkyDTO> getApiLocationStockInfo (@Param("data") WstkkyDTO data , @Param("apiCommon") ApiCommonDTO<T> apiCommon);
	<T> int updateApiStockLayerSort(@Param("data") WstkkyDTO data , @Param("apiCommon") ApiCommonDTO<T> apiCommon);
	WstkkyDTO isSameLocAndLayerApi(@Param("data") SelectionTaskDTO data , @Param("apiCommon") ApiCommonDTO<SelectionTaskDTO> apiCommon);
	<T> int updateApiWstkky(@Param("apiCommon") ApiCommonDTO<T> apiCommon); // 작업 완료 시 재고 update
	int updateSelectionTask(@Param("apiCommon") ApiCommonDTO<SelectionTaskDTO> apiCommon); // 작업 완료
	int updateSelectionCompl(@Param("apiCommon") ApiCommonDTO<SelectionOperationComplDTO> apiCommon); // 조업 완료
}
