package com.kbph.logistics.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.api.domain.ApiCommonDTO;

@Mapper
public interface ApiMapper {
	int insertShipping(@Param("apiCommon") ApiCommonDTO<?> apiCommon);
}
