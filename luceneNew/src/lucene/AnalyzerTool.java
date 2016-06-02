package lucene;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.util.Version;

/**
 *չʾ�ִʺ��Ч��
 */
public class AnalyzerTool {

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
        Analyzer aly2 = new StopAnalyzer(Version.LUCENE_40);
        Analyzer aly3 = new SimpleAnalyzer(Version.LUCENE_40);
        Analyzer aly4 = new WhitespaceAnalyzer(Version.LUCENE_40);
        
//        String str = "Hello ,I am King,���� �й���,my email is jianyuan_5731@qq.com";
        String str =   QueryParser.escape("JHYTzzzzzQ9230GSN");
        AnalyzerTool.displayToken(str, aly1);
        AnalyzerTool.displayToken(str, aly2);
        AnalyzerTool.displayToken(str, aly3);
        AnalyzerTool.displayToken(str, aly4);
    }
}