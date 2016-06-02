package test;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class BooleanQueryTest {
	public static void main(String[] args) {
		import java.io.File;  
		import java.io.IOException;  
		import org.apache.lucene.document.Document;  
		import org.apache.lucene.index.IndexReader;  
		import org.apache.lucene.index.Term;  
		import org.apache.lucene.search.BooleanClause.Occur;  
		import org.apache.lucene.search.BooleanQuery;  
		import org.apache.lucene.search.IndexSearcher;  
		import org.apache.lucene.search.ScoreDoc;  
		import org.apache.lucene.search.TermQuery;  
		import org.apache.lucene.search.TopDocs;  
		import org.apache.lucene.search.WildcardQuery;  
		import org.apache.lucene.store.Directory;  
		import org.apache.lucene.store.FSDirectory;  
		  
		/** 
		 * lucene����BooleanQuery���ж��Query��ϲ�ѯ 
		 *  
		 */  
		public class LuceneWildCardSearcher {  
		  
		    public static void main(String[] args) {  
		          
		        String dir = "D:\\index";  
		        try {  
		              
		            Directory directory = FSDirectory.getDirectory(new File(dir));  
		            @SuppressWarnings("deprecation")  
		            IndexReader reader = IndexReader.open(directory);  
		            IndexSearcher indexSearcher = new IndexSearcher(reader);  
		            Term term1 = new Term("filename", "<<������ƫ��>>");  
		            WildcardQuery wildcardQuery = new WildcardQuery(term1);  
		            Term term2 = new Term("content", "�����ñ����޷������ң�ƫ�������޷�ȥ������");  
		            TermQuery termQuery = new TermQuery(term2);  
		            //����booleanQuery,Ȼ�������query������
		            BooleanQuery booleanQuery = new BooleanQuery();  
		            booleanQuery.add(wildcardQuery, Occur.MUST);  
		            booleanQuery.add(termQuery, Occur.MUST);  
		            TopDocs topDocs = indexSearcher.search(booleanQuery,null, 10);  
		            ScoreDoc scoreDocs[] = topDocs.scoreDocs;  
		            for (int i = 0; i < scoreDocs.length; i++) {  
		                Document document = indexSearcher.doc(scoreDocs[i].doc);  
		                System.out.println(document.get("id"));  
		                System.out.println(document.get("name"));  
		                System.out.println(document.get("text"));  
		                System.out.println(document.get("datetime"));  
		            }  
		            directory.close();  
		        } catch (IOException e) {  
		              
		            e.printStackTrace();  
		        }  
		    }  
		}  
	}
}