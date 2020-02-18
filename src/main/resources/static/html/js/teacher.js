layui.use(['table', 'jquery', 'layer', 'upload', 'form'], function () {
    var table = layui.table;
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;

    var index = layer.msg('加载中，请稍候', {icon: 16, time: false, shade: 0.8});

    var tableIns = table.render({
        elem: '#teacher'
        , method: 'post'
        , url: '/selectAllTeacher.do'
        , toolbar: '#toolbar'
        , title: '教师信息表'
        , totalRow: true
        , height: 'full-30'
        , text: {
            none: '无任何教师信息' //默认：无数据。注：该属性为 layIM 2.2.5 开始新增
        }
        , cols: [[
            {type: 'checkbox', fixed: 'left'}
            , {field: 'id', title: 'ID', width: 80, fixed: 'left', unresize: true, sort: true}
            , {field: 'number', title: '教师编号', width: 120}
            , {field: 'name', title: '姓名', width: 120}
            , {field: 'sex', title: '性别', width: 80}
            , {field: 'no', title: '系别', minWidth: 180}
            , {field: 'phone', title: '电话', width: 180}
            , {field: 'email', title: '邮箱', width: 180}
            , {field: 'password', title: '密码', width: 180}
            , {fixed: 'right', title: '操作', align: 'center', toolbar: '#bar', width: 180}
        ]]
        , page: true
        , done: function (res, curr, count) {
            layer.close(index);
        }
    });

    //监听头工具栏事件
    table.on('toolbar(teacherDo)', function (obj) {
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
                        datas += "data[]=" + data[i].number + "&";
                    }
                    datas.substring(0, datas.length - 1);
                    layer.msg('<br>是否确认删除<br>' + data.length + '条数据', {
                        time: 20000, //20s后自动关闭
                        btn: ['狠心删除', '考虑考虑'],
                        btn1: function (index) {
                            $.ajax({
                                type: 'POST',
                                url: '/bathDeleteTeacherByNumber.do',
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
    table.on('tool(teacherDo)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
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
                            url: '/deleteTeacherByNumber.do',
                            data: 'number=' + data.number,
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
                title: '教师编辑',
                content: '/teacher/addTeacher.ac',
                shade: 0,
                success: function (layer0, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    if (data) {
                        body.find(".id").val(data.id);
                        body.find(".number").val(data.number);
                        body.find(".name").val(data.name);
                        body.find(".phone").val(data.phone);
                        body.find(".email").val(data.email);

                        var no = ["电气信息学院", "土木工程学院", "经济管理学院", "人文学院", "建筑管理学院", "艺术设计学院", "建筑学院"];

                        var no_index = 0;
                        for (var i = 0; i < no.length; i++) {
                            if (no[i] === data.no) {
                                no_index = i + 1;
                            }
                        }

                        body.find(".no").find('option[value=0' + no_index + ']').attr("selected", true);

                        body.find(".no").find('option[value=' + data.no + ']').attr("selected", true);

                        if (data.sex === "女") {
                            body.find(".girl").attr("checked", "checked");
                        }

                        //body.find(".layui-form").render();
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
                title: '添加教师',
                content: '/teacher/addTeacher.ac',
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
                body.find(".name").val("教师信息");
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
                // 1. 数组头部新增表头
                data.unshift({
                    number: '教师编号',
                    name: '姓名',
                    sex: '性别',
                    no: '系别',
                    phone: '电话',
                    email: '邮箱',
                    password: '密码'
                });
                // 2. 如果需要调整顺序/或指定导出的数据，请执行梳理函数
                var data1 = parent.LAY_EXCEL.filterExportData(data, [
                    'number', 'name', 'sex', 'no', 'phone', 'email', 'password'
                ]);
                // console.log(data1);
                var len = data.length - 1;
                // 3. 执行导出函数，系统会弹出弹框
                parent.LAY_EXCEL.exportExcel({sheet1: data1}, '默认.xlsx', 'xlsx', null);
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
                    , url: '/selectTeacherByNumberOrName.do'
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

    //精确搜索
    function accurateSearch() {
        var myIframe;
        var index = layer.open({
            type: 2,
            title: '教师精确查询',
            content: '/teacher/accurateSearch.ac',
            shade: 0,
            anim: 2,
            fixed: true,
            success: function (layer0, index) {
                //获取子页面方法
                myIframe = window[layer0.find('iframe')[0]['name']];
            },
            end: function () {
                tableIns.reload({
                    page: {
                        curr: 1
                    },
                    // number name no grade profession classes
                    //contentType: "application/json",
                    where: {
                        number: myIframe.getJson().number,
                        name: myIframe.getJson().name,
                        no: myIframe.getJson().no
                    }
                    , url: '/selectTeacherByAccSearch.do'
                    , method: 'post'
                    , height: 'full-30'
                    , text: {
                        none: '没有找到你想要的'
                    }
                    , cols: [
                        [
                            {type: 'checkbox', fixed: 'left'}
                            , {field: 'id', title: 'ID', width: 80, fixed: 'left', unresize: true, sort: true}
                            , {field: 'number', title: '教师编号', width: 120}
                            , {field: 'name', title: '姓名', width: 120}
                            , {field: 'sex', title: '性别', width: 80}
                            , {field: 'no', title: '系别', width: 180}
                            , {field: 'phone', title: '电话', width: 180}
                            , {field: 'email', title: '邮箱', width: 180}
                            , {field: 'password', title: '密码', width: 120, hide: true}
                            , {fixed: 'right', title: '操作', align: 'center', toolbar: '#bar', width: 180}
                        ]
                    ]
                });
            }
        });
        layer.full(index);
    }

    //批量导入
    function batchAdd() {
        var index = layer.open({
            type: 2,
            title: '批量导入',
            content: '/teacher/batchAdd.ac',
            shade: 0
        });

        layer.full(index);
    }

    //预览
    function preview(data) {
        var content =
            '<p>教师编号：<span>' + data.number + '</span></p>' +
            '<p>姓名：<span>' + data.name + '</span></p>' +
            '<p>性别：<span>' + data.sex + '</span></p>' +
            '<p>院系：<span>' + data.no + '</span></p>' +
            '<p>电话：<span>' + data.phone + '</span></p>' +
            '<p>邮箱：<span>' + data.email + '</span></p>' +
            '<p>密码：<span>' + data.password + '</span></p>';
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