layui.use(['table', 'jquery', 'layer', 'laydate', 'form', 'upload'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;


    // {
    //     "query": "word",   //要查询的值
    //     "suggestions": ["value1"，"value2", "value3"]   //匹配项
    // }
    //输入框联想
    $('#setTeacherNum').autocomplete({
        serviceUrl: "/selectTeacherByName.do",
        type: 'POST',
        deferRequestBy: 500
    });

    $('#cancel').on('click', function () {
        parent.layer.close(parent.layer.index);
    });

    var isOne = true;

    //提交数据
    form.on('submit(formDemo)', function (data) {
        var setTeacherNum = data.field.setTeacherNum.split("(");
        if (setTeacherNum.length <= 1) {
            layer.msg("出题老师必须是已存在的才行");
            return false;
        } else {
            data.field.setTeacherNum = setTeacherNum[1].replace(")","");
        }

        console.log(JSON.stringify(data.field));

        if (isOne) {
            isOne = false;
            $.ajax({
                type: 'POST',
                async: true,
                url: '/insertBsxt.do',
                contentType: "application/json",
                data: JSON.stringify(data.field),
                dataType: 'json',
                success: function (res) {
                    if (res.code > 0) {
                        parent.layer.close(parent.layer.index);
                        parent.layer.msg(res.msg);
                    } else {
                        layer.open({
                            title: 'error',
                            content: res.msg,
                            btn: 'ok',
                            btnAlign: 'c',
                            btn1: function (index, layer0) {
                                layer.close(index);
                            }
                        });
                        isOne = true;
                    }
                }
            })
        } else {
            layer.msg("请不要重复提交");
        }

        return false;
    });

});