layui.use(['table', 'jquery', 'layer', 'laydate', 'form', 'upload'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;

    //系别
    var no = null;

    //联动监听
    //监听系别
    form.on('select(no)', function (data) {
        if (data.value !== "") {
            //根据系判断不同专业
            loadProfession();

            $('#grade').removeAttr("disabled");
            no = data.value;
            form.render('select');
        } else {
            $('#grade').attr("disabled", true);
            $('#profession').attr("disabled", true);
            $('#classes').attr("disabled", true);
            form.render('select');
        }
    });

    //监听年级
    form.on('select(grade)', function (data) {
        if (data.value !== "") {

            // //根据系判断不同专业
            // loadProfession();

            $('#profession').removeAttr("disabled");
            form.render('select');
        } else {
            $('#profession').attr("disabled", true);
            $('#classes').attr("disabled", true);
            form.render('select');
        }
    });

    //监听专业
    form.on('select(profession)', function (data) {
        if (data.value !== "") {
            $('#classes').removeAttr("disabled");
            form.render('select');
        } else {
            $('#classes').attr("disabled", true);
            form.render('select');
        }
    });

    //重置
    $('#reset').on('click', function () {
        $('#grade').attr("disabled", true);
        $('#profession').attr("disabled", true);
        $('#classes').attr("disabled", true);
        form.render('select');
    });

    function loadProfession() {
        //加载专业数据
        $.ajax({
            type: 'get',
            url: "/static/profession.json",
            dataType: 'json',
            success: function (res) {
                $('#profession').empty();
                $("#profession").append(new Option("请选择专业", ""));
                for (var i = 0; i < res.length; i++) {
                    if (res[i].dm.substring(0, 2) === no) {
                        var option = document.createElement("option");
                        option.value = res[i].dm;
                        option.text = res[i].dmmc;
                        $('#profession').append(option);
                        form.render();
                    }
                }
            }
        });
    }

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
        if (data.field.number.length !== 8 || data.field.number.substring(0, 2) !== data.field.grade.substring(2)) {
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

        $.ajax({
            async: true,
            type: 'get',
            url: "/static/profession.json",
            dataType: 'json',
            success: function (res) {
                for (var i = 0; i < res.length; i++) {
                    if (res[i].dm === data.field.profession) {
                        data.field.profession = res[i].dmmc;
                        break;
                    }
                }


                if (isOne) {
                    isOne = false;
                    $.ajax({
                        type: 'POST',
                        async: true,
                        url: '/insertStudent.do',
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

            }
        });

        return false;
    });

});