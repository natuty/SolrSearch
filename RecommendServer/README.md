# 新闻资讯推荐接口说明

RecommendServer  新闻资讯推荐



1.添加新闻,公告,支撑政策
--------------

**简要描述：** 
- 添加推荐新闻,公告,支撑政策

**请求URL：** 
- ` http://xxx/news/recommend_news`

**请求方式：**
- POST 

**参数：** 

| 参数名     | 必选   | 类型     | 说明                                       |
| :------ | :--- | :----- | ---------------------------------------- |
| news_id | 是    | string | id                                       |
| title   | 是    | string | 标题                                       |
| content | 是    | string | 内容                                       |
| type    | 是    | string | news, announce, publicity, policy, guide |
| files    | 否    | file | 附件, 支持格式为: pdf, doc, docx, zip, rar, xls, xlsx, ppt, txt |



 **返回示例**
```
{
    "status": true,
    "message": "success"
}
```
 **返回参数说明** 

| 参数名     | 类型      | 说明                |
| :------ | :------ | ----------------- |
| status  | boolean | 状态，1：true；2：false |
| message | string  | 状态信息              |



 **备注说明** 

| 类型        | 说明   |
| :-------- | ---- |
| policy    | 支撑政策   |
| guide     | 指南   |
| news      | 新闻   |
| announce  | 公告   |
| publicity | 公示   |





# 




2.删除新闻
--------------

**简要描述：** 
- 删除推荐新闻,公告,支撑政策

**请求URL：** 
- ` http://xxx/news/recommend_news`

**请求方式：**
- DELETE 

**参数：** 

| 参数名     | 必选   | 类型     | 说明                                       |
| :------ | :--- | :----- | ---------------------------------------- |
| news_id | 是    | string | id                                       |
| type    | 是    | string | news, announce, publicity, policy, guide |


 **返回示例**
```
{
    "status": true,
    "message": "success"
}
```
 **返回参数说明** 

| 参数名     | 类型      | 说明                |
| :------ | :------ | ----------------- |
| status  | boolean | 状态，1：true；2：false |
| message | string  | 状态信息              |




# 





3.1.获取推荐信息
--------------

**简要描述：** 
- 获取推荐信息

**请求URL：** 
- ` http://xxx/news/recommend`

**请求方式：**
- POST 

**参数：** 

| 参数名        | 必选   | 类型     | 说明                                 |
| :--------- | :--- | :----- | ---------------------------------- |
| company_id | 是    | string | 社会信用代码                             |
| matching   | 是    | float  | 匹配度                                |
| type       | 是    | string | news, announce, policy, guide |


 **返回示例**
1.没有历史记录时：
```
{
    "result": "",
    "task_id": 270
}
```
2.有历史记录时：
```
{
    "result": {
        "data": [
            {
                "newsId": "4",
                "time": "2019-04-17T12:36:49.000+0000",
                "type": "news",
                "matching": 3.80231
            },
            {
                "newsId": "6",
                "time": "2019-04-17T12:36:50.000+0000",
                "type": "news",
                "matching": 3.00983
            }
        ]
    },
    "task_id": ""
}
```

 **返回参数说明** 

| 参数名     | 类型     | 说明       |
| :------ | :----- | -------- |
| result  |        | 上一次的推荐记录 |
| task_id | string | 任务id     |





3.2.根据task_id获取推荐信息
--------------

**简要描述：** 
- 根据task_id获取推荐信息

**请求URL：** 
- ` http://xxx/news/recommend`

**请求方式：**
- POST 

**参数：** 

| 参数名     | 必选   | 类型     | 说明   |
| :------ | :--- | :----- | ---- |
| task_id | 是    | string | 任务id |


 **返回示例**
```
{
    "result": [
        {
            "data": [
                {
                    "newsId": "4",
                    "time": "2019-04-17T12:36:49.000+0000",
                    "type": "news",
                    "matching": 3.80231
                },
                {
                    "newsId": "6",
                    "time": "2019-04-17T12:36:50.000+0000",
                    "type": "news",
                    "matching": 3.00983
                }
            ],
            "companyId": "91440101717852200L"
        }
    ],
    "message": "",
    "status": "Completed"
}
```


 **返回参数说明** 

| 参数名     | 类型     | 说明                                  |
| :------ | :----- | ----------------------------------- |
| result  |        | 任务结果                                |
| status  | string | 任务的状态(Completed, UnCompleted, Fail) |
| message | string | 状态信息                                |






# 






4.1.推送信息
--------------

**简要描述：** 
- 推送信息

**请求URL：** 
- ` http://xxx/news/checkRecommend`

**请求方式：**
- POST 

**参数：** 

| 参数名         | 必选   | 类型     | 说明                                       |
| :---------- | :--- | :----- | ---------------------------------------- |
| companies   | 是    | string | 社会信用代码列表                                 |
| threshold   | 是    | string | 匹配度                                      |
| callbackUrl | 是    | string | 回调函数url                                  |
| type        | 是    | string | news, announce, publicity, policy, guide |


 **返回示例**
```
{
    "message": {
        "traceback": "",
        "status": true
    },
    "task_id": 263
}
```
 **返回参数说明** 

| 参数名     | 类型     | 说明   |
| :------ | :----- | ---- |
| task_id | string | 任务id |
| message | string | 状态信息 |


4.2.根据task_id获取推送信息
--------------

**简要描述：** 
- 根据task_id获取推送信息

**请求URL：** 
- ` http://xxx/news/recommend`

**请求方式：**
- POST 

**参数：** 

| 参数名     | 必选   | 类型     | 说明   |
| :------ | :--- | :----- | ---- |
| task_id | 是    | string | 任务id |


 **返回示例**
```
{
    "result": [
        {
            "data": [
                {
                    "newsId": "3111",
                    "time": "2019-04-17T12:14:05.000+0000",
                    "type": "announce",
                    "matching": 1.14513
                },
                {
                    "newsId": "111",
                    "time": "2019-04-17T12:14:05.000+0000",
                    "type": "announce",
                    "matching": 1.14513
                }
            ],
            "companyId": "91440101717852200L"
        },
        {
            "data": [
                {
                    "newsId": "3111",
                    "time": "2019-04-17T12:14:05.000+0000",
                    "type": "announce",
                    "matching": 1.14513
                },
                {
                    "newsId": "111",
                    "time": "2019-04-17T12:14:05.000+0000",
                    "type": "announce",
                    "matching": 1.14513
                }
            ],
            "companyId": "91440101717852200L"
        }
    ],
    "message": "",
    "status": "Completed"
}
```
 **返回参数说明** 

| 参数名     | 类型     | 说明                                  |
| :------ | :----- | ----------------------------------- |
| result  |        | 任务结果                                |
| status  | string | 任务的状态(Completed, UnCompleted, Fail) |
| message | string | 状态信息                                |





# 




5.1回调接口
--------------

**简要描述：** 
- 回调函数

**请求URL：** 
- ` http://xxx/callback`

**请求方式：**
- POST 

**参数：** 

| 参数名     | 必选     | 类型     | 说明   |
| :------ | :----- | :----- | ---- |
| task_id | 是      | string | 任务id |
| message | string | 请求信息   |      |

 **请求示例**
```
task_id: "test"
message: ""
```






5.2回调接口测试
--------------
**简要描述：** 
- 回调接口测试

**请求URL：** 
- ` http://xxx/callback`

**请求方式：**
- POST 

**参数：** 

| 参数名     | 必选     | 类型     | 说明   |
| :------ | :----- | :----- | ---- |
| task_id | 是      | string | 任务id |
| message | string | 请求信息   |      |


 **请求示例**
```
{
    "task_id": "123", 
    "message": ""
}
```

 **返回示例**
```
{
    "task_id": "123", 
    "message": ""
}

```






6.新闻资讯点击
--------------
**简要描述：** 
- 新闻资讯点击

**请求URL：** 
- ` http://xxx/news/click`

**请求方式：**
- POST 

**参数：** 

| 参数名     | 必选     | 类型     | 说明   |
| :------ | :----- | :----- | ---- |
| company_id | 是      | string | 社会信用代码 |
| news_id | 是    | string | id                                       |
| type       | 是    | string | news, announce |


**请求示例**
```
{
    "company_id": "91440101717852200L", 
    "news_id": "123",
    "type": "news"
}
```

**返回示例**
```
{
    "status": true,
    "message": "success"
}
```


7.不感兴趣接口
--------------
**简要描述：** 
- 不感兴趣接口

**请求URL：** 
- ` http://xxx/news/uninterested`

**请求方式：**
- POST 

**参数：** 

| 参数名     | 必选     | 类型     | 说明   |
| :------ | :----- | :----- | ---- |
| company_id | 是      | string | 社会信用代码 |
| news_id | 是    | string | id                                       |
| type       | 是    | string | news, announce, publicity, policy |


**请求示例**
```
{
    "company_id": "91440101717852200L", 
    "news_id": "123",
    "type": "news"
}
```

**返回示例**
```
{
    "status": true,
    "message": "success"
}
```


