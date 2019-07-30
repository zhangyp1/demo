/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;

/**
 * 描述
 * @author linkun
 * @created 2018年6月26日 下午3:31:02
 */
public class MenuBO {
    private static final Log logger = LogFactory.getLogger(MenuBO.class);
    
    /**
     * 菜单id
     */
    private Long menuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 图标
     */
    private String icon;
    /**
     * 菜单url
     */
    private String url;
    /**
     * 父菜单id
     */
    private Long parentId;
    /**
     * 排序号
     */
    private String orderNumber;
    /**
     * 隐藏标志  false不隐藏，true隐藏
     */
    private String hidden;
    private Long menuType;
    
    private String addition;
    /**
     * @author linkun
     * @created 2018年6月26日 下午3:00:00
     * @return 
     */
    public Long getMenuId() {
        return menuId;
    }
    /**
     * @author linkun
     * @created 2018年6月26日 下午3:00:00
     * @param menuId
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
    /**
     * @author linkun
     * @created 2018年6月26日 下午3:00:00
     * @return 
     */
    public String getMenuName() {
        return menuName;
    }
    /**
     * @author linkun
     * @created 2018年6月26日 下午3:00:00
     * @param menuName
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    /**
     * @author linkun
     * @created 2018年6月26日 下午3:00:00
     * @return 
     */
    public String getUrl() {
        return url;
    }
    /**
     * @author linkun
     * @created 2018年6月26日 下午3:00:00
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @author linkun
     * @created 2018年6月26日 下午3:00:00
     * @return 
     */
    public Long getParentId() {
        return parentId;
    }
    /**
     * @author linkun
     * @created 2018年6月26日 下午3:00:00
     * @param parentId
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:28:47
     * @return 
     */
    public String getIcon() {
        return icon;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:28:47
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }
    /**
     * @author linkun
     * @created 2018年7月2日 下午2:01:00
     * @return 
     */
    public String getOrderNumber() {
        return orderNumber;
    }
    /**
     * @author linkun
     * @created 2018年7月2日 下午2:01:00
     * @param orderNumber
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    /**
     * @author linkun
     * @created 2018年7月9日 下午6:32:58
     * @return 
     */
    public String getHidden() {
        return hidden;
    }
    /**
     * @author linkun
     * @created 2018年7月9日 下午6:32:58
     * @param hidden
     */
    public void setHidden(String hidden) {
        this.hidden = hidden;
    }
	public Long getMenuType() {
		return menuType;
	}
	public void setMenuType(Long menuType) {
		this.menuType = menuType;
	}
	public String getAddition() {
		return addition;
	}
	public void setAddition(String addition) {
		this.addition = addition;
	}
	
    
}

