package test;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

/**
 *չʾ�ִʺ��Ч��
 */
public class AnalyzerToolTest {

    /**
     * ��ӡ�ִʺ����Ϣ
     * @param str        ���ִʵ��ַ���
     * @param analyzer    �ִ���
     */
    public static void displayToken(String str,Analyzer analyzer){
        try {
            //��һ���ַ���������Token��
            TokenStream stream  = analyzer.tokenStream("content", new StringReader(str));
            CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
            stream.reset();//һ��Ҫ����,��Ȼ�ϱ���
            while(stream.incrementToken()){
                System.out.print("��" + cta + "��");
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        Analyzer aly1 = new StandardAnalyzer(Version.LUCENE_40);
        Analyzer aly5 = new StandardAnalyzer(Version.LUCENE_40);
        Analyzer aly6 = new StandardAnalyzer(Version.LUCENE_40);
//        Analyzer aly2 = new StopAnalyzer(Version.LUCENE_40);
//        Analyzer aly3 = new SimpleAnalyzer(Version.LUCENE_40);
//        Analyzer aly4 = new WhitespaceAnalyzer(Version.LUCENE_40);
        
        String str1 = "Hi,�찲��,���Ѿ���33������. 0.0584";
        String str5 = "Hellow,��������!";
        String str6 = "Hi,����,���Ѿ���33������.";
        
        
        AnalyzerToolTest.displayToken(str1, aly1);
        AnalyzerToolTest.displayToken(str5, aly1);
        AnalyzerToolTest.displayToken(str6, aly1);
//        AnalyzerToolTest.displayToken(str, aly2);
//        AnalyzerToolTest.displayToken(str, aly3);
//        AnalyzerToolTest.displayToken(str, aly4);
    }
}