package com.kbph.logistics.bm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.bm.domain.BaddcoDTO;
import com.kbph.logistics.bm.domain.BsizpaDTO;
import com.kbph.logistics.bm.domain.BtrcatDTO;
import com.kbph.logistics.bm.domain.BtrfPrcDTO;
import com.kbph.logistics.bm.domain.BtrfhdDTO;
import com.kbph.logistics.bm.domain.BtrfitDTO;
import com.kbph.logistics.bm.domain.BtrfmaDTO;
import com.kbph.logistics.bm.domain.BtrpPrcDTO;
import com.kbph.logistics.bm.domain.BtrphdDTO;
import com.kbph.logistics.bm.domain.BtrpitDTO;
import com.kbph.logistics.bm.domain.BtrpmaDTO;
import com.kbph.logistics.sy.domain.UserVO;

@Mapper
public interface BillingMasterMapper {

	List<BaddcoDTO> selectBaddcoList(@Param("param") BaddcoDTO baddco, @Param("schema") String schema);
	
	List<BaddcoDTO> selectAddCostSizeKey(@Param("param") BaddcoDTO baddco, @Param("schema") String schema);
	
	String getAddcoky(@Param("userInfo") UserVO userInfo);
	
	int insertBaddco(@Param("param") BaddcoDTO baddco, @Param("userInfo") UserVO userInfo);
	
	int updateBaddco(@Param("param") BaddcoDTO baddco, @Param("userInfo") UserVO userInfo);
	
	int deleteBaddco(@Param("param") BaddcoDTO baddco, @Param("userInfo") UserVO userInfo);
	
	List<BtrcatDTO> selectBtrcatList(@Param("param") BtrcatDTO btrcat, @Param("schema") String schema);
	
	String getBtrcate(@Param("userInfo") UserVO userInfo);

	int insertBtrcat(@Param("param") BtrcatDTO btrcat, @Param("userInfo") UserVO userInfo);
	
	int updateBtrcat(@Param("param") BtrcatDTO btrcat, @Param("userInfo") UserVO userInfo);
	
	int deleteBtrcat(@Param("param") BtrcatDTO btrcat, @Param("userInfo") UserVO userInfo);
	
	List<BtrfmaDTO> selectBtrfmaList(@Param("param") BtrfmaDTO btrfma, @Param("userInfo") UserVO userInfo);
	
	List<BsizpaDTO> selectBsizpaList(@Param("param") BsizpaDTO bsizpa, @Param("schema") String schema);
	
	int insertBsizpa(@Param("param") BsizpaDTO bsizpa, @Param("userInfo") UserVO userInfo);
	
	int updateBsizpa(@Param("param") BsizpaDTO bsizpa, @Param("userInfo") UserVO userInfo);
	
	int deleteBsizpa(@Param("param") BsizpaDTO bsizpa, @Param("userInfo") UserVO userInfo);
	
	String getBtrfkey(@Param("userInfo") UserVO userInfo);
	
	String getBtrflsq(@Param("userInfo") UserVO userInfo);
	
	int insertBtrfma(@Param("param") BtrfmaDTO btrfma, @Param("userInfo") UserVO userInfo);
	
	int insertBtrfmaLog(@Param("param") BtrfmaDTO btrfma, @Param("userInfo") UserVO userInfo);
	
	int updateBtrfma(@Param("param") BtrfmaDTO btrfma, @Param("userInfo") UserVO userInfo);
	
	int deleteBtrfma(@Param("param") BtrfmaDTO btrfma, @Param("userInfo") UserVO userInfo);
	
	int updateBtrfmaSizeyn(@Param("param") BsizpaDTO bsizpa, @Param("userInfo") UserVO userInfo);
	
	List<BtrpmaDTO> selectBtrpmaList(@Param("param") BtrpmaDTO btrpma, @Param("userInfo") UserVO userInfo);
	
	int updateBtrpmaSizeyn(@Param("param") BsizpaDTO bsizpa, @Param("userInfo") UserVO userInfo);
	
	List<BsizpaDTO> selectDoe081BsizpaList(@Param("param") BsizpaDTO bsizpa, @Param("userInfo") UserVO userInfo);
	
	String getBtrplsq(@Param("userInfo") UserVO userInfo);
	
	String getBtrpkey(@Param("userInfo") UserVO userInfo);
	
	int insertBtrpma(@Param("param") BtrpmaDTO btrpma, @Param("userInfo") UserVO userInfo);
	
	int insertBtrpmaLog(@Param("param") BtrpmaDTO btrpma, @Param("userInfo") UserVO userInfo);
	
	int updateBtrpma(@Param("param") BtrpmaDTO btrpma, @Param("userInfo") UserVO userInfo);
	
	int deleteBtrpma(@Param("param") BtrpmaDTO btrpma, @Param("userInfo") UserVO userInfo);
	
	/*******이송정산처리*******/
	List<BtrfPrcDTO> selectDoe082List(@Param("param") BtrfPrcDTO btrfParam, @Param("userInfo") UserVO userInfo);
	
	String getBfhdkey(@Param("userInfo") UserVO userInfo);
	
	int insertBtrfhdFromBtrfit(@Param("param") BtrfhdDTO btrfhd, @Param("userInfo") UserVO userInfo);		//이송정산비처리 BTRFHD
	
	int insertBtrfit(@Param("param") BtrfPrcDTO btrfParam, @Param("userInfo") UserVO userInfo);		//이송정산비처리 BTRFIT
	
	int updateWrcvhdCloseyn(@Param("param") BtrfhdDTO btrfParam, @Param("userInfo") UserVO userInfo);	//입고문서아이템 정산마감
	
	/*******이송정산검증*******/
	List<BtrfhdDTO> selectBtrfhdList(@Param("param") BtrfhdDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
	
	List<BtrfPrcDTO> selectBeforeBtrfHeadList(@Param("param") BtrfPrcDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
	
	List<BtrfitDTO> selectBtrfitList(@Param("param") BtrfitDTO btrfitParam, @Param("userInfo") UserVO userInfo);
	
	int updateBtrfhdList(@Param("param") BtrfhdDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
	
	int updateBtrfitList(@Param("param") BtrfitDTO btrfitParam, @Param("userInfo") UserVO userInfo);
	
	int deleteBtrfhd(@Param("param") BtrfhdDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
	
	int deleteBtrfit(@Param("param") BtrfPrcDTO btrfParam, @Param("userInfo") UserVO userInfo);
	
	int updateDoe083BtrfcbtList(@Param("param") BtrfitDTO btrfitParam, @Param("userInfo") UserVO userInfo);
	/*******이송정산확정*******/
	int updateBtrfhdConfirm(@Param("param") BtrfhdDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
	
	int updateDoe084BtrfhdPayadyn(@Param("param") BtrfhdDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
	
	int updateDoe084WrcvhdPayadyn(@Param("param") BtrfhdDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
	
	int updateDoe084TplnhdPayadyn(@Param("param") BtrfhdDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
	
	List<BtrfPrcDTO> selectDoe088Md8SummaryList (@Param("param") BtrfPrcDTO btrfParam, @Param("userInfo") UserVO userInfo);
	
	List<BtrfhdDTO> selectDoe084VerifySummary(@Param("param") BtrfhdDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
	/*******운송정산*******/
	List<BtrpPrcDTO> selectDoe085List(@Param("param") BtrpPrcDTO btrfParam, @Param("userInfo") UserVO userInfo);
	
	List<BtrphdDTO> selectBtrphdList(@Param("param") BtrphdDTO btrphdParam, @Param("userInfo") UserVO userInfo);
	
	List<BtrpitDTO> selectBtrpitList(@Param("param") BtrpitDTO btrpitParam, @Param("userInfo") UserVO userInfo);
	
	int deleteBtrphd(@Param("param") BtrphdDTO btrphdParam ,@Param("userInfo") UserVO userInfo);
	
	int deleteBtrpit(@Param("param") BtrpitDTO btrphdParam ,@Param("userInfo") UserVO userInfo);
	
	String getBphdkey(@Param("userInfo") UserVO userInfo);
	
	int insertBtrpitList(@Param("param") BtrpPrcDTO btrpParam, @Param("userInfo") UserVO userInfo);
	
	int insertBtrphdFromBtrpit(@Param("param") BtrphdDTO btrpParam, @Param("userInfo") UserVO userInfo);
	
	int updateTplnhdCloseyn(@Param("param") BtrphdDTO btrpParam, @Param("userInfo") UserVO userInfo);
	
	int updateDoe086Validate(@Param("param") BtrphdDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
	
	int updateDoe086Item(@Param("param") BtrpitDTO btrfitParam, @Param("userInfo") UserVO userInfo);
	
	int updateDoe086BtrpcbtList(@Param("param") BtrpitDTO btrpitParam, @Param("userInfo") UserVO userInfo);
	
	int insertBppahdList(@Param("param") BtrphdDTO btrpParam, @Param("userInfo") UserVO userInfo);
	
	int insertBppaitList(@Param("param") BtrphdDTO btrpParam, @Param("userInfo") UserVO userInfo);
	
	int deleteBppahd(@Param("param") BtrphdDTO btrpParam, @Param("userInfo") UserVO userInfo);
	int deleteBppait(@Param("param") BtrphdDTO btrpParam, @Param("userInfo") UserVO userInfo);
	int updateTplnhdPayadyn(@Param("param") BtrphdDTO btrpParam, @Param("userInfo") UserVO userInfo);
	int updateBtrphdPayadyn(@Param("param") BtrphdDTO btrpParam, @Param("userInfo") UserVO userInfo);
	List<BtrphdDTO> selectDoe087VerifySummary(@Param("param") BtrphdDTO btrphdParam, @Param("userInfo") UserVO userInfo);
	//검증완료 및 취소 공통 쿼리
	int updateBtrphdYnList(@Param("param") BtrphdDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
	
	int updateBtrphdConfirm(@Param("param") BtrphdDTO btrfhdParam, @Param("userInfo") UserVO userInfo);
}
