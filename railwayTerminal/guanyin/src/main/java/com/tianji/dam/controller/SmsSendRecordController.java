package com.tianji.dam.controller;

import com.tianji.dam.domain.SmsSendRecord;
import com.tianji.dam.service.ISmsSendRecordService;
import com.tj.common.annotation.Log;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.page.TableDataInfo;
import com.tj.common.enums.BusinessType;
import com.tj.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 短信发送记录Controller
 * 
 * @author ly
 * @date 2023-12-11
 */
@RestController
@RequestMapping("/dam/smsrecord")
public class SmsSendRecordController extends BaseController
{
    @Autowired
    private ISmsSendRecordService smsSendRecordService;

    /**
     * 查询短信发送记录列表
     */

    @GetMapping("/list")
    public TableDataInfo list(SmsSendRecord smsSendRecord)
    {
        startPage();
        List<SmsSendRecord> list = smsSendRecordService.selectSmsSendRecordList(smsSendRecord);
        return getDataTable(list);
    }

    /**
     * 导出短信发送记录列表
     */

    @Log(title = "短信发送记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SmsSendRecord smsSendRecord)
    {
        List<SmsSendRecord> list = smsSendRecordService.selectSmsSendRecordList(smsSendRecord);
        ExcelUtil<SmsSendRecord> util = new ExcelUtil<SmsSendRecord>(SmsSendRecord.class);
        util.exportExcel(response, list, "短信发送记录数据");
    }

    /**
     * 获取短信发送记录详细信息
     */

    @GetMapping(value = "/{gid}")
    public AjaxResult getInfo(@PathVariable("gid") Long gid)
    {
        return AjaxResult.success(smsSendRecordService.selectSmsSendRecordByGid(gid));
    }

    /**
     * 新增短信发送记录
     */

    @Log(title = "短信发送记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SmsSendRecord smsSendRecord)
    {
        return toAjax(smsSendRecordService.insertSmsSendRecord(smsSendRecord));
    }

    /**
     * 修改短信发送记录
     */

    @Log(title = "短信发送记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SmsSendRecord smsSendRecord)
    {
        return toAjax(smsSendRecordService.updateSmsSendRecord(smsSendRecord));
    }

    /**
     * 删除短信发送记录
     */

    @Log(title = "短信发送记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{gids}")
    public AjaxResult remove(@PathVariable Long[] gids)
    {
        return toAjax(smsSendRecordService.deleteSmsSendRecordByGids(gids));
    }
}
