package entrance;

import java.io.File;

import lucene.IndexManager;
/**
 * 删除索引main入口
 * @author King
 *
 */
public class delIndexEntrance {
	public static void main(String[] args){
		String[] delArray={"杭州","Hi"};//删的是并集,而非交集,特别注意
		//先清空索引
	 	IndexManager.deleteDir(new File(IndexManager.INDEX_DIR));
	 	//再创建索引
	 	IndexManager.createIndex(IndexManager.DATA_DIR);
	 	//再删除和杭州相关的某个索引
	 	IndexManager.deleteIndexes("content", delArray);
	 	//最后搜索"杭州",看结果是否查到
	 	IndexManager.searchIndex("杭州");
	 	//找不到结果了,证明确实删除了
	 }
}
