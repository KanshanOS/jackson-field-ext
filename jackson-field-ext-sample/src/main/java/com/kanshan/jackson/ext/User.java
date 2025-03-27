package com.kanshan.jackson.ext;

import com.kanshan.jackson.ext.core.annotation.AssembleEnum;
import com.kanshan.jackson.ext.core.annotation.AssembleFunction;
import com.kanshan.jackson.ext.core.annotation.AssembleSpEL;
import com.kanshan.jackson.ext.core.annotation.Mapping;
import com.kanshan.jackson.ext.core.annotation.Type;
import com.kanshan.jackson.ext.core.enums.AssembleType;
import com.kanshan.jackson.ext.core.enums.DataType;
import com.kanshan.jackson.ext.core.handler.function.MaskEmailFunction;
import com.kanshan.jackson.ext.core.handler.function.MaskPhoneFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {


    @AssembleEnum(enumClass = CategoryEnum.class)
    private Integer category;

    @AssembleEnum(enumClass = CategoryEnum.class,
            type = AssembleType.MANY_TO_MANY,
            etxType = @Type(dataType = DataType.JSON_ARRAY)
    )
    private String category1;

    @AssembleEnum(enumClass = CategoryEnum.class,
            type = AssembleType.MANY_TO_MANY,
            etxType = @Type(dataType = DataType.LIST)
    )
    private String category2;

    @AssembleEnum(enumClass = CategoryEnum.class,
            type = AssembleType.MANY_TO_MANY,
            etxType = @Type(dataType = DataType.STRING_ARRAY),
            mapping = @Mapping(ref = "alias", ext = "categoryName")
    )
    private String category3;
    @AssembleEnum(enumClass = CategoryEnum.class,
            type = AssembleType.MANY_TO_MANY,
            srcType = @Type(dataType = DataType.JSON_ARRAY),
            etxType = @Type(dataType = DataType.JSON_ARRAY)
    )
    private String category4;

    @AssembleEnum(enumClass = CategoryEnum.class,
            type = AssembleType.MANY_TO_MANY,
            srcType = @Type(dataType = DataType.LIST),
            etxType = @Type(dataType = DataType.JSON_ARRAY)
    )
    private List<Integer> category5;

    @AssembleSpEL(ext = "ageName", expression = "#value > 18 ? '成年' : '未成年'")
    private int age;

    @AssembleFunction(function = MaskPhoneFunction.class)
    private String mobile;

    @AssembleFunction(ext = "newEmail", useExt = true, function = MaskEmailFunction.class)
    private String email;
}
