package com.yong.bsxt_admin.service;

import com.alibaba.fastjson.JSONObject;
import com.yong.bsxt_admin.pojo.Bsxt;

import java.util.List;

/**
 * Description:
 * Date: 13:39 2019/12/25
 *
 * @author yong
 * @see
 */
public interface BsxtService {

    /**
     * 分页查询所有bsxt
     * @param page
     * @param limit
     * @return
     */
    JSONObject selectAllBsxt(int page, int limit);

    /**
     * 插入一条bsxt数据
     * @param bsxt
     * @return
     */
    JSONObject insertBsxt(Bsxt bsxt);

    /**
     * 修改一名bsxt信息
     * @param bsxt
     * @return
     */
    JSONObject updateBsxt(Bsxt bsxt);

    /**
     * 模糊搜索可根据number/name
     * @param search
     * @param page
     * @param limit
     * @return
     */
    JSONObject selectBsxtByNumberOrName(String search, int page, int limit);

    /**
     * 精确搜索可根据
     * number name no grade profession classes
     *
     * @param page
     * @param limit
     * @param bsxt
     * @return
     */
    JSONObject selectBsxtByAccSearch(int page, int limit, Bsxt bsxt);

    /**
     * 根据code删除bsxt数据
     *
     * @param code
     * @return
     */
    JSONObject deleteBsxtByNumber(String code);

    /**
     * 根据number批量删除bsxt信息
     *
     * @param data
     * @return
     */
    JSONObject bathDeleteBsxtByNumber(String[] data);

    /**
     * 批量导入数据
     *
     * @param bsxtList
     * @return
     */
    JSONObject uploadBsxtExcel(List<Bsxt> bsxtList);
}
