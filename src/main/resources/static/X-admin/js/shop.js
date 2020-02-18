var url; //地址
var method; //请求方式
var category; //小说类型
var sort; //排序
var status; //类型(完本)

var getParam = function(url_, method_, category_, sort_, status_) {
    url = url_;
    method = method_;
    category = category_;
    sort = sort_;
    status = status_;
};

layui.use(['table', 'jquery', 'layer', 'laydate', 'upload', 'form'], function() {
    var table = layui.table;
    var $ = layui.jquery;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var form = layui.form;

    //var index = layer.msg('加载中，请稍候', {icon: 16, time: false, shade: 0.8});

    //日期
    laydate.render({
        elem: '#date'
    });

    //渲染表格
    var tableIns = table.render({
        elem: '#book',
        page: true,
        toolbar: '#toolbar',
        height: "full-30",
        method: method,
        url: url,
        // where: {
        //     category: category,
        //     sort: sort,
        //     status: status
        // },
        limit: 10,
        limits: [10, 20, 50, 100],
        text: {
            none: '无商品信息' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
        },
        cols: [
            [
                { type: 'checkbox', fixed: 'left' },
                { field: 'ID', title: 'ID', align: 'center', fixed: true },
                { field: 'Code', title: 'Code', align: 'center', fixed: true },
                { field: 'Name', title: 'Name', align: 'center', fixed: true },
                { field: 'Origin', title: 'Origin', align: 'center', fixed: true },
                { field: 'IconUrl', title: 'IconUrl', align: 'center', fixed: true },
                { field: 'Description', title: 'Description', align: 'center', fixed: true },
                { field: 'CreateTime', title: 'CreateTime', align: 'center', fixed: true },
                { fixed: 'right', title: '操作', align: 'center', width: 160, toolbar: '#bar' }
            ]
        ],
        done: function(res, curr, count) {
            // layer.close(index);
        }
    });

    //监听头工具栏事件
    table.on('toolbar(Book)', function(obj) {
        var checkStatus = table.checkStatus(obj.config.id),
            data = checkStatus.data; //获取选中的数据
        switch (obj.event) {
            case 'search':
                search();
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
                        btn1: function(index) {
                            $.ajax({
                                type: 'POST',
                                url: 'bathDeleteBookById.do',
                                data: datas,
                                dataType: 'json',
                                success: function(res) {
                                    if (res.code > 0) {
                                        layer.msg(res.detail);
                                        tableIns.reload();
                                    } else {
                                        layer.msg(res.detail);
                                    }
                                }
                            });
                            layer.close(index);
                        },
                        btn2: function(index) {
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
    table.on('tool(Book)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        switch (layEvent) {
            case 'del':
                layer.msg('<br>是否确认删除<br>' + data.Code + '', {
                    time: 20000, //20s后自动关闭
                    btn: ['狠心删除', '考虑考虑'],
                    btn1: function(index) {
                        $.ajax({
                            type: 'get',
                            url: 'http://localhost:8888/deleteProduct?code=' + data.Code,
                            dataType: 'json',
                            success: function(res) {
                                if (res.status == 1) {
                                    obj.del();
                                    layer.msg(res.msg);
                                    tableIns.reload();
                                } else {
                                    layer.msg(res.msg);
                                }
                            }
                        });
                        layer.close(index);
                    },
                    btn2: function(index) {
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
        if (edit == 'edit') {
            var index = layer.open({
                type: 2,
                title: '商品修改',
                content: 'editShop.html?code=' + data.Code,
                shade: 0,
                success: function(layer0, index) {
                    // alert(index + "测试" + data.Code);
                    var body = layer.getChildFrame('body', index);
                    if (data) {
                        // { field: 'Code', title: 'Code', align: 'center', fixed: true },
                        // { field: 'Name', title: 'Name', align: 'center', fixed: true },
                        // { field: 'Origin', title: 'Origin', align: 'center', fixed: true },
                        // { field: 'IconUrl', title: 'IconUrl', align: 'center', fixed: true },
                        // { field: 'Description', title: 'Description', align: 'center', fixed: true },
                        // { field: 'CreateTime', title: 'CreateTime', align: 'center', fixed: true },
                        // { fixed: 'right', title: '操作', align: 'center', width: 160, toolbar: '#bar' }
                        alert("测试" + data);
                        console.log(data.Code);
                        body.find(".code").val(data.Code);
                        body.find(".name").val(data.Name);
                        body.find(".origin").val(data.Origin);
                        body.find(".iconUrl").val(data.IconUrl);
                        body.find(".description").val(data.Description);
                        body.find(".createTime").val(data.CreateTime);
                    }
                },
                end: function() {
                    tableIns.reload();
                }
            });
            layer.full(index);
        } else if (edit == 'add') {
            var index = layer.open({
                type: 2,
                title: '添加商品',
                content: 'addShop.html',
                shade: 0,
                end: function() {
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
            content: 'batchExport.ac?url=' + url + '&category=' + category + '&sort=' + sort + '&status=' + status + '',
            shade: 0,
            maxmin: true
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
            success: function(index, layer0) {
                form.render('select');
                form.render('radio');
            },
            btn1: function(index, layero) {
                // 1. 数组头部新增表头
                data.unshift({
                    number: '书号',
                    name: '书名',
                    img: '图片地址',
                    author: '作者',
                    status: '状态',
                    wordNumber: '字数',
                    data: '最近更新',
                    category: '小说类型',
                    collection: '收藏',
                    totalHits: '总点击',
                    monthlyClicks: '月点击',
                    weeklyClicks: '周点击',
                    totalRecommendedNumber: '总推荐',
                    monthlyRecommendedNumber: '月推荐',
                    weekRecommendedNumber: '周推荐',
                    synopsis: '简介'
                });
                // 2. 如果需要调整顺序，请执行梳理函数
                var data1 = parent.LAY_EXCEL.filterExportData(data, [
                    'number', 'name', 'img', 'author', 'status', 'wordNumber', 'data', 'category',
                    'collection', 'totalHits', 'monthlyClicks', 'weeklyClicks', 'totalRecommendedNumber',
                    'monthlyRecommendedNumber', 'weekRecommendedNumber', 'synopsis'
                ]);
                // console.log(data1);
                var len = data.length - 1;
                // 3. 执行导出函数，系统会弹出弹框
                parent.LAY_EXCEL.exportExcel({ sheet1: data1 }, '默认.xlsx', 'xlsx', null);
                layer.msg('成功导出' + len + '条数据');
                layer.close(index);
            },
            btn2: function(index, layero) {
                layer.close(index);
            }
        });
    }

    //搜索
    function search() {
        var input = $('#search_input').val();
        if (input == '') {
            parent.layer.msg("请输入搜索关键字");
        } else {
            tableIns.reload({
                page: {
                    curr: 1
                },
                url: 'http://localhost:8888/getProduct?code=' + input,
                method: 'get',
                text: {
                    none: '没有找到你想要的'
                },
                cols: [
                    [
                        { type: 'checkbox', fixed: 'left' },
                        { field: 'ID', title: 'ID', align: 'center', fixed: true },
                        { field: 'Code', title: 'Code', align: 'center', fixed: true },
                        { field: 'Name', title: 'Name', align: 'center', fixed: true },
                        { field: 'Origin', title: 'Origin', align: 'center', fixed: true },
                        { field: 'IconUrl', title: 'IconUrl', align: 'center', fixed: true },
                        { field: 'Description', title: 'Description', align: 'center', fixed: true },
                        { field: 'CreateTime', title: 'CreateTime', align: 'center', fixed: true },
                        { fixed: 'right', title: '操作', align: 'center', width: 160, toolbar: '#bar' }
                    ]
                ]
            });
        }
    }

    //批量导入
    function batchAdd() {
        var index = layer.open({
            type: 2,
            title: '批量导入',
            content: 'batchAdd.ac',
            shade: 0
        });

        layer.full(index);
    };


    //预览
    function preview(data) {
        var index = layer.open({
            type: 2,
            title: '图书预览',
            content: 'preview.ac?number=' + data.number + '',
            shade: 0,
            success: function(layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                if (data) {
                    var wordNumber;
                    var num = data.wordNumber.substring(0, data.wordNumber.length - 1);
                    if (num > 10000) {
                        var n = parseInt(num / 10000);
                        wordNumber = n + "万字";
                    } else {
                        wordNumber = num + "字";
                    }

                    body.find(".img").attr("src", data.img);
                    body.find(".name")[0].innerHTML = data.name;
                    body.find(".author")[0].innerHTML = data.author;
                    body.find(".category")[0].innerHTML = data.category;
                    body.find(".status")[0].innerHTML = data.status;
                    body.find(".collection")[0].innerHTML = data.collection;
                    body.find(".wordNumber")[0].innerHTML = wordNumber;
                    body.find(".data")[0].innerHTML = data.data;

                    body.find(".totalHits")[0].innerHTML = data.totalHits;
                    body.find(".monthlyClicks")[0].innerHTML = data.monthlyClicks;
                    body.find(".weeklyClicks")[0].innerHTML = data.weeklyClicks;
                    body.find(".totalRecommendedNumber")[0].innerHTML = data.totalRecommendedNumber;
                    body.find(".monthlyRecommendedNumber")[0].innerHTML = data.monthlyRecommendedNumber;
                    body.find(".weekRecommendedNumber")[0].innerHTML = data.weekRecommendedNumber;
                    body.find(".synopsis")[0].innerHTML = data.synopsis;

                }
            }
        });
        layer.full(index);
    }
});