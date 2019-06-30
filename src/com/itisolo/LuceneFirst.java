package com.itisolo;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author 张生祥
 * @date 2019/04/30 11:09
 */
public class LuceneFirst {
    /**
     * 1.创建一个indexWriter对象
     *      * 指定索引库的存放位置 Directory对象
     *      * 指定一个indexWriterConfig对象
     * 2.创建document对象
     * 3.创建field对象，将field添加到document对象中
     * 4.使用indexWriter将document对象写入索引库，此过程进行索引创建。并将索引和document对象写入索引库
     * 5.关闭indexWriter对象
     */
    @Test
    public void createIndex() throws IOException {
        //创建索引库对象,指定索引库的位置
        Directory directory = FSDirectory.open(new File("F:\\temp\\index").toPath());
        //索引库存入内存
//        Directory directory1 = new RAMDirectory();
        //创建indexWriterConfig
        IndexWriterConfig config = new IndexWriterConfig();
        //创建indexWriter对象,传入之前创建好的两个对象
        IndexWriter indexWriter = new IndexWriter(directory,config);
        //创建原始文档对象
        File dir = new File("F:\\temp\\searchsource");
        for (File file : dir.listFiles()) {
            //文件名
            String fileName = file.getName();
            //文件内容
            String fileContent = FileUtils.readFileToString(file, "utf-8");
            //文件路径
            String filePath = file.getPath();
            //文件大小
            long fileSize = FileUtils.sizeOf(file);
            //创建对应的文件域
            //文件名称域
            Field fileNameField = new TextField("fileName",fileName, Field.Store.YES);
            //文件内容域
            Field fileContentField = new TextField("fileContext",fileContent, Field.Store.YES);
            //文件路径域
            Field filePathField = new TextField("path",filePath, Field.Store.YES);
            //文件的大小域
            Field fileSizeField = new TextField("size",fileSize + "", Field.Store.YES);

            //创建document对象
            Document document = new Document();
            document.add(fileNameField);
            document.add(fileContentField);
            document.add(filePathField);
            document.add(fileSizeField);
            //创建索引，并写入索引库
            indexWriter.addDocument(document);
        }
        //关闭indexWriter
        indexWriter.close();
    }

    /**
     * 查询索引库
     */
    @Test
    public void searchIndex() throws IOException {
        //指定索引库存放路径
        Directory directory = FSDirectory.open(new File("F:\\temp\\index").toPath());
        //创建indexReader对象,传入要读取的索引库
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建indexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //创建查询
        Query query = new TermQuery(new Term("fileName","apache"));
        //执行查询
        //第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);
        //查询结果的总条数
        System.out.println("查询的总条数：" + topDocs.totalHits);
        //遍历查询结果
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document =  indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("fileName"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
            System.out.println("----------分割线------------");
        }
        indexReader.close();
    }

    /**
     * 查看标准分析器的分词效果
     */
    @Test
    public void testTokenStream(){
        //创建一个标准分析器对象
        Analyzer analyzer = new StandardAnalyzer();
        //获得tokenSteam对象
        //第一个参数：域名，可以随便给一个
        //第二个参数：要分析的文本内容
//        TokenStream tokenStream = analyzer.tokenStream()

        System.out.println("我改代码了。。。。。。。。");
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa");

        System.out.println("bbbbbbbbbbbbbbbbbbbb");
    }
}
