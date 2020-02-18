layui.use(['table', 'jquery', 'layer', 'upload', 'form'], function () {
    var table = layui.table;
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;

    var index = layer.msg('加载中，请稍候', {icon: 16, time: false, shade: 0.8});

    var tableIns = table.render({
        elem: '#bsxt'
        , method: 'post'
        , url: '/selectAllBsxt.do'
        , toolbar: '#toolbar'
        , title: '选题信息表'
        , totalRow: true
        , height: 'full-30'
        , text: {
            none: '无任何选题数据' //默认：无数据。注：该属性为 layIM 2.2.5 开始新增
        }
        , cols: [[
            {type: 'checkbox', fixed: 'left'}
            , {field: 'id', title: 'ID', width: 80, fixed: 'left', unresize: true, sort: true}
            , {field: 'code', title: '编码', width: 180}
            , {field: 'title', title: '题名', width: 280}
            , {field: 'setTeacherNum', title: '出题老师', width: 180}
            , {
                field: 'name', title: '选题学生', width: 180
            }
            , {
                field: 'selectStudentNum', title: '选题学生号', width: 180, templet: function (d) {
                    if (d.selectStudentNum === "") {
                        return "<p style='color: orangered'>暂无学生选该题</p>";
                    } else {
                        return d.selectStudentNum;
                    }
                }
            }
            , {field: 'selectPeople', title: '已选学生', width: 120}
            , {field: 'upperPeople', title: '人数上限', width: 120}
            , {field: 'require', title: '要求', width: 180}
            , {field: 'body', title: '内容', width: 180}
            , {fixed: 'right', title: '操作', align: 'center', toolbar: '#bar', width: 180}
        ]]
        , page: true
        , done: function (res, curr, count) {
            layer.close(index);
        }
    });

    //监听头工具栏事件
    table.on('toolbar(bsxtDo)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id)
            , data = checkStatus.data; //获取选中的数据
        switch (obj.event) {
            case 'search':
                search();
                break;
            case 'accurateSearch':
                accurateSearch();
                break;
            case 'add':
                addNews('add');
                break;
            case 'batchAdd':
                batchAdd();
                break;
            case 'batchExport':
                if (data.length === 0) {
                    batchExport();
                } else {
                    batchCheckExport(data);
                }
                break;
            case 'batchDel':
                if (data.length === 0) {
                    layer.msg('请选择一行');
                } else {
                    var datas = '';
                    for (var i = 0; i < data.length; i++) {
                        datas += "data[]=" + data[i].code + "&";
                    }
                    datas.substring(0, datas.length - 1);
                    layer.msg('<br>是否确认删除<br>' + data.length + '条数据', {
                        time: 20000, //20s后自动关闭
                        btn: ['狠心删除', '考虑考虑'],
                        btn1: function (index) {
                            $.ajax({
                                type: 'POST',
                                url: '/bathDeleteBsxtByNumber.do',
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
    table.on('tool(bsxtDo)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        switch (layEvent) {
            case 'del':
                layer.msg('<br>是否确认删除<br>' + data.title + '', {
                    time: 20000, //20s后自动关闭
                    btn: ['狠心删除', '考虑考虑'],
                    btn1: function (index) {
                        $.ajax({
                            type: 'POST',
                            url: '/deleteBsxtByCode.do',
                            data: 'code=' + data.code,
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
            case 'edit':
                addNews('edit', data);
                break;
            case 'preview':
                preview(data);
                break;
        }
    });

    //添加 修改
    function addNews(edit, data) {
        if (edit === 'edit') {
            var index = layer.open({
                type: 2,
                title: '选题编辑',
                content: '/bsxt/addBsxt.ac',
                shade: 0,
                success: function (layer0, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    if (data) {
                        body.find(".id").val(data.id);
                        body.find(".title").val(data.title);
                        body.find(".setTeacherNum").val(data.setTeacherNum);
                        body.find(".require").val(data.require);
                        body.find(".body").val(data.body);
                    }
                },
                end: function () {
                    tableIns.reload();
                }
            });
            layer.full(index);
        } else if (edit === 'add') {
            var index = layer.open({
                type: 2,
                title: '添加选题',
                content: '/bsxt/addBsxt.ac',
                shade: 0,
                end: function () {
                    tableIns.reload();
                }
            });
            layer.full(index);
        }
    }

    //批量导出
    function batchExport() {
        var index = layer.open({
            type: 2,
            id: 'layer_batchExport',
            title: '导出数据',
            content: 'batchExport.ac',
            shade: 0,
            maxmin: true,
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                body.find(".name").val("选题信息");
            }
        });
        layer.full(index);
    }

    //导出选中的数据
    function batchCheckExport(data) {
        layer.open({
            title: '导出数据',
            content: '<p>导出' + data.length + '条数据?</p>' +
            '<p>数据默认名称为：默认</p>' +
            '<p>默认格式：xlsx</p>',
            btn: ['立即导出', '取消'],
            btnAlign: 'c',
            success: function (index, layer0) {
                form.render('select');
                form.render('radio');
            },
            btn1: function (index, layero) {
                // 3. 执行导出函数，系统会弹出弹框
                parent.LAY_EXCEL.exportExcel({sheet1: data}, '默认.xlsx', 'xlsx', null);
                layer.msg('成功导出' + len + '条数据');
                layer.close(index);
            },
            btn2: function (index, layero) {
                layer.close(index);
            }
        });
    }

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
                    , url: '/selectBsxtByNumberOrName.do'
                    , method: 'get'
                    , text: {
                        none: '没有找到你想要的'
                    }
                    , cols: [[
                        {type: 'checkbox', fixed: 'left'}
                        , {field: 'id', title: 'ID', width: 80, fixed: 'left', unresize: true, sort: true}
                        , {field: 'code', title: '编码', width: 180}
                        , {field: 'title', title: '题名', width: 280}
                        , {field: 'setTeacherNum', title: '出题老师', width: 180}
                        , {
                            field: 'name', title: '选题学生', width: 180
                        }
                        , {
                            field: 'selectStudentNum', title: '选题学生号', width: 180, templet: function (d) {
                                if (d.selectStudentNum === "") {
                                    return "<p style='color: orangered'>暂无学生选该题</p>";
                                } else {
                                    return d.selectStudentNum;
                                }
                            }
                        }
                        , {field: 'selectPeople', title: '已选学生', width: 120}
                        , {field: 'upperPeople', title: '人数上限', width: 120}
                        , {field: 'require', title: '要求', width: 180}
                        , {field: 'body', title: '内容', width: 180}
                        , {fixed: 'right', title: '操作', align: 'center', toolbar: '#bar', width: 180}
                    ]]
                }
            );
        }
    }

    //批量导入
    function batchAdd() {
        var index = layer.open({
            type: 2,
            title: '批量导入',
            content: '/bsxt/batchAdd.ac',
            shade: 0
        });

        layer.full(index);
    }

    //预览
    function preview(data) {
        var content =
            '<p>ID：<span>' + data.id + '</span></p>' +
            '<p>题名：<span>' + data.title + '</span></p>' +
            '<p>编码：<span>' + data.code + '</span></p>' +
            '<p>出题老师：<span>' + data.setTeacherNum + '</span></p>' +
            '<p>选题学生号：<span>' + data.selectStudentNum + '</span></p>' +
            '<p>选题学生：<span>' + data.name + '</span></p>' +
            '<p>已选人数：<span>' + data.selectPeople + '</span></p>' +
            '<p>人数上限：<span>' + data.upperPeople + '</span></p>' +
            '<p>任务要求：<span>' + data.require + '</span></p>' +
            '<p>任务内容：<span>' + data.body + '</span></p>';
        layer.open({
            type: 0,
            title: '选题信息',
            content: content,
            btn: '666',
            btnAlign: 'c',
            btn1: function (index) {
                layer.close(index);
            }
        });
    }
});