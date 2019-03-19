# 全文检索接口说明



使用POST方法进行访问，地址为 http://{host}:9000/data

#### 1.回调方法

http post请求

信息为json格式，字段如下

| 字段    |                   |      |
| ----- | ----------------- | ---- |
| event | delete、add、update |      |
| type  | policy、guide      |      |
| id    |                   | 文件id |

将以下信息填入到body中

```json
{
    "id": "40",
    "event": "add", 
    "type": "guide"
}
```

返回的信息如下

```json
{
    "status": true
}
```

python访问demo脚本：

```python
import requests
data={'id': "40", 'event': 'update', 'type': 'guide'}
r = requests.post("http://116.56.134.226:9000/data", data)
print(r.text)
```



#  

#### 2. 全文检索方法

http post请求

信息为json格式，字段如下

| 字段       |                            |      |
| -------- | -------------------------- | ---- |
| keyword  |                            | 关键词  |
| fileType | Guide、Policy、GuideOrPolicy |      |
| current  | 默认为0                       | 第几页  |
| pageSize | 默认为10                      | 每页条目 |

将以下信息填入到body中

```json
{
    "keyword": "南沙",
    "fileType": "GuideOrPolicy", 
    "current": 0,
    "pageSize": 10
}
```

返回的信息如下

```json
{
  "data" : [
      {
      "type" : guide,
      "id" : 41,
      "abstract" : ﻿"广州<b>南沙</b>新区（自贸片区）先进制造业企业技改后奖补办事指南\n\n一、 政策依据\n《广州<b>南沙</b>新区（自贸片区）促进先进制造业与建筑业发展扶持办法》"
      },
      {
      "type" : guide,
      "id" : 42,
      "abstract" : "..."
      },
      {
      "type" : policy,
      "id" : 43,
      "abstract" : "...."
      },
  	  ....
  ],
  "totalPages" : 1,
  "totalElements" : 10
}
```

python访问demo脚本：

```python
import requests
data={'keyword': "南沙", 'fileType': 'GuideOrPolicy', 'current': 0, 'pageSize': 10 }
r = requests.post("http://116.56.134.226:9000/search", data)
print(r.status_code, r.reason)
print(r.text)
```



#  

参考文档

[智能平台数据资源rpc接口说明](https://github.com/fishersosoo/NS_policy_recommendation/blob/feature/docs/%E6%99%BA%E8%83%BD%E5%B9%B3%E5%8F%B0%E6%95%B0%E6%8D%AE%E8%B5%84%E6%BA%90rpc%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E.md)

[jsonrpc4j](https://github.com/briandilley/jsonrpc4j)

[Spring Data for Apache Solr](https://docs.spring.io/spring-data/solr/docs/4.1.0.M2/reference/html/#reference)

[docker](https://docs.docker.com/get-started/)



