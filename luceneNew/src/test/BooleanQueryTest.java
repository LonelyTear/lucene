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
		 * lucene利用BooleanQuery进行多个Query组合查询 
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
		            Term term1 = new Term("filename", "<<傲慢与偏见>>");  
		            WildcardQuery wildcardQuery = new WildcardQuery(term1);  
		            Term term2 = new Term("content", "傲慢让别人无法来爱我，偏见让我无法去爱别人");  
		            TermQuery termQuery = new TermQuery(term2);  
		            //创建booleanQuery,然后把其它query串起来
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
