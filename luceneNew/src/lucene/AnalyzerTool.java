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
 *展示分词后的效果
 */
public class AnalyzerTool {

    /**
     * 打印分词后的信息
     * @param str        待分词的字符串
     * @param analyzer    分词器
     */
    public static void displayToken(String str,Analyzer analyzer){
        try {
            //将一个字符串创建成Token流
            TokenStream stream  = analyzer.tokenStream("content", new StringReader(str));
            CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
            stream.reset();//一定要重置,不然老报错
            while(stream.incrementToken()){
                System.out.print("【" + cta + "】");
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
        
//        String str = "Hello ,I am King,我是 中国人,my email is jianyuan_5731@qq.com";
        String str =   QueryParser.escape("JHYTzzzzzQ9230GSN");
        AnalyzerTool.displayToken(str, aly1);
        AnalyzerTool.displayToken(str, aly2);
        AnalyzerTool.displayToken(str, aly3);
        AnalyzerTool.displayToken(str, aly4);
    }
}