package com.tianji.dam.service;


import java.util.List;

import com.tianji.dam.domain.Material;


public interface MaterialService {
    Integer insertMaterial(Material ma);
    int deleteMaterial(Integer MaterialID);
    int updateMaterial(Material ma);
    List<Material> findMaterial(Material param);
    List<Material> findMaterial();
    Material selectMaterialByID(Integer MaterialID);
}
