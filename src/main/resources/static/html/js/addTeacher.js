layui.use(['table', 'jquery', 'layer', 'laydate', 'form', 'upload'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;

    $('#cancel').on('click', function () {
        parent.layer.close(parent.layer.index);
    });

    var isOne = true;

    //姓名验证
    function isChn(str) {
        var reg = /^[\u4E00-\u9FA5]+$/;
        if (reg.test(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为手机号
     * @param poneInput
     * @returns {boolean}
     */
    function isPoneAvailable(poneInput) {
        var myreg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
        if (!myreg.test(poneInput)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 校验邮箱
     * @returns {boolean}
     */
    //邮箱验证
    function isEmail(email) {
        return email.search(/^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\.(?:com|cn)$/) !== -1;
    }


    //提交数据
    form.on('submit(formDemo)', function (data) {

        //校验学号
        if (data.field.number.length !== 10) {
            layer.msg("学号不符合规范");
            return false;
        }

        //校验中文姓名
        if (!isChn(data.field.name)) {
            layer.msg("请输入中文名");
            return false;
        }

        if (data.field.name.length <= 1) {
            layer.msg("名字长度不能小于1");
            return false;
        }

        //校验手机号
        if (!isPoneAvailable(data.field.phone) || data.field.phone.length !== 11) {
            layer.msg("手机号不符合规范");
            return false;
        }

        //校验邮箱
        if (!isEmail(data.field.email)) {
            layer.msg("邮箱格式不对");
            return false;
        }

        var no = ["电气信息学院", "土木工程学院", "经济管理学院", "人文学院", "建筑管理学院", "艺术设计学院", "建筑学院"];

        for (var i = 0; i < no.length; i++) {
            if (i === (data.field.no.substring(1) - 1)) {
                data.field.no = no[i];
            }
        }

        if (isOne) {
            isOne = false;
            $.ajax({
                type: 'POST',
                async: true,
                url: '/insertTeacher.do',
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