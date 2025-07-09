package com.zhiyou.skycommon.Result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    // 总记录数
    private Integer total;
    // 记录集合
    private List<T> records;
}
