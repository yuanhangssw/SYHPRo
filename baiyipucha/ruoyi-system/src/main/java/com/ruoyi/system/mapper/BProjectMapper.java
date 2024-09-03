package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.BProject;
import org.apache.ibatis.annotations.Select;

/**
 * 项目信息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
public interface BProjectMapper 
{
    /**
     * 查询项目信息
     * 
     * @param id 项目信息主键
     * @return 项目信息
     */
    public BProject selectBProjectById(Long id);

    /**
     * 查询项目信息列表
     * 
     * @param bProject 项目信息
     * @return 项目信息集合
     */
    public List<BProject> selectBProjectList(BProject bProject);

    /**
     * 新增项目信息
     * 
     * @param bProject 项目信息
     * @return 结果
     */
    public int insertBProject(BProject bProject);

    /**
     * 修改项目信息
     * 
     * @param bProject 项目信息
     * @return 结果
     */
    public int updateBProject(BProject bProject);

    /**
     * 删除项目信息
     * 
     * @param id 项目信息主键
     * @return 结果
     */
    public int deleteBProjectById(Long id);

    /**
     * 批量删除项目信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBProjectByIds(Long[] ids);


    public  List<BProject> getBProjectByDept(List<Long> deptId);

    public List<Map<String, Object>> selectBProjectListJoinPrevent(Long id);

    @Select("select count(*) from b_project  where dept_id=#{deptId}")
    int selectNumberBydeprtId(Long deptId);

    @Select("\n" +
            "SELECT COUNT(DISTINCT project_id) \n" +
            "FROM b_patrol_unit_place \n" +
            "WHERE project_id IN (select id from b_project  where dept_id=#{deptId});")
    int selectProjectOfCompleteNumbers(Long deptId);
}
