package com.tj.web.controller.dam;
import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.mapper.TableMapper;
import com.tianji.dam.service.TDamsconstructionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@CrossOrigin
@RequestMapping("/AtreeMenu")
@Controller("AtreeMenuController")
public class AtreeMenuController {
    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private TDamsconstructionService damsConstructionService;

    /*
   * 给平板正在工作的工作仓
   * */
    @RequestMapping(value="/findAtreeMenuOpenedForAndriod",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject findAtreeMenuOpenedForAndriod(HttpServletRequest request) {
        List dams = damsConstructionService.findAtreeMenuOpenedForAndriod();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resultcode","0");
        jsonObject.put("data",dams);
        return jsonObject;
    }

    /*
     * 系统“结果分析-日工作情况”页面中的查询所有已闭仓树菜单接口
     * */
    @RequestMapping(value="/findAtreeMenuClosedTod",method= RequestMethod.GET)
    @ResponseBody
    public List findAtreeMenuClosedTod(HttpServletRequest request) {
        return damsConstructionService.findAtreeMenuClosedTod();
    }

    /*
    * 系统“结果分析-空间分析”页面中的查询所有已闭仓树菜单接口
    * */
    @RequestMapping(value="/findAtreeMenuClosed",method= RequestMethod.GET)
    @ResponseBody
    public List findAtreeMenuClosed(HttpServletRequest request) {
        return damsConstructionService.findAtreeMenuClosed();
    }

    /*
   * 系统“实时分析”页面中的查询所有已开仓树菜单接口
   * */
    @RequestMapping(value="/findAtreeMenuOpened",method= RequestMethod.GET)
    @ResponseBody
    public List findAtreeMenuOpened(HttpServletRequest request) {
        return damsConstructionService.findAtreeMenuOpened();
    }

    /*
   * 系统“工作仓”页面中的查询所有工作仓树菜单接口
   * */
    @RequestMapping(value="/findAtreeMenu",method= RequestMethod.GET)
    @ResponseBody
    public List findAtreeMenu(HttpServletRequest request) {
        return damsConstructionService.findAtreeMenu();
    }

    @RequestMapping(value="/findAtreeMenuThird",method= RequestMethod.GET)
    @ResponseBody
    public List findAtreeMenuThird(HttpServletRequest request) {
        List list2=damsConstructionService.getAtreeMenuByFor3();
        return list2;
    }

    @RequestMapping(value="/findWorkingCengHistory",method= RequestMethod.GET)
    @ResponseBody
    public List findWorkingCengHistory(HttpServletRequest request) {
        List<HashMap> result = new LinkedList<>();
        for(int j =1;j<7;j++){
            /*某个材料分共有哪几层*/
            String materialname = String.valueOf(j);
            HashMap<String,Object> item = new HashMap<>();
            List<DamsConstruction> list1=damsConstructionService.findCengByMaterialname(materialname);
            List<DamsConstruction> last = new LinkedList<>();
            for(int i=0;i<list1.size();i++){
                DamsConstruction damsConstruction = list1.get(i);
                damsConstruction.setSpread(false);
                damsConstruction.setIscang(0);
                last.add(damsConstruction);
            }
            item.put("title",getMaterialname(materialname));
            item.put("children",last);
            result.add(item);
        }
        return result;
    }

    @RequestMapping(value="/findAtreeMenuThirdWorking",method= RequestMethod.GET)
    @ResponseBody
    public List findAtreeMenuThirdWorking(HttpServletRequest request) {
        List list2=damsConstructionService.getAtreeMenuByFor3Working();
        return list2;
    }
    @RequestMapping(value="/findAtreeMenuThirdClosed",method= RequestMethod.GET)
    @ResponseBody
    public List findAtreeMenuThirdClosed(HttpServletRequest request) {
        List list2=damsConstructionService.getAtreeMenuByFor3Closed();
        return list2;
    }

    @RequestMapping(value="/findAtreeMenuThirdHasData",method= RequestMethod.GET)
    @ResponseBody
    public List findAtreeMenuThirdHasData(HttpServletRequest request) {
        List<DamsConstruction> list2=damsConstructionService.findAtreeMenuThirdHasData();
        int sum=0;
        for(int i=0;i<list2.size();i++){
            DamsConstruction father = damsConstructionService.selectByPid(list2.get(i).getPid().toString());
            DamsConstruction grandFather = damsConstructionService.selectByPid(father.getPid().toString());
            int count =tableMapper.getCount(list2.get(i).getTablename());
            list2.get(i).setTitle(list2.get(i).getTablename()+"(数据数"+count+")");
            sum=sum+count;
        }
        return list2;
    }

    @RequestMapping(value="/findAtreeMenuThirdHasDataByMaterialname/{materialname}",method= RequestMethod.GET)
    @ResponseBody
    public List findAtreeMenuThirdHasDataByMaterialname(HttpServletRequest request, @PathVariable String materialname) {
        /*某个材料分共有哪几层*/
        List<DamsConstruction> list1=damsConstructionService.findCengByMaterialname(materialname);
        List<DamsConstruction> last = new LinkedList<>();
        for(int i=0;i<list1.size();i++){
            DamsConstruction damsConstruction = list1.get(i);
            List<DamsConstruction> list3=damsConstructionService.findAtreeMenuThirdHasDataByMaterialnameAndHeightIndex(materialname,list1.get(i).getHeightIndex());
            List<DamsConstruction>  list2 = new LinkedList<>();
            for(DamsConstruction dams :list3){
                String tablename = dams.getTablename();
                String title = "区域"+tablename.split("_")[0]+"_"+tablename.split("_")[1];
                dams.setTitle(title);
                list2.add(dams);

            }
            damsConstruction.setSpread(false);
            damsConstruction.setIscang(0);
            damsConstruction.setChildren(list2);
            last.add(damsConstruction);
        }
        return last;
    }

    @RequestMapping(value="/findAtreeMenuThirdHasDataCeng",method= RequestMethod.GET)
    @ResponseBody
    public List findAtreeMenuThirdHasDataCeng(HttpServletRequest request) {
        return damsConstructionService.findByStatus(6);

    }

    @RequestMapping(value="/findSelect",method= RequestMethod.GET)
    @ResponseBody
    public List findSelect(HttpServletRequest request) {
        List<DamsConstruction> list2=damsConstructionService.findRange();
            /*只获得两天的数据*/
            /*计算昨天凌晨0:0:0的时间戳*/
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)-1,0,0,0);
        long yest = calendar1.getTime().getTime();
        System.out.println(yest);

            /*计算今天59:59:59的时间戳*/
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(calendar2.get(Calendar.YEAR),calendar2.get(Calendar.MONTH),calendar2.get(Calendar.DAY_OF_MONTH),23,59,59);
        long tod = calendar2.getTime().getTime();
        System.out.println(tod);
        for(int i=0;i<list2.size();i++){
            int count =tableMapper.getCountByTimeStamp(list2.get(i).getTablename(),yest,tod);
            list2.get(i).setTitle(list2.get(i).getTablename()+"(近两日数据数"+count+")");
        }
        return list2;
    }

    @RequestMapping(value="/findMaterialList/{damsId}",method= RequestMethod.GET)
    @ResponseBody
    public List findMaterialList(HttpServletRequest request,@PathVariable String damsId) {
        DamsConstruction damsConstruction = damsConstructionService.findById(Integer.valueOf(damsId));
        List<Integer> matList = tableMapper.findMaterialList(damsConstruction.getTablename());
        List<HashMap<String,String>> result = new LinkedList<>();
        for(Integer i:matList){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("value", String.valueOf(i));
            hashMap.put("mat", String.valueOf(i));
            result.add(hashMap);
        }
        return result;
    }

    public String getCreateFileTime(String fileName){
        try {
            String cmd = "cmd /C dir C:\\myProject\\springbootnettyserver\\src\\main\\resources\\static\\img\\"+fileName+".png ";
            Process p = Runtime.getRuntime().exec(cmd);
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String result;
            String getTime = null;
            while ((result = br.readLine()) != null) {
                String[] str = result.split(" ");
                for (int i = str.length - 1; i >= 0; i--) {
                    if (str[i].equals(fileName+".png")) {
                        getTime = str[0] + " " + str[2];
                    }
                }
            }
            System.out.println("modifyInfoLoade.log 文件的创建日期是：" + getTime);
            return getTime;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getMaterialname(String mat){
        if(mat.equalsIgnoreCase("1")){
            return "特殊垫层料";
        }
        if(mat.equalsIgnoreCase("2")){
            return "垫层料";
        }
        if(mat.equalsIgnoreCase("3")){
            return "过渡料";
        }
        if(mat.equalsIgnoreCase("4")){
            return "主堆石料";
        }
        if(mat.equalsIgnoreCase("5")){
            return "次堆石料";
        }
        if(mat.equalsIgnoreCase("6")){
            return "下游排水堆石料";
        }
        if(mat.equalsIgnoreCase("7")){
            return "爆破石料";
        }
        return "";
    }

    /**
     * 历史 左侧树结构数据
     * @param request
     * @return
     */
    @RequestMapping(value="/historyMaterialTree",method= RequestMethod.GET)
    @ResponseBody
    public List historyMaterialTree(HttpServletRequest request) {
        return damsConstructionService.historyMaterialTreeLevel();
    }

    /**
     * 补录左侧树加载。只加载已经开仓的仓位
     * @param request
     * @return
     */
    @RequestMapping(value="/historyMaterialTreeRepair",method= RequestMethod.GET)
    @ResponseBody
    public List historyMaterialTreeRepair(HttpServletRequest request) {
        return damsConstructionService.historyMaterialTreeRepair();
    }

    @RequestMapping(value="/historyMaterialTreeLevel",method= RequestMethod.GET)
    @ResponseBody
    public List historyMaterialTreeLevel(HttpServletRequest request) {
        return damsConstructionService.historyMaterialTreeLevel();
    }
}
