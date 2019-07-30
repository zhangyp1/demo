package com.newland.paas.paasservice.sysmgr.runner;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuOperRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuUrlMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOperRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.sbcommon.constants.RedisKeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * 角色API缓存刷新器
 *
 * @author zhongqingjiang
 */
@Component
public class RoleApiCacheRefresher {

    public RoleApiCacheRefresher() {
        // 初始化线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("roleApiCacheRefresher-pool-%d").build();
        int corePoolSize = 10;
        int maximumPoolSize = 10;
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    private static final Log LOG = LogFactory.getLogger(RoleApiCacheRefresher.class);

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysMenuOperRoleMapper sysMenuOperRoleMapper;
    @Autowired
    private SysMenuUrlMapper sysMenuUrlMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ExecutorService threadPoolExecutor;

    /**
     * 清空角色API缓存
     */
    public void clearRoleApiCacheAll() {
        redisTemplate.delete(RedisKeyConstants.REDIS_PAAS_API_AUTH_ALL);
    }

    /**
     * 刷新角色API缓存
     */
    public void refreshAll() {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "开始初始化角色API权限缓存.....");
        this.clearRoleApiCacheAll();
        List<SysRole> roles = sysRoleMapper.selectBySelective(null);
        roles.stream().forEach(v -> this.refreshApiAuthCacheByRoleId(v.getRoleId()));
    }

    /**
     * 根据菜单操作ID刷新角色API权限缓存
     *
     * @param menuOperId
     */
    public void refreshApiAuthCacheByMenuId(Long menuOperId) {
        LOG.info(LogProperty.LOGTYPE_DETAIL, "根据菜单操作ID" + menuOperId + "刷新角色API权限缓存.....");
        SysMenuOperRole menuOperRoleParam = new SysMenuOperRole();
        menuOperRoleParam.setMenuOperId(menuOperId);
        List<SysMenuOperRole> menuOperRoles = sysMenuOperRoleMapper.selectBySelective(menuOperRoleParam);
        menuOperRoles.stream().forEach(v -> refreshApiAuthCacheByRoleId(v.getRoleId()));
    }

    /**
     * 刷新单个角色的API权限缓存
     *
     * @param roleId
     */
    public void refreshApiAuthCacheByRoleId(Long roleId) {
        threadPoolExecutor
                .execute(new RoleApiCacheThread(roleId, redisTemplate, sysMenuOperRoleMapper, sysMenuUrlMapper));
    }

}
