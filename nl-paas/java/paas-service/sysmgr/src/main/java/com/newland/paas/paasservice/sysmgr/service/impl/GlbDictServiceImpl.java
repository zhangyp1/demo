package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.*;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.ex.BaseTreeDo;
import com.newland.paas.paasservice.sysmgr.error.SysDictError;
import com.newland.paas.paasservice.sysmgr.service.GlbDictService;
import com.newland.paas.paasservice.sysmgr.vo.DictNode;
import com.newland.paas.paasservice.sysmgr.vo.DictTreeNode;
import com.newland.paas.sbcommon.common.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 字典管理
 *
 * @author wrp
 * @since 2018/6/26
 */
@Service
public class GlbDictServiceImpl implements GlbDictService {

    @Autowired
    private GlbDictMapper glbDictMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Autowired
    private SysGroupMapper sysGroupMapper;
    @Autowired
    private SysCategoryMapper sysCategoryMapper;

    private static final short DICTLEVEL = 2;
    /**
     * 查询所有字典、用户PF_USER、租户PF_TENANT、工组PF_GROUP、系统分组PF_SYS_CATEGORY
     *
     * @return
     */
    @Override
    public List<GlbDict> findAll() {
        // 字典
        List<GlbDict> glbDicts = glbDictMapper.findBaseAll();
        // 用户PF_USER
        GlbDict userDict = new GlbDict("PF_USER", "用户", null);
        glbDicts.add(userDict);
        List<SysUser> sysUsers = sysUserMapper.findBaseAll();
        glbDicts.addAll(sysUsers.stream().map(v -> new GlbDict(String.valueOf(v.getUserId()),
                v.getUsername(), userDict.getDictCode())).collect(Collectors.toList()));
        // 租户PF_TENANT
        GlbDict tenantDict = new GlbDict("PF_TENANT", "租户", null);
        glbDicts.add(tenantDict);
        List<SysTenant> sysTenants = sysTenantMapper.findBaseAll();
        glbDicts.addAll(sysTenants.stream().map(v -> new GlbDict(String.valueOf(v.getId()),
                v.getTenantName(), tenantDict.getDictCode())).collect(Collectors.toList()));

        // 工组PF_GROUP
        GlbDict groupDict = new GlbDict("PF_GROUP", "工组", null);
        glbDicts.add(groupDict);
        List<SysGroup> sysGroups = sysGroupMapper.findBaseAll();
        glbDicts.addAll(sysGroups.stream().map(v -> new GlbDict(String.valueOf(v.getGroupId()),
                v.getGroupName(), groupDict.getDictCode())).collect(Collectors.toList()));

        // 系统分组PF_SYS_CATEGORY
        GlbDict categoryDict = new GlbDict("PF_SYS_CATEGORY", "系统分组", null);
        glbDicts.add(categoryDict);
        List<SysCategory> sysCategorys = sysCategoryMapper.findBaseAll();
        glbDicts.addAll(sysCategorys.stream().map(v ->
                    new GlbDict(String.valueOf(v.getSysCategoryId()), v.getSysCategoryName(),
                            categoryDict.getDictCode())).collect(Collectors.toList()));

        return glbDicts;
    }

    /**
     * 查询字典列表
     *
     * @return
     */
    @Override
    public List<GlbDict> getAll() {
        // 字典
        return glbDictMapper.findBaseAll();
    }

    /**
     * 查询字典列表
     *
     * @return
     */
    @Override
    public List<DictNode> getAllNode() {
        // 字典
        GlbDict query = new GlbDict();
        query.setDictLevel(DICTLEVEL);
        List<GlbDict> glbDicts = glbDictMapper.selectBySelective(query);
        List<DictNode> list = new ArrayList<>();
        for (GlbDict glbDict : glbDicts) {
            DictNode dictNode = new DictNode(glbDict);
            list.add(dictNode);
        }
        return list;
    }

    /**
     * 删除字典通过dict_code
     *
     * @return
     * @throws ApplicationException
     */
    @Override
    public int delByDictCodeAndPcode(String dictCode, String pcode) throws ApplicationException {
        GlbDict pquery = new GlbDict();
        pquery.setDictPcode(dictCode);
        int p = glbDictMapper.countBySelective(pquery);
        if (p > 0) {
            throw new ApplicationException(SysDictError.CHILDREN_EXIST_ERROR);
        }
        // 字典
        GlbDict glbDict = new GlbDict();
        glbDict.setDictCode(dictCode);
        glbDict.setDictPcode(pcode);
        List<GlbDict> glbDicts = glbDictMapper.selectBySelective(glbDict);
        if (!glbDicts.isEmpty()) {
            for (GlbDict gt : glbDicts) {
                if (gt.getDictLevel() != 1) {
                    glbDictMapper.deleteBySelective(gt);
                }
            }
            return glbDicts.size();
        } else {
            return 0;
        }

    }

    /**
     * 插入字典
     *
     * @return
     */
    @Override
    public int insertDict(GlbDict glbDict) {
        // 字典
        return glbDictMapper.insert(glbDict);

    }

    /**
     * 获取字典通过dict_code
     *
     * @return
     */
    @Override
    public List<GlbDict> getByDictCode(String dictCode) {
        // 字典
        GlbDict glbDict = new GlbDict();
        glbDict.setDictCode(dictCode);
        return glbDictMapper.selectBySelective(glbDict);

    }

    /**
     * 根据父类编码查询字典数据
     *
     * @param dictPcode
     * @return
     */
    @Override
    public List<GlbDict> getByDictPcode(String dictPcode) {
        GlbDict glbDict = new GlbDict();
        glbDict.setDictPcode(dictPcode);
        return glbDictMapper.selectBySelective(glbDict);
    }

    /**
     * 查询字典树
     *
     * @return
     */
    @Override
    public DictTreeNode<BaseTreeDo> getAllTree() {

        List<GlbDict> glbDicts = glbDictMapper.findBaseAll();
        DictTreeNode.TreeBuilder<BaseTreeDo> treeBuilder = new DictTreeNode.TreeBuilder<>();
        for (GlbDict glbDict : glbDicts) {
            BaseTreeDo baseTreeDo = new BaseTreeDo();
            baseTreeDo.setId(glbDict.getDictCode());
            baseTreeDo.setName(glbDict.getDictName());
            baseTreeDo.setParentId(glbDict.getDictPcode());
            baseTreeDo.setLevel(glbDict.getDictLevel() == null ? 1 : glbDict.getDictLevel().intValue());
            treeBuilder.addNode(baseTreeDo.getParentId(), baseTreeDo.getId(), baseTreeDo);
        }
        return treeBuilder.build();
    }

    @Override
    public int insertOrUpdate(GlbDict glbDict, String oldCode, String oldPcode) throws ApplicationException {
        GlbDict temp = new GlbDict();
        //判断是否是编辑
        if (oldCode != null && !"".equals(oldCode) && oldPcode != null && !"".equals(oldPcode)) {
            temp.setDictCode(oldCode);
            temp.setDictPcode(oldPcode);
        } else {
            temp.setDictCode(glbDict.getDictCode());
        }
        List<GlbDict> glbDicts = glbDictMapper.selectBySelective(temp);
        int i = glbDicts.size();
        if (i > 0) {
            //判断是否是编辑，不是编辑则抛出错误
            if (oldCode != null && !"".equals(oldCode) && oldPcode != null && !"".equals(oldPcode)) {
                if (!oldCode.equals(glbDict.getDictCode())) {
                    GlbDict t = new GlbDict();
                    t.setDictCode(glbDict.getDictCode());
                    List<GlbDict> tGlbDicts = glbDictMapper.selectBySelective(t);
                    //dictcode改变的情况下，如果有重复值抛错
                    if (!tGlbDicts.isEmpty()) {
                        throw new ApplicationException(SysDictError.DUPLICATE_ERROR);
                    }
                }
                if (!oldPcode.equals(glbDict.getDictPcode()) && oldCode.equals(glbDict.getDictPcode())) {
                        throw new ApplicationException(SysDictError.PCODE_ERROR);
                }
                for (GlbDict gt : glbDicts) {
                    if (gt.getDictLevel() != 1) {
                        glbDict.setDictId(gt.getDictId());
                        glbDictMapper.updateByDictId(glbDict);
                    }
                }
                return glbDicts.size();
            } else {
                throw new ApplicationException(SysDictError.DUPLICATE_ERROR);
            }
        } else {
            glbDict.setDictLevel(DICTLEVEL);
            return glbDictMapper.insertSelective(glbDict);
        }
    }

    @Override
    public DictTreeNode<BaseTreeDo> getTree(String pcode) {
        GlbDict temp = new GlbDict();
        temp.setDictPcode(pcode);
        List<GlbDict> glbDicts = glbDictMapper.selectBySelective(temp);
        DictTreeNode.TreeBuilder<BaseTreeDo> treeBuilder = new DictTreeNode.TreeBuilder<>();
        for (GlbDict glbDict : glbDicts) {
            BaseTreeDo baseTreeDo = new BaseTreeDo();
            baseTreeDo.setId(glbDict.getDictCode());
            baseTreeDo.setName(glbDict.getDictName());
            baseTreeDo.setParentId(glbDict.getDictPcode());
            baseTreeDo.setLevel(glbDict.getDictLevel() == null ? 1 : glbDict.getDictLevel().intValue());
            treeBuilder.addNode(baseTreeDo.getParentId(), baseTreeDo.getId(), baseTreeDo);
        }
        return treeBuilder.build();
    }

    /**
     * @param pCode
     * @param dictCode
     * @return
     * @throws ApplicationException
     */
    @Override
    public GlbDict getDict(String pCode, String dictCode) {
        return Objects.equals(pCode, "PF_USER") ? getGlbUserDict(pCode, dictCode)
                : Objects.equals(pCode, "PF_TENANT") ? getGlbTenantDict(pCode, dictCode)
                : Objects.equals(pCode, "PF_GROUP") ? getGlbGroupDict(pCode, dictCode)
                : Objects.equals(pCode, "PF_SYS_CATEGORY") ?   getGlbCategoryDict(pCode, dictCode)
                : getGlbDict(pCode, dictCode);
    }

    /**
     * 翻译字典
     * @param pCode
     * @param dictCode
     * @return
     */
    private GlbDict getGlbDict(String pCode, String dictCode) {
        // 字典
        GlbDict dict = null;
        GlbDict glbDict = new GlbDict();
        glbDict.setDictCode(dictCode);
        glbDict.setDictPcode(pCode);
        List<GlbDict> glbDicts = glbDictMapper.selectBySelective(glbDict);
        if (glbDicts != null  && !glbDicts.isEmpty()) {
            dict = glbDicts.get(0);
        }
        return dict;
    }

    /**
     * 翻译系统分组
     * @param pCode
     * @param dictCode
     * @return
     */
    private GlbDict getGlbCategoryDict(String pCode, String dictCode) {
        GlbDict dict = null;
        SysCategory sysCategoryReq = new SysCategory();
        sysCategoryReq.setSysCategoryId(Long.parseLong(dictCode));
        List<SysCategory> sysCategories = sysCategoryMapper.selectBySelective(sysCategoryReq);
        if (sysCategories != null  && !sysCategories.isEmpty()) {
            dict = new GlbDict();
            SysCategory sysCategory = sysCategories.get(0);
            dict.setDictPcode(pCode);
            dict.setDictCode(dictCode);
            dict.setDictName(sysCategory.getSysCategoryName());
        }
        return dict;
    }

    /**
     * 翻译工组
     * @param pCode
     * @param dictCode
     * @return
     */
    private GlbDict getGlbGroupDict(String pCode, String dictCode) {
        GlbDict dict = null;
        SysGroupReqBo sysGroupReq = new SysGroupReqBo();
        sysGroupReq.setGroupId(Long.parseLong(dictCode));
        List<SysGroup> sysGroups = sysGroupMapper.selectBySelective(sysGroupReq);
        if (sysGroups != null  && !sysGroups.isEmpty()) {
            dict = new GlbDict();
            SysGroup sysGroup = sysGroups.get(0);
            dict.setDictPcode(pCode);
            dict.setDictCode(dictCode);
            dict.setDictName(sysGroup.getGroupName());
        }
        return dict;
    }

    /**
     * 翻译租户
     * @param pCode
     * @param dictCode
     * @return
     */
    private GlbDict getGlbTenantDict(String pCode, String dictCode) {
        GlbDict dict = null;
        SysTenant sysTenantReq = new SysTenant();
        sysTenantReq.setId(Long.parseLong(dictCode));
        List<SysTenant> sysTenants = sysTenantMapper.selectBySelective(sysTenantReq);
        if (sysTenants != null  && !sysTenants.isEmpty()) {
            dict = new GlbDict();
            SysTenant sysTenant = sysTenants.get(0);
            dict.setDictPcode(pCode);
            dict.setDictCode(dictCode);
            dict.setDictName(sysTenant.getTenantName());
        }
        return dict;
    }

    /**
     * 翻译用户
     * @param pCode
     * @param dictCode
     * @return
     */
    private GlbDict getGlbUserDict(String pCode, String dictCode) {
        GlbDict dict = null;
        SysUser sysUserReq = new SysUser();
        sysUserReq.setUserId(Long.parseLong(dictCode));
        List<SysUser> sysUsers = sysUserMapper.selectBySelective(sysUserReq);
        if (sysUsers != null  && !sysUsers.isEmpty()) {
            dict = new GlbDict();
            SysUser sysUser = sysUsers.get(0);
            dict.setDictPcode(pCode);
            dict.setDictCode(dictCode);
            dict.setDictName(sysUser.getUsername());
        }
        return dict;
    }
}
