package io.github.kanshanos.jackson.ext;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {
    @GetMapping("get")
    public User get() {
        List<Integer> categoryCodes = Lists.newArrayList(
                CategoryEnum.平板.getCode(),
                CategoryEnum.手机.getCode(),
                CategoryEnum.手表.getCode()
        );
        String categoryCodeStringArray = categoryCodes.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return new User().setCategory(1)
                .setCategory1(categoryCodeStringArray)
                .setCategory2(categoryCodes)
                .setAge(19)
                .setEmail("123456@gmail.com");
    }


}
