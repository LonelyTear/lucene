package test;

import java.io.File;
import java.util.Date;

import lucene.IndexManager;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
/**
 * 正在研究
 * select * from Student where ( id = 1  || address = '浙江') and ( name = 'bobo' || age = '18')
 * @author King
 *
 */
public class LuceneTest {
	 private static IndexManager indexManager;
	    private static String content="";
	    
	    public static String INDEX_DIR = "D:\\luceneIndex";
	    public static String DATA_DIR = "D:\\luceneData";
	    private static Analyzer analyzer = null;
	    private static Directory directory = null;
	    private static IndexWriter indexWriter = null;
	/**
     * 查找索引，返回符合条件的文件
     * @param text 查找的字符串
     * @return 符合条件的文件List
     */
    public static void searchIndex(String text){
        Date date1 = new Date();
        try{
            directory = FSDirectory.open(new File(INDEX_DIR));
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
    
            analyzer = new StandardAnalyzer(Version.LUCENE_40);
            
            QueryParser parser1 = new QueryParser(Version.LUCENE_40, "content", analyzer);
            parser1.setDefaultOperator(QueryParser.AND_OPERATOR);//查询条件用and
            Query query1 = parser1.parse("hi");
            
            QueryParser parser2 = new QueryParser(Version.LUCENE_40, "filename", analyzer);
            parser2.setDefaultOperator(QueryParser.AND_OPERATOR);//查询条件用and
            Query query2 = parser2.parse("北");
            
            QueryParser parser3 = new QueryParser(Version.LUCENE_40, "path", analyzer);
            parser3.setDefaultOperator(QueryParser.AND_OPERATOR);//查询条件用and
            Query query3 = parser3.parse("luceneDatad");
            
            BooleanQuery booleanQuery = new BooleanQuery();  
            booleanQuery.add(query1, Occur.MUST);  
            booleanQuery.add(query2, Occur.MUST);
            booleanQuery.add(query3, Occur.MUST);
            
            
            ScoreDoc[] hits = isearcher.search(booleanQuery, null, 1000).scoreDocs;
        
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓结果↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓结果↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
//                System.out.println(hitDoc.get("filename"));
                System.out.println(hitDoc.get("content"));
//                System.out.println(hitDoc.get("path"));
                System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑结果↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑结果↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑\n");
            }
            ireader.close();
            directory.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        Date date2 = new Date();
        System.out.println("查看索引-----耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
    }
    
    
    public static void main(String[] args){
//    	IndexManager.deleteDir(new File(IndexManager.INDEX_DIR));
//	 	IndexManager.createIndex(IndexManager.DATA_DIR);
	 	searchIndex("杭州");
	 }
}
