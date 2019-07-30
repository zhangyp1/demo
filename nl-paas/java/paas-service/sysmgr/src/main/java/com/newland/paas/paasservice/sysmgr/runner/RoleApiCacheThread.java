package com.newland.paas.paasservice.sysmgr.runner;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuOperRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuUrlMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOperRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysMenuUrlBO;
import com.newland.paas.sbcommon.constants.RedisKeyConstants;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色API缓存刷新线程
 *
 * @author zhongqingjiang
 */
public class RoleApiCacheThread implements Runnable {

    private static final Log LOG = LogFactory.getLogger(RoleApiCacheThread.class);

    public RoleApiCacheThread(Long roleId, RedisTemplate<String, String> redisTemplate,
                              SysMenuOperRoleMapper sysMenuOperRoleMapper, SysMenuUrlMapper sysMenuUrlMapper) {
        this.roleId = roleId;
        this.redisTemplate = redisTemplate;
        this.sysMenuOperRoleMapper = sysMenuOperRoleMapper;
        this.sysMenuUrlMapper = sysMenuUrlMapper;
    }

    private Long roleId;
    private RedisTemplate<String, String> redisTemplate;
    private SysMenuOperRoleMapper sysMenuOperRoleMapper;
    private SysMenuUrlMapper sysMenuUrlMapper;

    @Override
    public void run() {
        try {
            LOG.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("开始刷新角色API缓存，角色ID:{0}", roleId));
            redisTemplate.delete(RedisKeyConstants.REDIS_PAAS_API_AUTH_PREFIX + roleId);
            List<SysMenuOperRole> menuOperRoles = sysMenuOperRoleMapper.selectByRoleId(roleId);
            List<Long> menuOperIds = menuOperRoles.stream().map(SysMenuOperRole::getMenuOperId).collect(Collectors.toList());

            if (menuOperIds != null && menuOperIds.size() > 0) {
                List<SysMenuUrlBO> sysMenuUrlBOS = sysMenuUrlMapper.selectSysMenuUrlBOByMenuIds(menuOperIds);
                Set<ZSetOperations.TypedTuple<String>> apiTuples = new HashSet<>();
                sysMenuUrlBOS.forEach(v -> {
                    String api = v.getMethod() + ":" + v.getUri();
                    ZSetOperations.TypedTuple<String> tuple = new DefaultTypedTuple<>(api, (double)api.hashCode());
                    apiTuples.add(tuple);
                });
                redisTemplate.opsForZSet().add(RedisKeyConstants.REDIS_PAAS_API_AUTH_ALL, apiTuples);
                redisTemplate.opsForZSet().add(RedisKeyConstants.REDIS_PAAS_API_AUTH_PREFIX + roleId, apiTuples);
            }
        } catch (Exception e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, "", e, MessageFormat.format("刷新角色API缓存异常，角色ID:{0}", roleId));
        }
        finally {
            LOG.info(LogProperty.LOGTYPE_DETAIL, MessageFormat.format("刷新角色API缓存结束，角色ID:{0}", roleId));
        }
    }
}
