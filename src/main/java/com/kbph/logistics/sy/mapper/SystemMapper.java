package com.kbph.logistics.sy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.sy.domain.DashBoardDTO;
import com.kbph.logistics.sy.domain.SmnghdDTO;
import com.kbph.logistics.sy.domain.SmnubmDTO;
import com.kbph.logistics.sy.domain.SmnumaDTO;
import com.kbph.logistics.sy.domain.SrolgrDTO;
import com.kbph.logistics.sy.domain.SrolmnDTO;
import com.kbph.logistics.sy.domain.SrolwhDTO;
import com.kbph.logistics.sy.domain.SusrvhDTO;
import com.kbph.logistics.sy.domain.UserDTO;
import com.kbph.logistics.sy.domain.UserVO;

@Mapper
public interface SystemMapper {
	List<SmnghdDTO> selectSmnghdList(@Param("param") SmnghdDTO smnghd, @Param("schema") String schema);

	//메뉴등록
	String selectMenukey(@Param("schema") String schema);

	List<SmnumaDTO> getMenuList(@Param("param") SmnumaDTO smnumaDTO, @Param("schema") String schema);

	int insertSmnuma(@Param("param") SmnumaDTO smnumaDTO, @Param("userData") UserVO userVO);
	int updateSmnuma(@Param("param") SmnumaDTO smnumaDTO, @Param("userData") UserVO userVO);

	//권한등록
	List<SrolgrDTO> getRoleList (@Param("param") SrolgrDTO srolgrDTO, @Param("schema") String schema);

	List<SmnumaDTO>getMenuByRoleList(@Param("param") SmnumaDTO smnumaDTO, @Param("schema") String schema);

	List<SrolwhDTO> getWarehouseByRoleList(@Param("param") SrolwhDTO srolwhDTO, @Param("schema") String schema);

	String selectRolnmky(@Param("schema") String schema);
	int saveMenuByRole(@Param("param") SrolmnDTO srolmnDTO, @Param("userData")UserVO userVO);

	int insertSrolgr(@Param("param") SrolgrDTO srolgrDTO, @Param("userData")UserVO userVO);
	int updateSrolgr(@Param("param") SrolgrDTO srolgrDTO, @Param("userData")UserVO userVO);

	int saveSrolwh(@Param("param") SrolwhDTO srolwhDTO, @Param("userData")UserVO userVO);

	//북마크
	int insertBookMark(@Param("param") SmnubmDTO smnubm, @Param("schema") String schema);
	int deleteBookMark(@Param("param") SmnubmDTO smnubm, @Param("schema") String schema);

	List<SusrvhDTO> syu05VehicleInitList(@Param("param") SusrvhDTO susrvh, @Param("userData")UserVO userVO);
	List<UserDTO> selectAppUserList(@Param("param") UserDTO user, @Param("userData")UserVO userVO);
	List<SusrvhDTO> selectVehicleByUser(@Param("param") SusrvhDTO susrvh, @Param("userData")UserVO userVO);

	int syu05UpdateDriver(@Param("param") SusrvhDTO susrvh, @Param("userData")UserVO userVO);
	int syu05DeleteDriver(@Param("param") SusrvhDTO susrvh, @Param("userData")UserVO userVO);

	DashBoardDTO selectInboundBarData(@Param("param") DashBoardDTO dashBoard, @Param("schema") String schema);
	List<DashBoardDTO> selectInboundDoughnutList(@Param("param") DashBoardDTO dashBoard, @Param("schema") String schema);
	DashBoardDTO selectOutboundBarData(@Param("param") DashBoardDTO dashBoard, @Param("schema") String schema);
	List<DashBoardDTO> selectOutboundDoughnutList(@Param("param") DashBoardDTO dashBoard, @Param("schema") String schema);
	DashBoardDTO selectStayedYardBarData(@Param("param") DashBoardDTO dashBoard, @Param("schema") String schema);
	List<DashBoardDTO> selectTransGridList(@Param("param") DashBoardDTO dashBoard, @Param("schema") String schema);
	List<DashBoardDTO> selectTransPieList(@Param("param") DashBoardDTO dashBoard, @Param("schema") String schema);
	List<DashBoardDTO> selectEquipmentTaskList(@Param("param") DashBoardDTO dashBoard, @Param("schema") String schema);
	// 인쇄물
	String selectPrintsq(@Param("schema") String schema);
}