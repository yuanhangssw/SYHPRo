package com.tianji.dam.service.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianji.dam.domain.Sysconfig;
import com.tianji.dam.domain.SysconfigExample;
import com.tianji.dam.mapper.SysconfigMapper;
import com.tianji.dam.service.SysconfigService;
@Service
public class SysconfigServiceImpl implements SysconfigService {
    @Autowired
    private SysconfigMapper sysconfigMapper;
    @Override
    public int insertSysconfig(Sysconfig sysconfig) {
        return this.sysconfigMapper.insert(sysconfig);
    }
   
    @Override
    public int delSysconfig(int id) {
        return this.sysconfigMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateSysconfig(Sysconfig sysconfig) {
        return this.sysconfigMapper.updateByPrimaryKey(sysconfig);
    }

    @Override
    public List<Sysconfig> findSysconfigAll() {
        return this.sysconfigMapper.selectByExample(new SysconfigExample());
    }

    @Override
    public Sysconfig findSysById(int id) {
        return this.sysconfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public Sysconfig findSysBySysKey(String key) {
        return this.sysconfigMapper.selectBySyskey(key);
    }
}
