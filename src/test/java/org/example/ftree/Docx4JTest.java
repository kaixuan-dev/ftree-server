package org.example.ftree;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.example.ftree.utils.Docx4JUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;

public class Docx4JTest {

    public static void main(String[] args) throws Docx4JException {
        String html = "<h1 style=\"color:blue; text-align:center;\">测试文档</h1> <p>这是一个 <b>加粗</b>、<i>斜体</i>、<u>下划线</u> 的段落。</p> <p style=\"color:red;\">这是一段红色文字。</p> <p style=\"font-size:18px; font-family:Arial;\">大号字体 + Arial 字体</p> <hr/> <h2>无序列表</h2> <ul> <li>第一项</li> <li>第二项 <b>加粗</b></li> <li>第三项 <i>斜体</i></li> </ul> <h2>有序列表</h2> <ol> <li>步骤一</li> <li>步骤二</li> <li>步骤三</li> </ol> <h2>表格</h2> <table border=\"1\" cellpadding=\"5\" cellspacing=\"0\"> <tr> <th>姓名</th> <th>年龄</th> <th>城市</th> </tr> <tr> <td>张三</td> <td>25</td> <td>北京</td> </tr> <tr> <td>李四</td> <td>30</td> <td>上海</td> </tr> </table> <h2>图片</h2> <p> </p>";
        Document doc = Jsoup.parse(html);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml); // 转 XHTML
        String xhtml = doc.html();

        File docxFile = new File("D://hello.docx");

        // To docx, with content controls
        Docx4JUtils.toDocx(xhtml, docxFile);
    }

}
