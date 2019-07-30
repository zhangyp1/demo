package com.newland.paas.prefabsql.db.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.newland.paas.prefabsql.db.dbutils.RowProcessor;


/**
 * @author Administrator
 *
 */
public class MapListForMetaHandler extends MapListHandler {
	private Map<String,Integer> colTypes;
	
	public MapListForMetaHandler() {
		super();
	}
	
	public MapListForMetaHandler(RowProcessor convert) {
		super(convert);
	}
	
	public Map<String,Integer> getColTypeMap() {
		return colTypes;
	}
	
	@Override
    protected Map<String, Object> handleRow(ResultSet rs) throws SQLException {
		if(colTypes==null) {
			colTypes = new HashMap<String,Integer>();
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();

	        for (int i = 1; i <= cols; i++) {
	            String columnName = rsmd.getColumnLabel(i);
	            if (null == columnName || 0 == columnName.length()) {
	              columnName = rsmd.getColumnName(i);
	            }
	            colTypes.put(columnName, rsmd.getColumnType(i));
	        }
	        
		}
        return super.handleRow(rs);
    }
}
