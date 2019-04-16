
服务1. /root/solr_search/apiServer
-----------
启动命令： 
```shell
cd /root/solr_search/apiServer
sh start.sh
```


服务2. /root/solr_search/mysql
-----------
启动命令： 
```shell
cd /root/solr_search/mysql
sh start.sh
```


服务3. /root/solr_search/recommendserver
-----------
启动命令： 
```shell
cd /root/solr_search/recommendserver
sh start.sh
```


服务4. /root/solr_search/solr
-----------
启动命令： 
```shell
cd /root/solr_search/solr
sh start.sh
```


# 



nginx.conf
-----------
```
        location /search{
            proxy_pass http://127.0.0.1:9001/search;
            proxy_set_header   X-Real-IP        $remote_addr;
            proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
        }
        
        location /news{
            proxy_pass http://127.0.0.1:9002/news;
            proxy_set_header   X-Real-IP        $remote_addr;
            proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
        }
        
        location /solr{
            proxy_pass http://127.0.0.1:8983/solr;
            proxy_set_header   X-Real-IP        $remote_addr;
            proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
        }

```




