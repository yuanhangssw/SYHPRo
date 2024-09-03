package com.tianji.dam.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.Material;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.vo.DamsConstructionVo;
import com.tianji.dam.mapper.MaterialMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.service.TDamsconstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DamsConstructionServiceImpl implements TDamsconstructionService {
    @Autowired
    private TDamsconstructionMapper damsConstructionMapper;
    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public List getAll(String pid) {
        return damsConstructionMapper.getAll(pid);
    }

    @Override
    public List<DamsConstruction> getAtreeMenuByFor2() {
        //获得Pid为0的条目
        List<DamsConstruction> list = damsConstructionMapper.getAll("0");
        for (DamsConstruction item : list) {
            item.setHref("treeLevel1?id=" + item.getId());
            List<DamsConstruction> children = damsConstructionMapper.getAll(item.getId().toString());
            for (DamsConstruction next : children) {
                next.setHref("treeLevel2?id=" + next.getId());
                List<DamsConstruction> grandSon = damsConstructionMapper.getAll(next.getId().toString());
            }
            item.setChildren(children);
        }
        return list;
    }

    @Override
    public List<DamsConstruction> getAtreeMenuByFor3() {
        //获得Pid为0的条目
        List<DamsConstruction> list = damsConstructionMapper.getAll("0");
        for (DamsConstruction item : list) {
            item.setHref("treeLevel1?id=" + item.getId());
            List<DamsConstruction> children = damsConstructionMapper.getAll(item.getId().toString());
            for (DamsConstruction next : children) {
                next.setHref("treeLevel2?id=" + next.getId());
                List<DamsConstruction> grandSon = damsConstructionMapper.getAll(next.getId().toString());
                next.setChildren(grandSon);
            }
            item.setChildren(children);
        }
        return list;
    }

    @Override
    public List<DamsConstruction> getAtreeMenuByFor3Working() {
        //获得Pid为0的条目
        List<DamsConstruction> list = damsConstructionMapper.getAll("0");
        for (DamsConstruction item : list) {
            item.setHref("treeLevel1?id=" + item.getId());
            List<DamsConstruction> children = damsConstructionMapper.getAll(item.getId().toString());
            for (DamsConstruction next : children) {
                next.setHref("treeLevel2?id=" + next.getId());
                List<DamsConstruction> grandSon = damsConstructionMapper.getAllWorking(next.getId().toString());
                next.setChildren(grandSon);
            }
            item.setChildren(children);
        }
        return list;
    }

    @Override
    public List<DamsConstruction> getAtreeMenuByFor3Closed() {
        //获得Pid为0的条目
        List<DamsConstruction> list = damsConstructionMapper.getAll("0");
        for (DamsConstruction item : list) {
            item.setHref("treeLevel1?id=" + item.getId());
            List<DamsConstruction> children = damsConstructionMapper.getAll(item.getId().toString());
            for (DamsConstruction next : children) {
                next.setHref("treeLevel2?id=" + next.getId());
                List<DamsConstruction> grandSon = damsConstructionMapper.getAllClosed(next.getId().toString());
                next.setChildren(grandSon);
            }
            item.setChildren(children);
        }
        return list;
    }

    @Override
    public int add(DamsConstruction damsConstruction) {
        int i = damsConstructionMapper.insert(damsConstruction);
        return damsConstruction.getId();
    }

    @Override
    public DamsConstruction findById(Integer id) {
        return damsConstructionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateDC(DamsConstruction damsConstruction) {
        return damsConstructionMapper.updateByPrimaryKey(damsConstruction);
    }

    @Override
    public int deleteDC(Integer id) {
        return damsConstructionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<DamsConstruction> findAtreeMenuThirdHasData() {
        return damsConstructionMapper.getAllHasData();
    }

    @Override
    public DamsConstruction selectByPid(String pid) {
        return damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(pid));
    }

    @Override
    public DamsConstruction findByTablename(String str) {
        return damsConstructionMapper.findByTablename(str);
    }

    @Override
    public List<DamsConstruction> findRange() {
        return damsConstructionMapper.findRange();
    }

    @Override
    public List<DamsConstruction> findByStatus(int i) {
        return damsConstructionMapper.findByStatus(i);
    }

    @Override
    public List<DamsConstruction> findCengByMaterialname(String materialname) {
        return damsConstructionMapper.findCengByMaterialname(materialname);
    }

    @Override
    public List<DamsConstruction> findAtreeMenuThirdHasDataByMaterialnameAndHeightIndex(String materialname, Integer heightIndex) {
        return damsConstructionMapper.findAtreeMenuThirdHasDataByMaterialnameAndHeightIndex(materialname, heightIndex);
    }

    @Override
    public List<String> findCangToday(String t_1, long yest, long tod) {
        return damsConstructionMapper.findCangToday(t_1, yest, tod);
    }

    @Override
    public DamsConstruction selectByTablename(String toString) {
        return damsConstructionMapper.selectByTablename(toString);
    }

    @Override
    public Set findWorkingCengOfTwoDay(String tod, String yesterday, String daybeforeyesterday) {
        Set set = new HashSet();
        for (int i = 1; i < 4; i++) {
            String tablename1 = "t_" + i + "_" + tod;
            String tablename2 = "t_" + i + "_" + yesterday;
            String tablename3 = "t_" + i + "_" + daybeforeyesterday;
            List<RollingData> cengList = damsConstructionMapper.findWorkingCeng(tablename1, tablename2, tablename3);
            for (RollingData rollingData : cengList) {
                set.add(rollingData);
            }
        }
        return set;
    }

    /*
     * 系统“结果分析”页面中的查询所有已闭仓树菜单接口
     * */
    @Override
    public List findAtreeMenuClosed() {
        /*1.查询所有的材料*/
        List<Material> materialList = materialMapper.findAllMaterial();
        List<JSONObject> result = new LinkedList<>();
        for (Material material : materialList) {
            /*2.查询每种材料对应的已关闭的工作仓*/
            List<DamsConstruction> damsConstructionsClosed = damsConstructionMapper.getAllClosedByMaterial(String.valueOf(material.getMid()));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", material.getMaterialname());
            jsonObject.put("id", 1);
            jsonObject.put("children", damsConstructionsClosed);
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public List findAtreeMenuClosedTod() {

        List<JSONObject> result = new LinkedList<>();
        /*2.查询每种材料对应的已关闭的工作仓*/
        List<DamsConstruction> damsConstructionsClosed = damsConstructionMapper.getAllByTod();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "区域-层号-材料");
        jsonObject.put("id", 1);
        jsonObject.put("children", damsConstructionsClosed);
        result.add(jsonObject);
        return result;
    }


    /*
     * 系统“实时分析”页面中的查询所有已开仓树菜单接口
     * */
    @Override
    public List findAtreeMenuOpened() {
        /*1.查询所有的材料*/
        List<Material> materialList = materialMapper.findAllMaterial();
        List<JSONObject> result = new LinkedList<>();
        for (Material material : materialList) {
            /*2.查询每种材料对应的已关闭的工作仓*/
            List<DamsConstruction> damsConstructionsOpened = damsConstructionMapper.getAllOpenedByMaterial(String.valueOf(material.getId()));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", material.getMaterialname());
            jsonObject.put("id", 1);
            jsonObject.put("children", damsConstructionsOpened);
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public List findAtreeMenu() {
        /*1.查询所有的材料*/
        List<Material> materialList = materialMapper.findAllMaterial();
        List<JSONObject> result = new LinkedList<>();
        for (Material material : materialList) {
            /*2.查询每种材料对应的已关闭的工作仓*/
            List<DamsConstruction> damsConstructions = damsConstructionMapper.getAllByMaterial(String.valueOf(material.getId()));
            /*3.查询dams表中每个材料的id*/
            DamsConstruction dam = damsConstructionMapper.findMaterialByMaterialId(material.getId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", material.getMaterialname());
            jsonObject.put("id", dam.getId());
            jsonObject.put("children", damsConstructions);
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public List<DamsConstruction> getAllByPage(String id, int begin, int limit, int status) {
        return damsConstructionMapper.getAllByPage(id, begin, limit, status);
    }

    @Override
    public List findAtreeMenuOpenedForAndriod() {
        return damsConstructionMapper.findAtreeMenuOpenedForAndriod();
    }

    @Override
    public List historyMaterialTree() {
        /*1.查询所有的材料*/
        List<DamsConstruction> materialList = damsConstructionMapper.getAllByPage("0", 0, 100, null);
        List<JSONObject> result = new LinkedList<>();

        //damsConstructionMapper.getAllByPage("", 0, 10);

        for (DamsConstruction material : materialList) {

            List<DamsConstruction> damsConstructions = damsConstructionMapper.getAllByPage(material.getId() + "", 0, 1000, null);
            for (DamsConstruction damsConstruction : damsConstructions) {
                //damsConstruction.setTitle(damsConstruction.getTitle()+damsConstruction.getEngcode());
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", material.getTitle());

            jsonObject.put("id", material.getId());
            jsonObject.put("children", damsConstructions);
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public List historyMaterialTreeRepair() {
        /*1.查询所有的材料*/
        List<DamsConstruction> materialList = damsConstructionMapper.getAllByPage("0", 0, 100, null);
        List<JSONObject> result = new LinkedList<>();

        //damsConstructionMapper.getAllByPage("", 0, 10);

        for (DamsConstruction material : materialList) {
            List<DamsConstruction> allchild = new LinkedList<>();



            List<DamsConstruction> damsConstructions = damsConstructionMapper.getAllByPage(material.getId() + "", 0, 1000, null);
            for (DamsConstruction damsConstruction : damsConstructions) {
                if (damsConstruction.getStatus() != 8) {
                    continue;
                }
                allchild.add(damsConstruction);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", material.getTitle());

            jsonObject.put("id", material.getId());
            jsonObject.put("children", allchild);
            result.add(jsonObject);


        }
        return result;
    }

    @Override
    public List historyMaterialTreeLevel() {
        /*1.查询所有的材料*/
        List<DamsConstruction> materialList = damsConstructionMapper.getAllByPage("0", 0, 100, null);
        List<JSONObject> result = new LinkedList<>();

        //damsConstructionMapper.getAllByPage("", 0, 10);

        for (DamsConstruction material : materialList) {


            //获取这一个分区的所有层
            List<Integer> alllevel =    damsConstructionMapper.selectarealevel(material.getId());
            List<DamsConstruction> allchildlevel = new ArrayList<>();

            for (Integer i : alllevel) {
                DamsConstruction  d = new DamsConstruction();
                  d.setTitle("第"+i+"层");
                  d.setEngcode(i+"");
                  d.setId(i);
                  d.setPid(material.getId());
                allchildlevel.add(d);
            }

            for (DamsConstruction damsConstruction : allchildlevel) {
                List<DamsConstruction> allchild = new LinkedList<>();
                List<DamsConstruction> damsConstructions = damsConstructionMapper.getAllByPage(damsConstruction.getPid() + "", 0, 1000, null);
                for (DamsConstruction damsConstruction2 : damsConstructions) {
                    if (damsConstruction2.getEngcode()==null ||!damsConstruction2.getEngcode().equals(damsConstruction.getEngcode())  ) {
                        continue;
                    }
                    allchild.add(damsConstruction2);
                }
                damsConstruction.setChildren(allchild);
            }


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", material.getTitle());
            jsonObject.put("id", material.getId());
            jsonObject.put("children", allchildlevel);
            result.add(jsonObject);


        }
        return result;
    }



    @Override
    public DamsConstruction getDamsMax() {
        return damsConstructionMapper.getDamsMax();
    }


    @Override
    public int addSelective(DamsConstruction damsConstruction) {
        int i = damsConstructionMapper.insertSelective(damsConstruction);
        return damsConstruction.getId();
    }

    @Override
    public List getAllVo(DamsConstructionVo damsConstructionVo) {
        return damsConstructionMapper.getAllVo(damsConstructionVo);
    }

    @Override
    public List<DamsConstruction> getAllByPageVo(DamsConstructionVo damsConstructionVo) {
        return damsConstructionMapper.getAllByPageVo(damsConstructionVo);
    }

    @Override
    public List<DamsConstruction> selectChildById(Long id, int start, int end) {
        return damsConstructionMapper.selectChildById(id, start, end);
    }

    @Override
    public String selectStore(Long id) {
        return damsConstructionMapper.selectStore(id);
    }

    @Override
    public int selectCountNumber(Long id) {
        return damsConstructionMapper.selectCountNumber(id);
    }



    @Override
    public int updateRanges(Long id, String ranges) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("ranges", ranges);

        return damsConstructionMapper.updateRanges(map);
    }

}
