package lucene;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class IndexManager{
    private static IndexManager indexManager;
    private static String content="";
    
    public static String INDEX_DIR = "src/lucene_need/luceneIndex";
    public static String DATA_DIR = "src/lucene_need/luceneData";
    private static Analyzer analyzer = null;
    private static Directory directory = null;
    private static IndexWriter indexWriter = null;
    
    /**
     * 创建索引管理器
     * @return 返回索引管理器对象
     */
    public IndexManager getManager(){
        if(indexManager == null){
            indexManager = new IndexManager();
        }
        return indexManager;
    }
    /**
     * 创建当前文件目录的索引
     * @param path 当前文件目录
     * @return 是否成功
     */
    public static boolean createIndex(String path){
        Date date1 = new Date();
        List<File> fileList = getFileList(path);
        analyzer = new StandardAnalyzer(Version.LUCENE_40);
        try {
			directory = FSDirectory.open(new File(INDEX_DIR));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        for (File file : fileList) {
            content = "";
            //获取文件后缀
            String type = file.getName().substring(file.getName().lastIndexOf(".")+1);
            if("txt".equalsIgnoreCase(type)){
                content += txt2String(file);
            }
            System.out.println("name :"+file.getName());
            System.out.println("path :"+file.getPath());
            System.out.println("content :"+content);
            System.out.println();
            
            try{
    
                File indexFile = new File(INDEX_DIR);
                if (!indexFile.exists()) {
                    indexFile.mkdirs();
                }
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
                indexWriter = new IndexWriter(directory, config);
                
                Document document = new Document();
                document.add(new TextField("filename", file.getName(), Store.YES));
//                document.add(new TextField("content", content, Store.YES));
                //查索引时必须  Query query2 = NumericRangeQuery.newLongRange("content", 1l, 1000l, true, true);
                //document.add(new LongField("content",Long.parseLong(content.trim()),Store.YES)); 
                
                //查索引时必须  Query query4 = NumericRangeQuery.newLongRange("content", 1l, 1000l, true, true);
//                document.add(new DoubleField("content",Double.parseDouble(content.trim()),Store.YES)); 
                
                //查索引时必须  Query query3 = NumericRangeQuery.newLongRange("date", 1l, 10000000l, true, true);
                //document.add(new LongField("date",new Date().getTime(),Store.YES)); 
                
                //特殊字符 -
                content  = QueryParser.escape(content);
                document.add(new TextField("content", content, Store.YES));
                
                document.add(new TextField("path", file.getPath(), Store.YES));
                indexWriter.addDocument(document);
                indexWriter.commit();
                closeWriter();
            }catch(Exception e){
                e.printStackTrace();
            }
            content = "";
        }
        Date date2 = new Date();
        System.out.println("创建索引-----耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
        return true;
    }
    
    /**
	 * 从数据库中创建索引<br/>
	 * @param list 代表从数据库中提取的一整张表,其中的Map为<String,Object>格式,一个Map代表一行数据 	<br/>
	 * 单个Map中存放形式	<br/>
	 * key 	-- value	<br/>
	 * --------------	<br/>
	 * ID 	-- 23		<br/>
	 * NAME -- bobo		<br/>
	 * AGE 	-- 29		<br/>
	 * --------------	<br/>
	 * 所以多个Map形成的List就成了一整张表	<br/>
	 * @return
	 */
	public static boolean createIndexFromDB(List<Map> list) {
		if (null == list)
			return false;
		try {
			analyzer = new StandardAnalyzer(Version.LUCENE_40);
			directory = FSDirectory.open(new File(INDEX_DIR));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
			indexWriter = new IndexWriter(directory, config);
			for (Map map : list) {
				Document document = new Document();
				Iterator iterator = map.keySet().iterator();
				while (iterator.hasNext()) {
					String name = (String) iterator.next();//name为列名
					document.add(new TextField(name, (String) map.get(name),Store.YES));
				}
				indexWriter.addDocument(document);
			}
			indexWriter.commit();
			closeWriter();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	
	/**
	 * 根据field字段（值为 keyword）删除指定的document.
	 * @param field 指定的某个字段,目前建议只用ID唯一主键进行删除
	 * @param keywords  keywords 中不能有""空串. <br/>
	 * 而且删的是 keywords中并集,而不是keywords的交集.<br/>
	 * <br/>
	 * 因为并集关系,目前该field字段必需是唯一的,不然删除时可能会引出其它多余删除问题<br/>
	 */
	public static void deleteIndexes(String field, String[] keywords) {
		Directory dir;
		IndexWriterConfig config;
		try {
			dir = FSDirectory.open(new File(INDEX_DIR));
			analyzer = new StandardAnalyzer(Version.LUCENE_40);
			config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
			indexWriter = new IndexWriter(dir, config);
			QueryParser parser = new QueryParser(Version.LUCENE_40, field,	analyzer);
			Query query;
			for(String keyword : keywords){
				query = parser.parse(keyword);
				indexWriter.deleteDocuments(query);
			}
			indexWriter.commit();
			indexWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	
    /**
     * 读取txt文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String txt2String(File file){
        String result = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result = result + "\n" +s;
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
   
    
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
            QueryParser parser = new QueryParser(Version.LUCENE_40, "content", analyzer);
            parser.setDefaultOperator(QueryParser.AND_OPERATOR);//查询条件用and
            Query query = parser.parse(text);
//          建索引时必须document.add(new LongField("content",Long.parseLong(content.trim()),Store.YES));
//          Query query2 = NumericRangeQuery.newLongRange("price", 1l, 1000l, true, true);   
            
//          建索引时必须document.add(new LongField("date",new Date().getTime(),Store.YES));
            query = NumericRangeQuery.newDoubleRange("content", 1.0, 100.0, true, true); 
//          建索引时必须document.add(new LongField("date",new Date().getTime(),Store.YES));
//          Query query3 = NumericRangeQuery.newLongRange("date", 1l, 10000000l, true, true);  
            
            ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
        
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
    
    /**
     * 过滤目录下的文件
     * @param dirPath 想要获取文件的目录
     * @return 返回文件list
     */
    public static List<File> getFileList(String dirPath) {
        File[] files = new File(dirPath).listFiles();
        List<File> fileList = new ArrayList<File>();
        for (File file : files) {
            if (isTxtFile(file.getName())) {
                fileList.add(file);
            }
        }
        return fileList;
    }
    /**
     * 判断是否为目标文件，目前支持txt xls doc格式
     * @param fileName 文件名称
     * @return 如果是文件类型满足过滤条件，返回true；否则返回false
     */
    public static boolean isTxtFile(String fileName) {
        if (fileName.lastIndexOf(".txt") > 0) {
            return true;
        }
        return false;
    }
    
    public static void closeWriter() throws Exception {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }
    
    /**
     * 删除文件目录下的所有文件<br/>
     * 测试的时候每次都delete一下被创建的索引目录,不然相同索引会一直往里添加
     * @param file 要删除的文件目录
     * @return 如果成功，返回true.
     */
    public static boolean deleteDir(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i=0; i<files.length; i++){
                deleteDir(files[i]);
            }
        }
        file.delete();
        System.out.println("删除整个索引目录:"+INDEX_DIR);
        return true;
    }
    
   
}