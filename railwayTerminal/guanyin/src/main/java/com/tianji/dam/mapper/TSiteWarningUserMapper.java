package com.tianji.dam.mapper;

import com.tianji.dam.domain.TSiteWarningUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 站点预警用户Mapper接口
 * 
 * @author tj
 * @date 2022-01-05
 */
@Mapper
@Component
public interface TSiteWarningUserMapper 
{
    /**
     * 查询站点预警用户
     * 
     * @param userGid 站点预警用户主键
     * @return 站点预警用户
     */
    public TSiteWarningUser selectTSiteWarningUserByUserGid(String userGid);

    /**
     * 查询站点预警用户列表
     * 
     * @param tSiteWarningUser 站点预警用户
     * @return 站点预警用户集合
     */
    public List<TSiteWarningUser> selectTSiteWarningUserList(TSiteWarningUser tSiteWarningUser);
    public List<TSiteWarningUser> selectTSiteWarningUserList2(TSiteWarningUser tSiteWarningUser);
    /**
     * 新增站点预警用户
     * 
     * @param tSiteWarningUser 站点预警用户
     * @return 结果
     */
    public int insertTSiteWarningUser(TSiteWarningUser tSiteWarningUser);

    /**
     * 修改站点预警用户
     * 
     * @param tSiteWarningUser 站点预警用户
     * @return 结果
     */
    public int updateTSiteWarningUser(TSiteWarningUser tSiteWarningUser);

    /**
     * 删除站点预警用户
     * 
     * @param userGid 站点预警用户主键
     * @return 结果
     */
    public int deleteTSiteWarningUserByUserGid(String userGid);

    /**
     * 批量删除站点预警用户
     * 
     * @param userGids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSiteWarningUserByUserGids(String[] userGids);
    /**
     * 判断相同站点是否有重复的电话号码
     *
     * */
    public List<TSiteWarningUser> checkTelSiteUnique(TSiteWarningUser tSiteWarningUser);
}
