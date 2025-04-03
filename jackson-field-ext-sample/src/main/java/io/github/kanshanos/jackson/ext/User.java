package io.github.kanshanos.jackson.ext;

import io.github.kanshanos.jackson.ext.core.annotation.AssembleEnum;
import io.github.kanshanos.jackson.ext.core.annotation.AssembleFunction;
import io.github.kanshanos.jackson.ext.core.annotation.AssembleSpEL;
import io.github.kanshanos.jackson.ext.core.annotation.Mapping;
import io.github.kanshanos.jackson.ext.core.annotation.Type;
import io.github.kanshanos.jackson.ext.core.enums.AssembleType;
import io.github.kanshanos.jackson.ext.core.enums.DataType;
import io.github.kanshanos.jackson.ext.core.handler.function.MaskEmailFunction;
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

    @AssembleEnum(enumClass = CategoryEnum.class, type = AssembleType.MANY_TO_MANY)
    private String category1;

    @AssembleEnum(enumClass = CategoryEnum.class,
            type = AssembleType.MANY_TO_MANY,
            srcType = @Type(dataType = DataType.LIST),
            etxType = @Type(dataType = DataType.MAP),
            mapping = @Mapping(src = "code", ref = "desc"),
            ext = "categoryMapping",
            override = true
    )
    private List<Integer> category2;

    @AssembleSpEL(ext = "ageName", expression = "#value > 18 ? '成年' : '未成年'")
    private int age;

    @AssembleFunction(function = MaskEmailFunction.class, override = true)
    private String email;
}
