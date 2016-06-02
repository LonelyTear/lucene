package entrance;

import java.io.File;

import org.apache.lucene.queryparser.classic.QueryParser;

import lucene.IndexManager;
/**
 * 用文件创建索引main入口
 * @author King
 *
 */
public class FileEntrance {
	 public static void main(String[] args){
	 	IndexManager.deleteDir(new File(IndexManager.INDEX_DIR));
	 	IndexManager.createIndex(IndexManager.DATA_DIR);
	 	String s = QueryParser.escape("ui$$-$$e");
	 	System.out.println("ui\\-e");
	 	IndexManager.searchIndex("ui\\-e");
	 }
}
