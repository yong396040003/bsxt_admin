layui.use(['table', 'jquery', 'layer', 'upload', 'form'], function () {
    var table = layui.table;
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;

    var index = layer.msg('加载中，请稍候', {icon: 16, time: false, shade: 0.8});

    var tableIns = table.render({
        elem: '#comment'
        , method: 'post'
        , url: '/selectAllComment.do'
        , toolbar: '#toolbar'
        , title: '评论区'
        , totalRow: true
        , height: 'full-30'
        , text: {
            none: '无任何评论区信息' //默认：无数据。注：该属性为 layIM 2.2.5 开始新增
        }
        , cols: [[
            {type: 'checkbox', fixed: 'left'}
            , {field: 'id', title: 'ID', width: 80, fixed: 'left', unresize: true, sort: true}
            , {field: 'praise', title: '赞', width: 80}
            , {field: 'number', title: '学号/教师号', width: 120}
            , {field: 'name', title: '姓名', width: 80}
            , {field: 'msg', title: '评论消息', minWidth: 180}
            , {field: 'time', title: '时间', width: 180}
            , {fixed: 'right', title: '操作', align: 'center', toolbar: '#bar', width: 160}
        ]]
        , page: true
        , done: function (res, curr, count) {
            layer.close(index);
        }
    });

    //监听头工具栏事件
    table.on('toolbar(commentDo)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id)
            , data = checkStatus.data; //获取选中的数据
        switch (obj.event) {
            case 'search':
                search();
                break;
            case 'batchDel':
                if (data.length === 0) {
                    layer.msg('请选择一行');
                } else {
                    var datas = '';
                    for (var i = 0; i < data.length; i++) {
                        datas += "data[]=" + data[i].time + "&";
                    }
                    datas.substring(0, datas.length - 1);
                    layer.msg('<br>是否确认删除<br>' + data.length + '条数据', {
                        time: 20000, //20s后自动关闭
                        btn: ['狠心删除', '考虑考虑'],
                        btn1: function (index) {
                            $.ajax({
                                type: 'POST',
                                url: '/bathDeleteCommentByTime.do',
                                data: datas,
                                dataType: 'json',
                                success: function (res) {
                                    if (res.code > 0) {
                                        layer.msg(res.msg);
                                        tableIns.reload();
                                    } else {
                                        layer.msg(res.msg);
                                    }
                                }
                            });
                            layer.close(index);
                        },
                        btn2: function (index) {
                            layer.close(index);
                        }
                    });
                }
                break;
            case 'refresh':
                tableIns.reload();
                break;
        }
    });

    //监听工具条
    table.on('tool(commentDo)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        switch (layEvent) {
            case 'del':
                layer.msg('<br>是否确认删除<br>' + data.name + '', {
                    time: 20000, //20s后自动关闭
                    btn: ['狠心删除', '考虑考虑'],
                    btn1: function (index) {
                        $.ajax({
                            type: 'POST',
                            url: '/deleteCommentByTime.do',
                            data: 'number=' + data.time,
                            dataType: 'json',
                            success: function (res) {
                                if (res.code > 0) {
                                    obj.del();
                                    layer.msg(res.msg);
                                    //tableIns.reload();
                                } else {
                                    layer.msg(res.msg);
                                }
                            }
                        });
                        layer.close(index);
                    },
                    btn2: function (index) {
                        layer.close(index);
                    }
                });
                break;
            case 'preview':
                preview(data);
                break;
        }
    });

    //搜索
    function search() {
        var input = $('#search_input').val();
        if (input === '') {
            parent.layer.msg("请输入搜索关键字");
        } else {

            function show(date) {
                var input1 = input;
                var i = date.indexOf(input1);
                var j = date.indexOf(input1.toLocaleUpperCase());
                var k = date.indexOf(input1.toLocaleLowerCase());

                console.log(i + "  " + j + "   " + k);

                if (i !== -1 || j !== -1 || k !== -1) {
                    if (j !== -1) {
                        i = j;
                    } else if (k !== -1) {
                        i = k;
                    }
                    var s = date.substring(0, i);
                    var e = date.substring(i + input.length, date.length);
                    return s + '<span style="color: red">' + date.substring(i, i + input.length)
                        + '</span>' + e;
                } else {
                    return date;
                }
            }

            tableIns.reload({
                    page: {
                        curr: 1
                    },
                    where: {
                        search: input
                    }
                    , url: '/selectCommentByNumberOrName.do'
                    , method: 'get'
                    , height: 'full-30'
                    , text: {
                        none: '没有找到你想要的'
                    }
                    , cols: [
                        [
                            {type: 'checkbox', fixed: 'left'}
                            , {field: 'id', title: 'ID', width: 80, fixed: 'left', unresize: true, sort: true}
                            , {
                            field: 'number', title: '教师编号', width: 120, templet: function (d) {
                                var num = d.number.toString();
                                return show(num);
                            }
                        }
                            , {
                            field: 'name', title: '姓名', width: 120, templet: function (d) {
                                return show(d.name);
                            }
                        }
                            , {field: 'sex', title: '性别', width: 80}
                            , {field: 'no', title: '系别', width: 180}
                            , {field: 'phone', title: '电话', width: 180}
                            , {field: 'email', title: '邮箱', width: 180}
                            , {field: 'password', title: '密码', width: 120, hide: true}
                            , {fixed: 'right', title: '操作', align: 'center', toolbar: '#bar', width: 180}
                        ]
                    ]
                }
            );
        }
    }

    //预览
    function preview(data) {
        var content =
            '<p>选题编码：<span>' + data.code + '</span></p>' +
            '<p>赞：<span>' + data.praise + '</span></p>' +
            '<p>学号/教师号：<span>' + data.number + '</span></p>' +
            '<p>姓名：<span>' + data.name + '</span></p>' +
            '<p>评论内容：<span>' + data.msg + '</span></p>' +
            '<p>邮箱：<span>' + data.email + '</span></p>' +
            '<p>时间：<span>' + data.time + '</span></p>';
        layer.open({
            type: 0,
            title: '教师信息',
            content: content,
            btn: '666',
            btnAlign: 'c',
            btn1: function (index) {
                layer.close(index);
            }
        });
    }
});