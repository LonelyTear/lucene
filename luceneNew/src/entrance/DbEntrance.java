package entrance;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lucene.IndexManager;

/**
 * 从数据库创建索引入口
 * @author King
 *
 */
public class DbEntrance {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		 List<Map> list = new ArrayList<Map>();
		 Map map = new HashMap();
		 map.put("ID", "23");
		 map.put("NAME", "bobo");
		 map.put("AGE", "29");
		 map.put("content", "23 bobo 29");
		 list.add(map);
		 
		 IndexManager.deleteDir(new File(IndexManager.INDEX_DIR));
		 IndexManager.createIndexFromDB(list);
		 IndexManager.searchIndex("23");
	    }
}
