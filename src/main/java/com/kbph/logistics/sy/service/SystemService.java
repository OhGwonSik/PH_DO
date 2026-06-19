package com.kbph.logistics.sy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.DeleteCheckedException;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.sy.domain.DashBoardDTO;
import com.kbph.logistics.sy.domain.RoleMenuWhDTO;
import com.kbph.logistics.sy.domain.SmnghdDTO;
import com.kbph.logistics.sy.domain.SmnubmDTO;
import com.kbph.logistics.sy.domain.SmnumaDTO;
import com.kbph.logistics.sy.domain.SrolgrDTO;
import com.kbph.logistics.sy.domain.SrolmnDTO;
import com.kbph.logistics.sy.domain.SrolwhDTO;
import com.kbph.logistics.sy.domain.SusrvhDTO;
import com.kbph.logistics.sy.domain.TotalDashBoardDTO;
import com.kbph.logistics.sy.domain.UserDTO;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.sy.mapper.SystemMapper;
import com.kbph.logistics.sy.mapper.UserMapper;
import com.kbph.logistics.sy.type.SusrmaFlag;
import com.kbph.logistics.sy.type.SusrvhFlag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemService {
	private final SystemMapper systemMapper;
	private final UserMapper userMapper;

	public List<SmnghdDTO> getSmnghdList(SmnghdDTO smnghd) {
		return systemMapper.selectSmnghdList(smnghd, SecurityUtils.getSchema());
	}

	public List<SmnumaDTO> getMenuList(SmnumaDTO smnumaDTO){
		return systemMapper.getMenuList(smnumaDTO, SecurityUtils.getSchema());
	}

	public List<SrolgrDTO> getRoleList (SrolgrDTO srolgrDTO){
		return systemMapper.getRoleList(srolgrDTO, SecurityUtils.getSchema());
	}

	public List<SmnumaDTO> getMenuByRoleList(SmnumaDTO smnumaDTO){
		return systemMapper.getMenuByRoleList(smnumaDTO, SecurityUtils.getSchema());
	}

	public List<SrolwhDTO> getWarehouseByRoleList(SrolwhDTO srolwhDTO){
		return systemMapper.getWarehouseByRoleList(srolwhDTO, SecurityUtils.getSchema());
	}

	public int saveSmnuma(GridDTO<SmnumaDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();

		int count = 0;

		String menukey = "";
		if(!requestDTO.getAddList().isEmpty()) {
			menukey = systemMapper.selectMenukey(SecurityUtils.getSchema());
		}
		//메뉴 저장
		for(SmnumaDTO smnumaDTO : requestDTO.getAddList()) {

			smnumaDTO.setMenukey(menukey);
			smnumaDTO.setMnuseyn("Y");

			count = systemMapper.insertSmnuma(smnumaDTO, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}

		}
		//메뉴 수정
		for(SmnumaDTO smnumaDTO : requestDTO.getUpdateList()) {

			smnumaDTO.setMnuseyn("Y");

			count = systemMapper.updateSmnuma(smnumaDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		//메뉴 삭제
		for(SmnumaDTO smnumaDTO : requestDTO.getDeleteList()) {

			smnumaDTO.setMnuseyn("N");

			count = systemMapper.updateSmnuma(smnumaDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}

		return count;
	}

	public int saveSrolgr(GridDTO<SrolgrDTO> requestDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		//권한 저장
		for(SrolgrDTO srolgrDTO : requestDTO.getAddList()) {
			count = systemMapper.insertSrolgr(srolgrDTO, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}

		}
		//권한 수정
		for(SrolgrDTO srolgrDTO : requestDTO.getUpdateList()) {

			srolgrDTO.setRluseyn("Y");

			count = systemMapper.updateSrolgr(srolgrDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		//권한 삭제
		for(SrolgrDTO srolgrDTO : requestDTO.getDeleteList()) {

			srolgrDTO.setRluseyn("N");

			count = systemMapper.updateSrolgr(srolgrDTO, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}

		return count;
	}

	public int saveMenuByRole(RoleMenuWhDTO<SrolmnDTO> roleMenuWhDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;


		for(SrolmnDTO srolmnDTO : roleMenuWhDTO.getRoleMenuWhList()) {


			if(srolmnDTO.getRolnmky() == null) {
				srolmnDTO.setRolnmky(systemMapper.selectRolnmky(SecurityUtils.getSchema()));
			}

			if("true".equals(srolmnDTO.getState())) {
				srolmnDTO.setRmuseyn("Y");
			}else {
				srolmnDTO.setRmuseyn("N");
			}

			srolmnDTO.setRolgkey(roleMenuWhDTO.getRolgkey());

			count = systemMapper.saveMenuByRole(srolmnDTO, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}
		}

		return count;
	}

	public int saveSrolwh(RoleMenuWhDTO<SrolwhDTO> roleMenuWhDTO) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		int count = 0;

		for(SrolwhDTO srolwhDTO : roleMenuWhDTO.getRoleMenuWhList()) {

			srolwhDTO.setRolgkey(roleMenuWhDTO.getRolgkey());
			count = systemMapper.saveSrolwh(srolwhDTO, userVO);

			if(count == 0) {
				throw new InsertCheckedException();
			}
		}
		return count;
	}

	public int saveBookMark(SmnubmDTO smnubm) {
		int result = systemMapper.insertBookMark(smnubm, SecurityUtils.getSchema());
		if(result != 1) {
			throw new InsertCheckedException();
		}
		return result;
	}

	public int deleteBookMark(SmnubmDTO smnubm) {
		int result = systemMapper.deleteBookMark(smnubm, SecurityUtils.getSchema());
		if(result != 1) {
			throw new DeleteCheckedException();
		}
		return result;
	}

	public List<UserDTO> getAppUserList(UserDTO user) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		user.setActivyn(SusrmaFlag.ACTIVE.getString());
		user.setApuseyn(SusrmaFlag.APP_USER.getString());
		return systemMapper.selectAppUserList(user, userVO);
	}

	public List<SusrvhDTO> syu05VehicleInitList(SusrvhDTO susrvh){
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		return systemMapper.syu05VehicleInitList(susrvh, userVO);
	}

	public List<SusrvhDTO> getVehicleByUser(SusrvhDTO susrvh) {
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();
		susrvh.setActivyn(SusrmaFlag.ACTIVE.getString());
		susrvh.setVhuseyn(SusrvhFlag.MAPPING_USE.getString());
		return systemMapper.selectVehicleByUser(susrvh, userVO);
	}

	public int syu05SaveDriver(GridDTO<SusrvhDTO> requestDTO) {
		int count = 0;
		UserVO userVO = SecurityUtils.getCustomUserDetails().getUserInfo();

		//addList(insert)
		for(SusrvhDTO addList : requestDTO.getAddList()) {
			addList.setVhownyn(SusrvhFlag.NOT_VHICLE_OWNER.getString()); // 차주여부
			count = userMapper.insertSusrvh(addList, userVO);

			if(count == 0 ) {
				throw new InsertCheckedException();
			}
		}

		//updateList(update -> vhuseyn 으로 삭제 여부 확인)
		for(SusrvhDTO updateList : requestDTO.getUpdateList()) {
			count = systemMapper.syu05UpdateDriver(updateList, userVO);

			if(count == 0) {
				throw new UpdateCheckedException();
			}
		}
		return count;
	}

	public TotalDashBoardDTO getDashBoardInfo(DashBoardDTO dashboard) {
		TotalDashBoardDTO totalDashBoard = new TotalDashBoardDTO();

		String schema = SecurityUtils.getSchema();
		// 입출고 현황
		totalDashBoard.setInboundBarData(systemMapper.selectInboundBarData(dashboard, schema));
		totalDashBoard.setInboundDoughnutList(systemMapper.selectInboundDoughnutList(dashboard, schema));
		totalDashBoard.setOutboundBarData(systemMapper.selectOutboundBarData(dashboard, schema));
		totalDashBoard.setOutboundDoughnutList(systemMapper.selectOutboundDoughnutList(dashboard, schema));

		// 운송 현황
		totalDashBoard.setTransGridList(systemMapper.selectTransGridList(dashboard, schema));
		totalDashBoard.setTransPieList(systemMapper.selectTransPieList(dashboard, schema));

		// 평균 하치장 체류시간
		totalDashBoard.setStayedYardBarData(systemMapper.selectStayedYardBarData(dashboard, schema));
		
		//설비 작업 현황
		totalDashBoard.setEquipmentTaskList(systemMapper.selectEquipmentTaskList(dashboard, schema));

		return totalDashBoard;
	}
}