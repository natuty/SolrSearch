# 新闻资讯推荐接口说明

RecommendServer  新闻资讯推荐



1.添加新闻
--------------

## /news/recommend_news

添加推荐新闻

### POST

form-data

| key     | value    |
| ------- | -------- |
| title   | 新闻标题 |
| content   | 新闻内容 |
| news_id | 新闻id   |

输返回：

```
{
    status: "状态"
    message:"和状态相关的提示"
}
```



# 




2.删除新闻
--------------

## /news/recommend_news

删除推荐新闻


### DELETE


form-data

| key     | value    |
| ------- | -------- |
| news_id | 新闻id   |

输返回：

```
{
    status: "状态"
    message:"和状态相关的提示"
}
```



3.获取推荐信息
--------------

##  /news/recommend


### GET

获取给该企业推荐的新闻。

http://116.56.134.232:9002/recommend?company_id=91440101717852200L&matching=0.5

使用company_id进行查询的时候，系统首先会将提交一个异步任务到后台，重新计算对该企业推荐哪些新闻，异步任务的id放入返回内容中的task_id字段中。新建异步任务之后，系统会查找是否存在这个企业的历史推荐记录，历史记录会放入到返回内容中的result字段中，若不存在历史推荐记录，则result为一个空的列表**（只返回匹配度大于阈值的记录）**

matching表示推荐的结果和企业的匹配程度，


form-data

| key     | value    |
| ------- | -------- |
| company_id | 社会信用代码   |
| matching | 匹配度   |
| task_id | 任务id   |
| type | policy、guide、news, 默认查找全部类型   |

输返回：

```
{
    "task_id": "异步任务id，可以用于更新推荐的新闻",
    "result": [
        {
            company_id：【{ newsid:1, matching:0.9, status:"状态信息",time: "推荐时间", type="news"},{ newsid:2, matching:0.9, status:"状态信息",time: "推荐时间",type="guide" },】
        }
    ]
}
```

/news/recommend?task_id=123

使用company_id进行查询后，可以使用拿到的task_id获取更新后的企业推荐记录。

```
{
    "status": "任务状态",
    "result": [
        {
            company_id：【{ newsid:1, matching:0.9, status:"状态信息",time: "推荐时间",type="guide"},{ newsid:2, matching:0.9, status:"状态信息",time: "推荐时间",type="news" },】
        }
    ]
}
```



4.推送信息
--------------

##  /news/checkRecommend


### POST
计算多个企业和所有新闻资讯的匹配情况。此接口针对新闻资讯推送功能。当一个新新闻资讯发布之后，平台在上传新闻资讯信息之后，调用此接口批量获取新新闻资讯与目标企业的匹配情况。匹配情况异步计算，需要提供回调函数url以接受异步任务执行情况。

输入：

form-data

| key     | value    |
| ------- | -------- |
| companies | 社会信用代码   |
| threshold | 匹配度   |
| callbackUrl | 回调函数url   |
| type | policy、guide、news, 默认查找全部类型   |


json格式
```

{
    "companies": "企业id1,企业id2,...,企业idn",
    "threshold":"匹配度阈值，只有大于该匹配度的结果才会再callback中返回",
    "callback": "回调函数url",
    "type": "news"
}
```
输出：
```
{
    "task_id": "异步任务id",
    "message":
    {
        "status": true,
        "traceback":"错误消息"
    }
}
```

由于计算资源限制，对每个企业计算匹配度的任务会放在任务队列中进行调度，队列长度限制为100。也就是每次最多通过此接口传入100个企业id，等任务完成回调后再次传入下一批企业id。

如果传入企业id数量大于队列剩余长度，则会尽可能放入将企业id放入队列中，剩下未能放入的企业id将会返回到traceback字段中。例子如下
```
{
    "task_id": "任务id", # 如果所有企业都不能放进去则任务id为空
    "message":
    {
        "status":"FULL",
        "traceback":["未能放入的企业id1","未能放入的企业id2","未能放入的企业id3"]
    }
}

```



### callback定义

```
`callback`应为完整的url，如`https://test.com/callback`
```

callback接口接受POST请求，请求数据为json格式

输入：

```
{
    "task_id":"任务id"
    "guide_id":"指南id",
    "result":[
      {"data":[
            {"newsId":"6","matching":37.052,type="guide"},
            {"newsId":"2","matching":8.85842,type="news"},
            ...
          ],
      "companyId": "91440101717852200L"
      },
      ...
    ]
}
```

### callback测试

在系统接到`/policy/check_recommend/`的请求后，会对callback的url进行测试，测试时候会发送如下消息

```
{
    "task_id":"test",
    "result":[
       {"data":{"newsId":"6","matching":一个0到100之间的随机数,type="news"}
       ],
       "companyId": "123"
       }
    }
}
```

callback应该返回该随机数，以确保callback接口能够正确读取结果。

测试不通过则不会将企业id加入队列中，并返回如下消息

```
{
    "task_id": "", 
    "message":
    {
        "status":"CALLBACK_FAIL"
    }
}
```



