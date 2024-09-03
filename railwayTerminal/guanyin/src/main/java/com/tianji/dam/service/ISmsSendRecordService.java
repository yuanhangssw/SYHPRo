package com.tianji.dam.service;

import java.util.List;
import com.tianji.dam.domain.SmsSendRecord;

/**
 * 短信发送记录Service接口
 * 
 * @author ly
 * @date 2023-12-11
 */
public interface ISmsSendRecordService 
{
    /**
     * 查询短信发送记录
     * 
     * @param gid 短信发送记录主键
     * @return 短信发送记录
     */
    public SmsSendRecord selectSmsSendRecordByGid(Long gid);

    /**
     * 查询短信发送记录列表
     * 
     * @param smsSendRecord 短信发送记录
     * @return 短信发送记录集合
     */
    public List<SmsSendRecord> selectSmsSendRecordList(SmsSendRecord smsSendRecord);

    /**
     * 新增短信发送记录
     * 
     * @param smsSendRecord 短信发送记录
     * @return 结果
     */
    public int insertSmsSendRecord(SmsSendRecord smsSendRecord);

    /**
     * 修改短信发送记录
     * 
     * @param smsSendRecord 短信发送记录
     * @return 结果
     */
    public int updateSmsSendRecord(SmsSendRecord smsSendRecord);

    /**
     * 批量删除短信发送记录
     * 
     * @param gids 需要删除的短信发送记录主键集合
     * @return 结果
     */
    public int deleteSmsSendRecordByGids(Long[] gids);

    /**
     * 删除短信发送记录信息
     * 
     * @param gid 短信发送记录主键
     * @return 结果
     */
    public int deleteSmsSendRecordByGid(Long gid);
}
