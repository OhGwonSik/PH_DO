package com.kbph.logistics.im.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.im.domain.InboundOrderForTreeGridDTO;
import com.kbph.logistics.im.domain.OstrhdDTO;
import com.kbph.logistics.im.domain.OstritDTO;
import com.kbph.logistics.im.domain.OstritSeacrhParamDTO;
import com.kbph.logistics.im.domain.WasnhdDTO;
import com.kbph.logistics.im.domain.WasnitDTO;
import com.kbph.logistics.im.domain.WasnitSeacrhParamDTO;
import com.kbph.logistics.im.domain.WrcvhdDTO;
import com.kbph.logistics.im.domain.WrcvitDTO;
import com.kbph.logistics.im.domain.WrcvitSeacrhParamDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;
import com.kbph.logistics.sy.domain.UserVO;
/**
 * @author : OP
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Mapper Class for Inbound
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09					   OP           					create Mapper class
 * 2024-08-22					t.s.park        			create Inbound order method
 * 2024-08-29					t.s.park          	create Inbound document method
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Mapper
public interface InboundMapper {
	/** 입고오더 */
	// 입고오더 아이템 조회
	public List<OstritDTO> getOstritList(@Param("param") OstritDTO ostritDTO, @Param("userData") UserVO userVO);
	// 입고오더 조회(간소화)
	public OstrhdDTO selectOstrhdKey(@Param("param") OstrhdDTO ostrhdDTO, @Param("userData") UserVO userVO);
	// 입고오더아이템 조회(간소화)
	public List<OstritDTO> selectOstritKeyAndItem(@Param("param") OstritSeacrhParamDTO param, @Param("userData") UserVO userVO);
	// 입고오더헤더 생성
	public int insertOstrhdList(@Param("insertList") List<InboundOrderForTreeGridDTO> inboundOrderList, @Param("userData") UserVO userVO);
	// 입고오더아이템 생성
	public int insertOstritList(@Param("insertList") List<InboundOrderForTreeGridDTO> inboundOrderItemList, @Param("userData") UserVO userVO);
	// 입고오더헤더 상태변경
	public int updateOstrhdStatus(@Param("param") OstrhdDTO param, @Param("status") String status, @Param("userData") UserVO userVO);
	// 입고오더아이템 상태변경
	public int updateOstritStatus(@Param("updateList") List<OstritDTO> updateList, @Param("status") String status, @Param("withoutCancel") boolean withoutCancel, @Param("userData") UserVO userVO);
	// 입고오더번호 채번
	public String selectCostrky(@Param("userData") UserVO userVO);
	// 입고오더아이템 번호 채번
	public int selectCostrit(@Param("costrky") String costrky, @Param("userData") UserVO userVO);
	/** 입고오더 end */

	/** ASN */
	// 직송 헤더
	public int saveDirectShippingHeader(@Param("param") WasnhdDTO wasnhdDTO, @Param("userInfo") UserVO userVO);
	// 직송 아이템
	public int saveDirectShippingItem(@Param("param") WasnhdDTO wasnhdDTO, @Param("userInfo") UserVO userVO);

	public int wasnitCount(@Param("param") WasnhdDTO wasnhdDTO, @Param("userInfo") UserVO userVO);

	public int deleteWasnhd(@Param("param") WasnhdDTO wasnhdDTO, @Param("userInfo") UserVO userVO);

	public int deleteWasnit(@Param("param") WasnhdDTO wasnhdDTO, @Param("userInfo") UserVO userVO);

	//입고예정 아이템 삭제
	public List<WasnitDTO> getDoe024AsnItemListStlayer(@Param("param") WasnitDTO wasnitDTO, @Param("userInfo") UserVO userVO);
	public int updateDoe024AsnItemStlayer(@Param("param") WasnitDTO wasnitDTO, @Param("userInfo") UserVO userVO);
	public int deleteDoe024Wasnit(@Param("param") WasnitDTO wasnitDTO, @Param("userInfo") UserVO userVO);
	public int deleteDoe024Mskuwc(@Param("param") WasnitDTO wasnitDTO, @Param("userInfo") UserVO userVO);

	// asn header 조회
	public List<WasnhdDTO> getWasnhdList(@Param("param") WasnhdDTO wasnhdDTO, @Param("userData") UserVO userVO);
	// asn item 조회
	public List<WasnitDTO> getWasnitList(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);

	//doe024 asn item 조회
	public List<WasnitDTO> getDoe024WasnitList(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	// asn item 조회(태스크 없는)
	public List<WasnitDTO> selectWasnitListWithoutTask(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	// asn header 조회(no join)
	public WasnhdDTO selectPlainWasnhd(@Param("param") WasnhdDTO wasnhdDTO, @Param("userData") UserVO userVO);
	// asn header 조회(필드 간소화) selectDoe024WasnhdKey
	public WasnhdDTO selectWasnhdKey(@Param("param") WasnhdDTO wasnhdDTO, @Param("userData") UserVO userVO);
	// asn item 조회 ALL
	public List<WasnitDTO> selectWasnitKeyAndItemAll(@Param("param") WasnitSeacrhParamDTO param, @Param("userData") UserVO userVO);
	// asn item 조회(필드 간소화)selectWasnitKeyAndItemAll
	public List<WasnitDTO> selectWasnitKeyAndItem(@Param("param") WasnitSeacrhParamDTO param, @Param("userData") UserVO userVO);
	// asn item 조회(필드 간소화) -> cancel 조건 추가
	public List<WasnitDTO> selectWasnitKeyAndItemNotCancel(@Param("param") WasnitSeacrhParamDTO param, @Param("userData") UserVO userVO);
	// 입고예정검수여부 update
	public int updateWasnhdAsnpiyn(@Param("param") WasnhdDTO wasnhdDTO, @Param("userData") UserVO userVO);
	// 기존 단 -> 첫 단 변경
	public int updateWasnitToTopLayer(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	// 첫 단 -> 단 교체
	public int updateWasnitSwapLayer(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	// 입고예정번호 채번(function call)
	public String selectEoasnky(@Param("userData") UserVO userVO);
	// 입고예정아이템 번호 채번(function call)
	public int selectEoasnit(@Param("eoasnky") String eoasnky, @Param("userData") UserVO userVO);
	// 입고예정정보헤더 insert
	public int insertWasnhdList(@Param("insertList") List<WasnhdDTO> insertList, @Param("userData") UserVO userVO);

	// 연관관계 확인
	public int checkRelationship(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	// 입고예정정보아이템 insert
	public int insertWasnitList(@Param("insertList") List<WasnitDTO> insertList, @Param("userData") UserVO userVO);
//	// 입고예정정보아이템 update
//	public int updateWasnitList(@Param("updateList") List<WasnitDTO> updateList, @Param("userData") UserVO userVO);

	// 입고예정정보헤더 상태(asnstat) update
	public int updateDoe024WasnhdStatus(@Param("param") WasnhdDTO param, @Param("status") String status, @Param("userData") UserVO userVO);
	// 입고예정정보헤더 상태(asnstat) update updateDoe024WasnhdStatus
	public int updateWasnhdStatus(@Param("param") WasnhdDTO param, @Param("status") String status, @Param("userData") UserVO userVO);
	// 입고예정정보아이템 상태(asnitst) update
	public int updateDoe024WasnitStatus(@Param("updateList") List<WasnitDTO> updateList, @Param("status") String status, @Param("withoutCancel") boolean withoutCancel, @Param("userData") UserVO userVO);
	// 입고예정정보아이템 상태(asnitst) update updateDoe024WasnitStatus
	public int updateWasnitStatus(@Param("updateList") List<WasnitDTO> updateList, @Param("status") String status, @Param("withoutCancel") boolean withoutCancel, @Param("userData") UserVO userVO);

	public int updateAsnitstDoe027(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public int updateHeadAsnstatDoe027(@Param("param") WasnhdDTO wasnhdDTO, @Param("userData") UserVO userVO);

	public int updateStritstDoe027(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public int updateOstrhdDoe027(@Param("param") OstrhdDTO ostrhdDTO, @Param("userData") UserVO userVO);

	// 입고예정정보 입차정보 입력(update)
	public int updateWasnhdVehicleEntranceInfo(@Param("updateList") List<WasnhdDTO> updateList, @Param("userData") UserVO userVO);

	// 입고검수대상확정
	public int updateWasnhdFlagForInspectionTarget(@Param("updateList") List<WasnhdDTO> updateList, @Param("userData") UserVO userData);

	// 입고정보등록
	// 재고키 채번(function call)
	public String selectStockky(@Param("userData") UserVO userVO);
	// LOT number 채번(function call)
	public String selectLotnmky(@Param("userData") UserVO userVO);
	public List<WasnhdDTO> getDoe033HeadList(@Param("param") WasnhdDTO wasnhdDTO, @Param("userData") UserVO userVO);
	public List<WasnitDTO> getDoe033ItemList(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public int getDoe033Layer(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public int saveDoe033Wstkky(@Param("item") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public int saveDoe033WtakitOperStart(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public int saveDoe033Wtakit(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public int saveDoe033WtakitOperCmp(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public int isAsnTaskAllOperCmp(@Param("param") WasnhdDTO wasnhdDTO, @Param("userData") UserVO userVO);
	public List<WasnitDTO> getWasnitUllockyGroup(@Param("param") WasnhdDTO wasnhdDTO, @Param("userData") UserVO userVO);
	public List<WasnitDTO> getWasnitUllockyGroupList(@Param("param") WasnitDTO wasnhdDTO, @Param("userData") UserVO userVO);
	/** asn end */

	/** 입고문서 */
	// 입고문서아이템 상태별 리스트 조회
	public List<WrcvitDTO> selectWrcvitStatusList(@Param("param") WrcvitSeacrhParamDTO param, @Param("userData") UserVO userVO);
	// 입고문서아이템 상태별 리스트 조회 - doe033용 -> ASN번호로 조회
	public List<WrcvitDTO> selectWrcvitRVCMPList(@Param("param") WrcvitSeacrhParamDTO param, @Param("userData") UserVO userVO);
	// 지시한 입고문서헤더 리스트 조회
	public List<WrcvhdDTO> selectTaskWrcvhdList(@Param("param") WrcvhdDTO wrcvhdDTO, @Param("userData") UserVO userVO);
	// 입고완료 확정 헤더 조회
	public List<WrcvhdDTO> selectDoe034TaskWrcvhdList(@Param("param") WrcvhdDTO wrcvhdDTO, @Param("userData") UserVO userVO);
	// 지시한 입고문서아이템 리스트 조회
	public List<WrcvitDTO> selectTaskWrcvitList(@Param("param") WrcvitDTO wrcvitDTO, @Param("userData") UserVO userVO);
	// 입고문서번호 채번(function call)
	public String selectRcvdcky(@Param("userData") UserVO userVO);
	// 입고문서아이템 번호 채번
	public int selectRcvdcit(@Param("rcvdcky") String rcvdcky, @Param("userData") UserVO userVO);
	// 입고문서헤더 생성(지시 등록)
	public int insertWrcvhd(@Param("param") WrcvhdDTO wrcvhdDTO, @Param("userData") UserVO userVO);
	// 입고문서아이템 생성(지시 등록)
	public int insertWrcvitList(@Param("insertList") List<WrcvitDTO> insertList, @Param("userData") UserVO userVO);
	// 입고문서헤더 상태 변경
	public int updateWrcvhdStatus(@Param("param") WrcvhdDTO param, @Param("status") String status, @Param("userData") UserVO userVO);
	// 입고문서아이템 상태 변경
	public int updateWrcvitStatus(@Param("updateList") List<WrcvitDTO> updateList, @Param("status") String status, @Param("withoutCancel") boolean withoutCancel, @Param("userData") UserVO userVO);
	/** 입고문서 end*/

	/** task */
	// 입고 task 생성
	public int insertWtakitList(@Param("insertList") List<WtakitDTO> insertList, @Param("userData") UserVO userVO);
	/** task end*/

	/** 재고유무 확인 */
	public WstkkyDTO isSameLocationAndLayer(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public WtakitDTO getAsnTaskoky(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public int updateWtakitStockky(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public List<WstkkyDTO> getFromLocationAndLayerStock(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public int updateLayerStockShift(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userVO);
	public WstkkyDTO getPhyCreateApiData(@Param("param") WasnitDTO wasnitDTO, @Param("userData") UserVO userVO);
	public List<WstkkyDTO> getPhyCreateLocationStockInfo(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userVO);
}
