package com.newland.paas.prefabsql.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.newland.paas.sbcommon.annotation.Base64Parameter;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.prefabsql.db.dbutils.handlers.MapListHandler;
import com.newland.paas.prefabsql.service.PrefabSqlService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.common.PaasErrorCode;
import com.newland.paas.sbcommon.common.PageBean;
import com.newland.paas.sbcommon.common.PageParams;
import com.newland.paas.sbcommon.vo.Response;

@RestController
@RequestMapping("common/v1/prefab-sql")
public class PrefabSqlController {
	private static final Log log = LogFactory.getLogger(PrefabSqlController.class);

	@Autowired
	PrefabSqlService prefabSqlService;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	@Base64Parameter(value = { "p", "q" })
	public Response<?> query(String p, String q) throws ApplicationException{
		try {
			Map<String, Object> requestMap =new Gson().fromJson(q, new TypeToken<Map<String,Object>>() {}.getType()) ;
			
			requestMap = humpToLine(requestMap);
						
			String sqlId = (String)requestMap.get("sql_id");
			requestMap.remove("sql_id");
			
			if ( p != null ) {
				PageParams pageParams = new Gson().fromJson(p, PageParams.class);// 获得传过来的分页信息
				PageBean pageBean = prefabSqlService.executeQueryPage(sqlId, requestMap, pageParams); 
				return new Response<PageBean>(pageBean);
			}else {
				List<Map<String, Object>> queryResult = prefabSqlService.executeQuery(sqlId, 
						requestMap, new MapListHandler());
				return new Response<List<Map<String, Object>>>(queryResult);		
				
			}
		}catch (JsonSyntaxException e) {
			log.error("", "", e, e.getMessage());
			// 400 参数错误
			throw new ApplicationException(PaasErrorCode.ERROR);
		} catch (Exception e) {
			throw new ApplicationException(PaasErrorCode.EXCEPTION);
			// 500服务器内部错误
			//return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	public static Map<String, Object> humpToLine( Map<String, Object> requestMap) {
		Map<String,Object> newRequestMap = new HashMap<String, Object>();
		for ( Entry<String, Object> entry : requestMap.entrySet() ) {
			String first = entry.getKey();
			newRequestMap.put(StringUtils.humpToLine(first), entry.getValue());
		}		
		return newRequestMap ; 
	}
}
