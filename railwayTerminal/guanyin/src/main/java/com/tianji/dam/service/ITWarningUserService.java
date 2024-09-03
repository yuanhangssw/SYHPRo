package com.tianji.dam.service;

import java.util.List;
import com.tianji.dam.domain.TWarningUser;

/**
 * 预警用户维护Service接口
 * 
 * @author liyan
 * @date 2022-06-09
 */
public interface ITWarningUserService 
{
    /**
     * 查询预警用户维护
     * 
     * @param userGid 预警用户维护主键
     * @return 预警用户维护
     */
    public TWarningUser selectTWarningUserByUserGid(String userGid);

    /**
     * 查询预警用户维护列表
     * 
     * @param tWarningUser 预警用户维护
     * @return 预警用户维护集合
     */
    public List<TWarningUser> selectTWarningUserList(TWarningUser tWarningUser);

    /**
     * 新增预警用户维护
     * 
     * @param tWarningUser 预警用户维护
     * @return 结果
     */
    public int insertTWarningUser(TWarningUser tWarningUser);

    /**
     * 修改预警用户维护
     * 
     * @param tWarningUser 预警用户维护
     * @return 结果
     */
    public int updateTWarningUser(TWarningUser tWarningUser);

    /**
     * 批量删除预警用户维护
     * 
     * @param userGids 需要删除的预警用户维护主键集合
     * @return 结果
     */
    public int deleteTWarningUserByUserGids(String[] userGids);

    /**
     * 删除预警用户维护信息
     * 
     * @param userGid 预警用户维护主键
     * @return 结果
     */
    public int deleteTWarningUserByUserGid(String userGid);
}
