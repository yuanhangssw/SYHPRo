package com.tianji.dam.service;


import java.util.List;

import com.tianji.dam.domain.Sysconfig;

public interface SysconfigService {
    int insertSysconfig(Sysconfig sysconfig);
    int delSysconfig(int id);
    int updateSysconfig(Sysconfig sysconfig);
    List<Sysconfig> findSysconfigAll();
    Sysconfig findSysById(int id);
    Sysconfig findSysBySysKey(String key);
}
