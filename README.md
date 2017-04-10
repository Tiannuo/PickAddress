# PickAddress
##地址选择器

# 联动crud接口文档

#### 4.4.1 创建IFTTT规则

```
curl -v -X POST \
    -H "Authorization: Bearer {JWT_TOKEN}" \
    -H "Content-Type: application/json" \
    -H "Accept: application/json" \
    "http://user.openapi.hekr.me/rule/iftttRule" \
    -d '{
        "devTid" : "ESP_xxx",
        "ctrlKey" : "xxx",
        "subDevTid": "01234567788"
        "ruleName" : "新建联动",
        "condition" : {
            "triggerType" : REPORT,
            "conDesc" : "触发描述"
            "triggerParams" : [
              {
                "left" : "_cmdId",
                "operator" : "==",
                "right": 10
              },
              {
                "left" : "_cloudTime",
                "operator" : "==",
                "right" : "0 15 10 * * ? 2016"
              },
              ......      
            ]
        },
        "iftttTasks" : [
            {
                "type": "APPSEND",
                "params": {
                    "devTid": "2016-08-17T20-21-42",
                    "ctrlKey": "54308a73b0b34911a14206b8baaba57f",
                    "desc" : "开灯"
                    "data": {
                        "raw": "48070200010153"
                    }
                }
            },
            {
                "type": "APPSUBSEND",
                "params": {
                    "devTid": "2016-08-17T20-21-42",
                    "ctrlKey": "54308a73b0b34911a14206b8baaba57f",
                    "desc" : "开灯"
                    "data"：{
                        "cmdId" : 1,
                        "key1" : "value"
                    }
                }

            },
            {
                "type": "APPGROUPSEND",
                "params": {
                    "groupId":"123123",
                    "desc" : "开客厅灯"
                    "data"：{
                        "cmdId" : 1,
                        "key1" : "value"
                    }
                }
            },
            {
                "type": "SCENETRIGGERSEND",
                "params": {
                    "sceneId":"123123",
                    "desc" : "执行场景"
                    "data"：{
                        "cmdId" : 1,
                        "key1" : "value"
                    }
                }
            },
            {
                "type": "ENABLEIFTTT",
                "desc" : "开启联动"
                "params": {
                    "iftttId":"123123123",
                    "enable":"enable\disable"
                }
            },
            {
                "type": "DALAYTIME",
                "desc" : "延时执行"
                "params": {
                    "time":"10"
                }
            }
        ],
        "desc" : "xxxxxx"
        "pushMsg": {
            "pushEnable": true,
            "isAlarm": true,
            "pushTemplateId": "00000000012"
        },
        "smsPushMsg": {
            "pushEnable": "true",
            "pushTemplateId": "00000000002"
        },
        "iftttType":"CUSTOM",
        "cronExpr" : "0 15 10 * * ? 2016",
        "timeZoneOffset" : 480,
        "setRing":{
            "cmdId" : 1,
            "key1" : "value"
        },
        "undo_ring" = {
            "undoParams": [
                {
                    "left": "_cmdId",
                    "operator": "==",
                    "right": 10,
                }
            ],
            "subIftttTask": {
                "type": "appSend",
                "params": {
                    "devTid": "2016-08-17T20-21-42",
                    "ctrlKey": "54308a73b0b34911a14206b8baaba57f",
                    "data": {
                        "raw": "48070200010153"
                    }
                }
            }
        }
    }'
```

**参数**

|  参数名  | 是否可选 | 参数类型 |   取值范围   |     说明            |
|:--------:|:--------:|:--------:|:------------:|:-------------------:|
| devTid   |   必选   |  String  |              |  设备ID             |
| ctrlKey  |   必选   |  String  |              |  控制码             |
| subdevTid|   可选   |  String  |              |  子设备ID           |
| ruleName |   必选   |  String  | 长度[1, 128] |  规则名称           |
| condition |  必选   |TriggerCondition|      |  联动触发条件       |
| iftttTasks|  必选   |List<IFTTTTask> |      |  联动执行action列表 |
|  desc    |   可选   | String   |  长度[1, 128]|  联动描述           |
| pushMsg  |   可选   | PushMessage|             |  推送信息          |
| smsPushMsg|   可选  | PushMessage|             |  短信推送信息       |
| iftttType |   必选  | enum     |SECURITY_ALARM,DEPLOY,MORNING_ALARM,DOOR_BELL,CUSTOM|  联动类型   |
| cronExpr |   可选   | cron_expr|              |  启动时间cron表达式 |
| timeZoneOffset| 必选| String   |  [-720, 720] | 时区偏移，请注意javascript中获取的东八区时区偏移为-480，该处需要填写480 |
| setRing  |   可选   | Map      |              |  设置铃声的指令     |
| undoRing |   可选   | UndoRingIFTTT|          |   撤销铃声的联动    |

* TriggerCondition参数说明

|      参数名  | 参数类型 | 取值范围 |    说明  |
|:------------:|:--------:|:--------:|:--------:|
| triggerType  |  enum    | SCHEDULER, REPORT| 触发类型（定时触发，上行指令触发） |
| triggerParams| List<TriggerParam> |          | 触发表达式参数列表 |
|  conDesc     |  String   |  长度[1, 128]|  触发条件描述           |


* TriggerParam 参数说明

|   参数名  | 参数类型 | 取值范围 |    说明  |
|:---------:|:--------:|:--------:|:--------:|
| left      |  String  |          | 条件表达式左边参数 |
| right     |  String  |          | 条件表达式右边参数 |
| operator  |  String  |">", "<", "==", "in", "not in" | 条件表达式运算符|

* UndoRingIFTTT参数说明

|   参数名  | 参数类型 | 取值范围 |    说明  |
|:---------:|:--------:|:--------:|:--------:|
| left      |  String  |          | 条件表达式左边参数 |
| right     |  String  |          | 条件表达式右边参数 |
|operator   |  String  |">", "<", "==" | 条件表达式运算符|
| subTask   |  Map     |          |  联动执行action指令|

* PushMessage 参数说明

|   参数名  | 参数类型 | 取值范围 |    说明  |
|:---------:|:--------:|:--------:|:--------:|
| pushEnable     | boolean |          | 是否推送 |
| isAlarm        | boolean |          | 是否是告警推送（短信推送无该参数）|
| pushTemplateId | String  |          | 推送模板ID|
| pushParams     | List<String>|      | 推送模板参数（名称、执行结果、执行时间已默认添加）|

**参数提交说明**

* 触发条件参数为定时触发，则参数right必须为cron表达式 且云端会对cron表达式触发频率进行校验，触发过于频繁的表达式会被禁止创建(每5分钟触发一次为阈值)

* IFTTTTask参数说明:  
    1> type : 为字符串String 取值 [appSend,appSubSend,appGroupSend,sceneTriggerSend,enableIFTTT,delayTime]  
    2> params: 根据type的不同所传参数也不一样， 对应关系如，上边创建参数所示  

* PushMessage参数说明：
    1> pushParams推送基本参数已添加，故录入时暂时不用添加改参数.
    2> 短信推送目前只支持安防报警类型的联动，其他类型的联动暂不需要添加改参数

**返回**

```
< 201
< {  
    "cronExpr": "0 15 10 * * ? 2016",
    "iftttType": "SECURITY_ALARM",
    "uid": "10894587829",
    "ruleId": "effdd6a0-5b67-486b-8e14-fe6606d10c7d",
    "pushMsg": {
        "alarm": false,
        "pushEnable": true,
        "pushParams": [],
        "pushTemplateId": "00000000012"
    },
    "smsPushMsg": {
        "alarm": false,
        "pushEnable": true,
        "pushParams": [],
        "pushTemplateId": "00000000002"
    },
    "ctrlKey": "e86ea2dead014923b1c04a463372637a",
    "pid": "00000000000",
    "updateTime": null,
    "ruleName": "SECURITY_ALARM_test6",
    "isEnabled": true,
    "iftttTasks": [
        {
            "params": {
                "devTid": "2016-08-17T20-21-42",
                "data": {
                    "cmdId": 1,
                    "power": 1
                },
                "ctrlKey": "54308a73b0b34911a14206b8baaba57f"
            },
            "type": "APPSEND"
        }
    ],
    "devTid": "test-11-36-14",
    "undoRing": {
        "subIftttTask": {
            "params": {
                "devTid": "123123",
                "data": {
                    "cmdId": 1,
                    "key1": "value"
                },
                "ctrlKey": "123123"
            },
            "desc" : "开灯"
            "type": "APPSEND"
        },
        "undoParams": [
            {
                "operator": "==",
                "right": "10",
                "left": "_cmdId"
            }
        ]
    },
    "timeZoneOffset": 480,
    "subDevTid": null,
    "setRing": {
        "cmdId": 1,
        "key1": "value"
    },
    "createTime": 1482313906019,
    "condition": {
        "triggerParams": [
            {
                "operator": "==",
                "right": "10",
                "left": "_cmdId"
            },
            {
                "operator": "==",
                "right": "0 15 10 * * ? 2016",
                "left": "_cloudTime"
            }
        ],
        "triggerType": "REPORT",
        "conDesc" : "触发描述"
    },
    "desc": "第一次测试"
}

```
**调用说明**

* 需console提供接口，通过设备相关信息确认当前联动下是否包含闹铃设备或报警设备，否则无法确认。
* 当前联动如果包含多个触发条件，只支持 以 并且的关系操作
* 联动名称不可重复，且数量不超过20个
* 请注意接口中的参数形式及名称

#### 4.4.2 列举IFTTT规则

支持通过联动名称模糊查询

```
curl -v -X GET \
    -H "Authorization: Bearer {JWT_TOKEN}" \
    -H "Content-Type: application/json" \
    -H "Accept: application/json" \
    "http://user.openapi.hekr.me/rule/iftttRule?devTid=1234&ruleName=xxxx&ruleId=1234&ruleType=CUSTOM"
```

**参数**

| 参数名  | 是否可选 | 参数类型 | 取值范围 |    说明    |
|:-------:|:--------:|:--------:|:--------:|:----------:|
| devTid  |   可选   |  String  |          | 设备ID，多个使用逗号分隔|
| ruleName|   可选   |  String  |          | 联动名称，支持模糊查询 |
| ruleId  |   可选   |  String  |          | 联动ID，多个使用逗号分隔 |
| ruleType|   可选   |  String  |SECURITY_ALARM,DEPLOY,MORNING_ALARM,DOOR_BELL,CUSTOM| 联动类型 |


**返回**

```
< 200
< [
    同4.4.1
    ...
]
```

#### 4.4.3 编辑IFTTT规则

更新为全参提交，参数与4.4.1方法相同

```
curl -v -X PUT \
    -H "Authorization: Bearer {JWT_TOKEN}" \
    -H "Content-Type: application/json" \
    -H "Accept: application/json" \
    "http://user.openapi.hekr.me/rule/iftttRule?ruleId=123"
       -d '{
        "devTid" : "ESP_xxx",
        "ctrlKey" : "xxx",
        "subDevTid": "01234567788"
        "ruleName" : "新建联动",
        "condition" : {
            "triggerType" : REPORT,
            "triggerParams" : [
              {
                "left" : "_cmdId",
                "operator" : "==",
                "right": 10
              },
              {
                "left" : "_cloudTime",
                "operator" : "==",
                "right" : "0 15 10 * * ? 2016"
              },
              ......      
            ]
        },
        "iftttTasks" : [
            {
                "type": "APPSEND",
                "params": {
                    "devTid": "2016-08-17T20-21-42",
                    "ctrlKey": "54308a73b0b34911a14206b8baaba57f",
                    "data": {
                        "raw": "48070200010153"
                    }
                }
            },
            ......
        ],
        "desc" : "xxxxxx"
        "pushMsg": {
            "pushEnable": true,
            "isAlarm": true,
            "pushTemplateId": "00000000012"
        },
        "isEnabled" : true,
        "iftttType":"CUSTOM",
        "cronExpr" : "0 15 10 * * ? 2016",
        "timeZoneOffset" : 480,
        "setRing":{
            "cmdId" : 1,
            "key1" : "value"
        },
        "undo_ring" = {
            "undoParams": [
                {
                    "left": "_cmdId",
                    "operator": "==",
                    "right": 10,
                }
            ],
            "subIftttTask": {
                "type": "appSend",
                "params": {
                    "devTid": "2016-08-17T20-21-42",
                    "ctrlKey": "54308a73b0b34911a14206b8baaba57f",
                    "data": {
                        "raw": "48070200010153"
                    }
                }
            }
        }
    }'

```

**参数**

|  参数名  | 是否可选 | 参数类型 |   取值范围   |     说明            |
|:--------:|:--------:|:--------:|:------------:|:-------------------:|
| devTid   |   必选   |  String  |              |  设备ID             |
| ctrlKey  |   必选   |  String  |              |  控制码             |
| subdevTid|   可选   |  String  |              |  子设备ID           |
| ruleName |   必选   |  String  | 长度[1, 128] |  规则名称           |
| condition  |   必选   |TriggerCondition|      |  联动触发条件       |
| iftttTasks |   必选   |List<IFTTTTask> |      |  联动执行action列表 |
|  desc    |   可选   | String   |  长度[1, 128]|  联动描述           |
| pushMsg  |   可选   | PushMessage|             |  推送信息          |
| smsPushMsg|   可选  | PushMessage|             |  短信推送信息       |
| iftttType |   必选   | enum     |SECURITY_ALARM,DEPLOY,MORNING_ALARM,DOOR_BELL,CUSTOM|  联动类型   |
| cronExpr |   可选   | cron_expr|              |  启动时间cron表达式 |
| timeZoneOffset| 必选 | String   |  [-720, 720] | 时区偏移，请注意javascript中获取的东八区时区偏移为-480，该处需要填写480 |
| setRing  |   可选   | Map      |              |  设置铃声的指令     |
| undoRing |   可选   | UndoRingIFTTT|          |   撤销铃声的联动    |
| enabled  |   可选   | Boolean|          |   联动开关     |


**返回**

```
< 204
< No Content
```

**调用说明**

修改联动是enabled参数 如果没有输入，则不会修改联动当前的isEnabled

#### 4.4.4 删除IFTTT规则

```
curl -v -X DELETE \
    -H "Authorization: Bearer {JWT_TOKEN}" \
    -H "Content-Type: application/json" \
    -H "Accept: application/json" \
    "http://user.openapi.hekr.me/rule/iftttRule?ruleId=xxx,xxx,xxx"
```

**参数**

| 参数名 | 是否可选 | 参数类型 | 取值范围 |           说明           |
|:------:|:--------:|:--------:|:--------:|:------------------------:|
| ruleId |   必选   |  String  |          | 联动ID，多个使用逗号分隔 |


**返回**

```
< 204
< No Content
```

#### 4.4.4 查询联动执行历史记录

```
curl -X GET \
    -H "Authorization: Bearer {JWT_TOKEN}" \
    -H "Accept: application/json" \
    "http://user-openapi.hekr.me/rule/iftttExecuteHistory?page=0&size=20"
```

**参数**

|   参数名    | 是否可选 |   参数类型  |    取值范围   |     说明      |
|:-----------:|:--------:|:-----------:|:-------------:|:-------------:|
|   ruleId    |   可选   |  String     |               |    联动ID     |
|  ruleName   |   可选   |  String     |               |    联动name   |
|   result    |   可选   |  Boolean    | true/false    |    执行结果   |
| startTime   |   必选   |  dateTime   | yyyy-MM-ddTHH:mm:ss.SSS+Z，例如2016-01-01T09:45:00.000+0800 |起始时间|
|  endTime    |   可选   |  dateTime   | yyyy-MM-ddTHH:mm:ss.SSS+Z，例如2016-01-01T09:45:00.000+0800 |结束时间|
|   page      |   可选   |   Int       |  [0, ?]       |    分页参数   |
|   size      |   可选   |   Int       | [1, 20]       |    分页参数   |


**返回**

```
< 201
< {
    "ruleId" ： 123123123123，
    "ruleName" : "安防报警",
    "executeTime" ：1480650897  //执行时间
    "result" ：true  //执行结果
    "uid" : 1234567890
    "desc": "执行成功"
 }

```

**注意**

* 开始时间为必选参数



| 错误码  | 提示信息                                 | 中文释义                   | 可能造成的原因                                |
|:-------:|:-----------------------------------------|:---------------------------|:----------------------------------------------|
| 6400036 | {0}                                      | 联动相关的参数问题         | 联动相关的操作中，参数验证不通过              |
| 6400037 | iftttRule not found                      | 找不到联动信息             | 由于所传参数的原因导致查询不对对应的联动信息  |
| 6400038 | iftttRule name had already exists        | 联动名称已经存在           | 同一用户下联动的名称不能重复                  |
| 6400039 | iftttRule Number is beyond the limit     | 联动数量超限               | 用户下的联动数量不能超过20                    |

