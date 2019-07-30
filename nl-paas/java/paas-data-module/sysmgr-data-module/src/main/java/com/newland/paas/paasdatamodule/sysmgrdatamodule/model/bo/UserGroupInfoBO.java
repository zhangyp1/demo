/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;

/**
 * 工组BO
 * 描述
 * @author linkun
 * @created 2018年7月4日 上午11:06:16
 */
public class UserGroupInfoBO {
    private static final Log logger = LogFactory.getLogger(UserGroupInfoBO.class);
    
    private Long id;

    /**
     * 工组名称
     */
    private String name;
    /**
     * 工组描述
     */
    private String description;

    /**
     * @author linkun
     * @created 2018年7月4日 上午11:07:06
     * @return 
     */
    public Long getId() {
        return id;
    }

    /**
     * @author linkun
     * @created 2018年7月4日 上午11:07:06
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @author linkun
     * @created 2018年7月4日 上午11:07:06
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * @author linkun
     * @created 2018年7月4日 上午11:07:06
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @author linkun
     * @created 2018年7月4日 上午11:07:06
     * @return 
     */
    public String getDescription() {
        return description;
    }

    /**
     * @author linkun
     * @created 2018年7月4日 上午11:07:06
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    

}

