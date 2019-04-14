
# python 3.6
import xml.etree.cElementTree as ET
import os

path = "./text/"
fileId = 1
dirs = os.listdir(path)
root = ET.Element("add")

for file in dirs:
    fname = path + file
    fileId = fileId + 1
    with open(fname, encoding='utf-8') as f:
        content = f.read().replace("\n","").replace("\r","")
        doc = ET.SubElement(root, "doc")
        ET.SubElement(doc, "field", name="id").text = str(fileId)
        ET.SubElement(doc, "field", name="title").text = file
        ET.SubElement(doc, "field", name="content").text = content
        

tree = ET.ElementTree(root)
tree.write("filename.xml", encoding="UTF-8")