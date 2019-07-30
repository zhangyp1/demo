package com.newland.paas.ee.util;

import com.newland.paas.common.util.StringUtils;
import com.newland.paas.ee.vo.cluster.ZoneDetailVo;

public class ZoneDetailUtil {
	/**
	 * 检查命名空间是否有效
	 * @param clusterId 集群ID
	 * @return ZoneDetailVo 包含命名空间的Vo结构
	 */
	public static void checkZoneDetail(Long clusterId, ZoneDetailVo zoneDetailVo) throws Exception {
		if ( zoneDetailVo == null )
			throw new Exception("K8SClusterInstanceDetailInfo zone detail is null where cluster id=" + clusterId );
		if ( StringUtils.isEmpty( zoneDetailVo.getZoneName() ))
			throw new Exception("ZoneDetailVo zone name is empty where zone id=" + zoneDetailVo.getClusterZoneId());
	}
}
