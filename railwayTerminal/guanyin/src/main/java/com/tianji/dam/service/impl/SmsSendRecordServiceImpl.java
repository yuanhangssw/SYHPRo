package com.tianji.dam.service.impl;

import java.util.List;
import com.tj.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tianji.dam.mapper.SmsSendRecordMapper;
import com.tianji.dam.domain.SmsSendRecord;
import com.tianji.dam.service.ISmsSendRecordService;

/**
 * 短信发送记录Service业务层处理
 * 
 * @author ly
 * @date 2023-12-11
 */
@Service
public class SmsSendRecordServiceImpl implements ISmsSendRecordService 
{
    @Autowired
    private SmsSendRecordMapper smsSendRecordMapper;

    /**
     * 查询短信发送记录
     * 
     * @param gid 短信发送记录主键
     * @return 短信发送记录
     */
    @Override
    public SmsSendRecord selectSmsSendRecordByGid(Long gid)
    {
        return smsSendRecordMapper.selectSmsSendRecordByGid(gid);
    }

    /**
     * 查询短信发送记录列表
     * 
     * @param smsSendRecord 短信发送记录
     * @return 短信发送记录
     */
    @Override
    public List<SmsSendRecord> selectSmsSendRecordList(SmsSendRecord smsSendRecord)
    {
        return smsSendRecordMapper.selectSmsSendRecordList(smsSendRecord);
    }

    /**
     * 新增短信发送记录
     * 
     * @param smsSendRecord 短信发送记录
     * @return 结果
     */
    @Override
    public int insertSmsSendRecord(SmsSendRecord smsSendRecord)
    {
        smsSendRecord.setCreateTime(DateUtils.getNowDate());
        return smsSendRecordMapper.insertSmsSendRecord(smsSendRecord);
    }

    /**
     * 修改短信发送记录
     * 
     * @param smsSendRecord 短信发送记录
     * @return 结果
     */
    @Override
    public int updateSmsSendRecord(SmsSendRecord smsSendRecord)
    {
        return smsSendRecordMapper.updateSmsSendRecord(smsSendRecord);
    }

    /**
     * 批量删除短信发送记录
     * 
     * @param gids 需要删除的短信发送记录主键
     * @return 结果
     */
    @Override
    public int deleteSmsSendRecordByGids(Long[] gids)
    {
        return smsSendRecordMapper.deleteSmsSendRecordByGids(gids);
    }

    /**
     * 删除短信发送记录信息
     * 
     * @param gid 短信发送记录主键
     * @return 结果
     */
    @Override
    public int deleteSmsSendRecordByGid(Long gid)
    {
        return smsSendRecordMapper.deleteSmsSendRecordByGid(gid);
    }
}
