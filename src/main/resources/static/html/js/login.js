/**
 * 判断是否为手机号
 * @param $poneInput
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

layui.use(['form', 'layer', 'jquery'], function () {
    var form = layui.form;
    var layer = layui.layer;
    var $ = layui.jquery;
    form.on('submit(login)', function (data) {
        if (!isPoneAvailable(data.field.phone)) {
            layer.msg("你输入的手机号有误");
        } else {
            var encode = encodeInp(data.field.phone) + "%%%" + encodeInp(data.field.password);
            encode = encodeURI(encode);
            encode = encodeURI(encode);
            var phone = parseInt(data.field.phone);
            $.ajax({
                type: "POST",
                url: "/login.do",
                data: {
                    'phone': phone,
                    'encode': encode
                },
                dataType: "json",
                success: function (res) {
                    if (res.code === -1) {
                        layer.msg(res.msg);
                    }
                    else if (res.code === 0) {
                        window.location.href = "/index.ac";
                    } else if (res.code === 1) {
                        //配置一个透明的询问框
                        layer.msg('你的账号已在其他地方登陆<br>是否继续登陆操作', {
                            time: 20000, //20s后自动关闭
                            btn: ['继续', '取消'],
                            btn1: function (index, layer0) {
                                $.ajax({
                                    type: "POST",
                                    url: "/loginKick.do",
                                    contentType: "application/json",
                                    data: JSON.stringify(data.field),
                                    dataType: "text",
                                    success: function (res) {
                                        if (res === "success") {
                                            window.location.href = "/index.ac";
                                        }
                                    }
                                });
                            },
                            btn2: function (index, layer0) {
                                layer.close(index);
                            }
                        });
                    }
                }
            });
        }
        return false;
    });
});