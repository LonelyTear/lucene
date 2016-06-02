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
     * ��������������
     * @return ������������������
     */
    public IndexManager getManager(){
        if(indexManager == null){
            indexManager = new IndexManager();
        }
        return indexManager;
    }
    /**
     * ������ǰ�ļ�Ŀ¼������
     * @param path ��ǰ�ļ�Ŀ¼
     * @return �Ƿ�ɹ�
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
            //��ȡ�ļ���׺
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
                //������ʱ����  Query query2 = NumericRangeQuery.newLongRange("content", 1l, 1000l, true, true);
                //document.add(new LongField("content",Long.parseLong(content.trim()),Store.YES)); 
                
                //������ʱ����  Query query4 = NumericRangeQuery.newLongRange("content", 1l, 1000l, true, true);
//                document.add(new DoubleField("content",Double.parseDouble(content.trim()),Store.YES)); 
                
                //������ʱ����  Query query3 = NumericRangeQuery.newLongRange("date", 1l, 10000000l, true, true);
                //document.add(new LongField("date",new Date().getTime(),Store.YES)); 
                
                //�����ַ� -
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
        System.out.println("��������-----��ʱ��" + (date2.getTime() - date1.getTime()) + "ms\n");
        return true;
    }
    
    /**
	 * �����ݿ��д�������<br/>
	 * @param list ���������ݿ�����ȡ��һ���ű�,���е�MapΪ<String,Object>��ʽ,һ��Map����һ������ 	<br/>
	 * ����Map�д����ʽ	<br/>
	 * key 	-- value	<br/>
	 * --------------	<br/>
	 * ID 	-- 23		<br/>
	 * NAME -- bobo		<br/>
	 * AGE 	-- 29		<br/>
	 * --------------	<br/>
	 * ���Զ��Map�γɵ�List�ͳ���һ���ű�	<br/>
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
					String name = (String) iterator.next();//nameΪ����
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
	 * ����field�ֶΣ�ֵΪ keyword��ɾ��ָ����document.
	 * @param field ָ����ĳ���ֶ�,Ŀǰ����ֻ��IDΨһ��������ɾ��
	 * @param keywords  keywords �в�����""�մ�. <br/>
	 * ����ɾ���� keywords�в���,������keywords�Ľ���.<br/>
	 * <br/>
	 * ��Ϊ������ϵ,Ŀǰ��field�ֶα�����Ψһ��,��Ȼɾ��ʱ���ܻ�������������ɾ������<br/>
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
     * ��ȡtxt�ļ�������
     * @param file ��Ҫ��ȡ���ļ�����
     * @return �����ļ�����
     */
    public static String txt2String(File file){
        String result = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//����һ��BufferedReader������ȡ�ļ�
            String s = null;
            while((s = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
                result = result + "\n" +s;
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
   
    
    /**
     * �������������ط����������ļ�
     * @param text ���ҵ��ַ���
     * @return �����������ļ�List
     */
    public static void searchIndex(String text){
        Date date1 = new Date();
        try{
            directory = FSDirectory.open(new File(INDEX_DIR));
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
    
            analyzer = new StandardAnalyzer(Version.LUCENE_40);
            QueryParser parser = new QueryParser(Version.LUCENE_40, "content", analyzer);
            parser.setDefaultOperator(QueryParser.AND_OPERATOR);//��ѯ������and
            Query query = parser.parse(text);
//          ������ʱ����document.add(new LongField("content",Long.parseLong(content.trim()),Store.YES));
//          Query query2 = NumericRangeQuery.newLongRange("price", 1l, 1000l, true, true);   
            
//          ������ʱ����document.add(new LongField("date",new Date().getTime(),Store.YES));
            query = NumericRangeQuery.newDoubleRange("content", 1.0, 100.0, true, true); 
//          ������ʱ����document.add(new LongField("date",new Date().getTime(),Store.YES));
//          Query query3 = NumericRangeQuery.newLongRange("date", 1l, 10000000l, true, true);  
            
            ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
        
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                System.out.println("������������������������������������������������������������������������������������������������������");
//                System.out.println(hitDoc.get("filename"));
                System.out.println(hitDoc.get("content"));
//                System.out.println(hitDoc.get("path"));
                System.out.println("������������������������������������������������������������������������������������������������������\n");
            }
            ireader.close();
            directory.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        Date date2 = new Date();
        System.out.println("�鿴����-----��ʱ��" + (date2.getTime() - date1.getTime()) + "ms\n");
    }
    
    /**
     * ����Ŀ¼�µ��ļ�
     * @param dirPath ��Ҫ��ȡ�ļ���Ŀ¼
     * @return �����ļ�list
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
     * �ж��Ƿ�ΪĿ���ļ���Ŀǰ֧��txt xls doc��ʽ
     * @param fileName �ļ�����
     * @return ������ļ����������������������true�����򷵻�false
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
     * ɾ���ļ�Ŀ¼�µ������ļ�<br/>
     * ���Ե�ʱ��ÿ�ζ�deleteһ�±�����������Ŀ¼,��Ȼ��ͬ������һֱ��������
     * @param file Ҫɾ�����ļ�Ŀ¼
     * @return ����ɹ�������true.
     */
    public static boolean deleteDir(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i=0; i<files.length; i++){
                deleteDir(files[i]);
            }
        }
        file.delete();
        System.out.println("ɾ����������Ŀ¼:"+INDEX_DIR);
        return true;
    }
    
   
}