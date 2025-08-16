package org.example.ftree.utils;

import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import java.io.File;

public class Docx4JUtils {

    /**
     *
     * @param html
     * @param outFile
     * @throws Docx4JException
     */
    public static void toDocx(String html, File outFile) throws Docx4JException {
        // 2. 创建 Word 文档
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        MainDocumentPart mainPart = wordMLPackage.getMainDocumentPart();

        // 3. 使用 XHTMLImporterImpl 转换 HTML → Word 元素
        XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
        mainPart.getContent().addAll(importer.convert(html, null));

        // 4. 保存 DOCX 文件
        wordMLPackage.save(outFile);
    }

}
