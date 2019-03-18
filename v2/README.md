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

```
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

```
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
      "abstract" : ﻿"..."
      },
      {
      "type" : policy,
      "id" : 43,
      "abstract" : ﻿"...."
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