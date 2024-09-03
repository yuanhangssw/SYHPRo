package com.tianji.dam.service.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianji.dam.domain.Material;
import com.tianji.dam.mapper.MaterialMapper;
import com.tianji.dam.service.MaterialService;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;


@Service
@DataSource(value = DataSourceType.SLAVE)
public class MaterialServiceImpl implements MaterialService {
	
		  @Autowired
		  MaterialMapper  mapper;

		@Override
		public Integer insertMaterial(Material ma) {
			//
			return mapper.insert(ma);
		}

		@Override
		public int deleteMaterial(Integer MaterialID) {
			// TODO Auto-generated method stub
			return mapper.deleteByPrimaryKey(MaterialID);
		}

		@Override
		public int updateMaterial(Material ma) {
			// TODO Auto-generated method stub
			return mapper.updateByPrimaryKeySelective(ma);
		}

		@Override
		public List<Material> findMaterial(Material param) {
			// TODO Auto-generated method stub
			return mapper.findAllMaterial(param);
		}
  
		@Override
		public List<Material> findMaterial() {
			// TODO Auto-generated method stub
			return mapper.findAllMaterial();
		}
		@Override
		public Material selectMaterialByID(Integer MaterialID) {
			// TODO Auto-generated method stub
			return mapper.selectByPrimaryKey(MaterialID);
		}
	

	
}
