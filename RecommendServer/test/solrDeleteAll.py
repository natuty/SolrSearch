from urllib2 import *
connection = urlopen('http://116.56.134.166:8983/solr/mycore/update?stream.body=<delete><query>*:*</query></delete>&commit=true')
print(connection.read())