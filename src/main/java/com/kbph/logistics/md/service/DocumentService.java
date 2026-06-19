package com.kbph.logistics.md.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.md.domain.MdocmaDTO;
import com.kbph.logistics.md.mapper.DocumentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {
	private final DocumentMapper documentMapper;

	// 문서 유형 리스트 조회
	public List<MdocmaDTO> getMdocmaDoccateList(MdocmaDTO params) {
		return documentMapper.selectMdocmaDoccateList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 문서 관계 리스트 조회 (창고, 문서유형, 문서타입)
	public List<MdocmaDTO> getWareCateTypeRelations(MdocmaDTO params) {
		return documentMapper.selectWareCateTypeRelations(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 문서 유형 셀렉트박스 조회
	public List<MdocmaDTO> getDocumentCategorySelectBox(MdocmaDTO params) {
		return documentMapper.selectDoccateSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 문서 타입 셀렉트박스 조회
	public List<MdocmaDTO> getDocumentTypeSelectBox(MdocmaDTO params) {
		if (params.getDoccate() != null) {
			params.setDoccates(Arrays.asList(params.getDoccate().split(",")));
			params.setDoccate(null);
		}

		if (params.getDoctype() != null) {
			params.setDoctypes(Arrays.asList(params.getDoctype().split(",")));
			params.setDoctype(null);
		}
		// 더 추가될 수 있음
		return documentMapper.selectDoctypeSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}
}
