package test;

import org.apache.lucene.queryparser.classic.QueryParser;

public class a {
public static void main(String[] args) {
	String s  = QueryParser.escape("abc-daf");
	System.out.println(s);
}
}
