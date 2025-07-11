package com.zhiyou.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.PrimitiveIterator;

@Data
@ApiModel("修改密码时传递的模型")
public class EmployeeChangePasswdDTO implements Serializable {

    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty("新密码")
    @NotNull(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty("旧密码")
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;

}
