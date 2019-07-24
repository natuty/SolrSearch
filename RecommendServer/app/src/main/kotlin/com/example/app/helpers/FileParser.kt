package com.example.app.helpers

import com.github.junrar.Archive
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.poi.hwpf.extractor.WordExtractor
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipFile


//1.
fun getPdfText(file: File): String {
    return PDDocument.load(file).use { PDFTextStripper().getText(it) }
}

//2.
fun getDocText(file: File): String {
    return FileInputStream(file).use { WordExtractor(it).text }
}

//3.
fun getDocxText(file: File): String {
        return FileInputStream(file).use { XWPFWordExtractor(XWPFDocument(it)).text }
}

//4.
fun getZipText(file: File): String {
    var content = ""
    ZipFile(file).apply {
        val entries = this.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            this.getInputStream(entry).use { iStream ->
                File("./temp/"+entry.getName()).apply {
                    FileOutputStream(this).use { outputStream ->
                        iStream.copyTo(outputStream)
                        outputStream.close()
                    }
                    content += getFileText(this)
                }
                iStream.close()
            }
        }
    }.close()
    return content
}

//5.
fun getRarText(file: File): String {
    var content = ""
    Archive(file.inputStream()).apply {
        var fh = this.nextFileHeader()
        while (fh != null) {
            File("./temp/"+fh!!.getFileNameString().trim()).apply {
                this.createNewFile()
                FileOutputStream(this).use { os ->
                    getInputStream(fh).use { it.copyTo(os) }
                    os.close()
                }
                content += getFileText(this)
            }
            fh = this.nextFileHeader()
        }
    }.close()

    return content
}

//6.
fun getExcelText(file: File): String {
    return file.name
}

//7.
fun getPptText(file: File): String {
    return file.name
}

//8.
fun getTxtText(file: File): String {
    return file.readText()
}


fun getFileText(file: File): String{
    val extension = file.extension

    try {
        when(extension){
            FileTypes.PDF.remark -> return getPdfText(file)
            FileTypes.DOC.remark -> return getDocText(file)
            FileTypes.DOCX.remark -> return getDocxText(file)
            FileTypes.ZIP.remark -> return getZipText(file)
            FileTypes.RAR.remark -> return getRarText(file)
            FileTypes.XLSX.remark -> return getExcelText(file)
            FileTypes.XLS.remark -> return getExcelText(file)
            FileTypes.PPT.remark -> return getPptText(file)
            FileTypes.TXT.remark -> return getTxtText(file)
            else -> throw Exception("This file format is not supported")

        }
    }catch (e: Exception){
        e.printStackTrace()
    }
    return ""
}


//pdf. doc. zip  rar  excel,txt
enum class FileTypes(val remark: String) {
    PDF("pdf"),
    DOC("doc"),
    DOCX("docx"),
    ZIP("zip"),
    RAR("rar"),
    XLS("xls"),
    XLSX("xlsx"),
    PPT("ppt"),
    TXT("txt")
}