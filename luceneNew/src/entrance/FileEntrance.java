package entrance;

import java.io.File;

import org.apache.lucene.queryparser.classic.QueryParser;

import lucene.IndexManager;
/**
 * ���ļ���������main���
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