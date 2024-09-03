package com.tianji.dam.service.impl;

import java.util.List;
import java.util.UUID;

import com.tj.common.utils.DateUtils;
import com.tj.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianji.dam.mapper.TWarningUserMapper;
import com.tianji.dam.domain.TWarningUser;
import com.tianji.dam.service.ITWarningUserService;

/**
 * 预警用户维护Service业务层处理
 * 
 * @author liyan
 * @date 2022-06-09
 */
@Service
public class TWarningUserServiceImpl implements ITWarningUserService 
{
    @Autowired
    private TWarningUserMapper tWarningUserMapper;

    /**
     * 查询预警用户维护
     * 
     * @param userGid 预警用户维护主键
     * @return 预警用户维护
     */
    @Override
    public TWarningUser selectTWarningUserByUserGid(String userGid)
    {
        return tWarningUserMapper.selectTWarningUserByUserGid(userGid);
    }

    /**
     * 查询预警用户维护列表
     * 
     * @param tWarningUser 预警用户维护
     * @return 预警用户维护
     */
    @Override
    public List<TWarningUser> selectTWarningUserList(TWarningUser tWarningUser)
    {
        return tWarningUserMapper.selectTWarningUserList(tWarningUser);
    }

    /**
     * 新增预警用户维护
     * 
     * @param tWarningUser 预警用户维护
     * @return 结果
     */
    @Override
    public int insertTWarningUser(TWarningUser tWarningUser)
    {
        tWarningUser.setCreateTime(DateUtils.getNowDate());
        tWarningUser.setUserGid(UUID.randomUUID().toString().replace("-",""));
        tWarningUser.setCreateBy(SecurityUtils.getUsername());
        tWarningUser.setStatus(0l);
        return tWarningUserMapper.insertTWarningUser(tWarningUser);
    }

    /**
     * 修改预警用户维护
     * 
     * @param tWarningUser 预警用户维护
     * @return 结果
     */
    @Override
    public int updateTWarningUser(TWarningUser tWarningUser)
    {
        return tWarningUserMapper.updateTWarningUser(tWarningUser);
    }

    /**
     * 批量删除预警用户维护
     * 
     * @param userGids 需要删除的预警用户维护主键
     * @return 结果
     */
    @Override
    public int deleteTWarningUserByUserGids(String[] userGids)
    {
        return tWarningUserMapper.deleteTWarningUserByUserGids(userGids);
    }

    /**
     * 删除预警用户维护信息
     * 
     * @param userGid 预警用户维护主键
     * @return 结果
     */
    @Override
    public int deleteTWarningUserByUserGid(String userGid)
    {
        return tWarningUserMapper.deleteTWarningUserByUserGid(userGid);
    }
}
