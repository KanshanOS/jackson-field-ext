package io.github.kanshanos.jackson.ext;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @GetMapping("get")
    public User getUser() {
        String category = "1,2,3";
        return new User().setCategory(1)
                .setCategory1(category)
                .setCategory2(category)
                .setCategory3(category)
                .setCategory4("[" + category + "]")
                .setCategory5(Lists.newArrayList(1, 2, 3))
                .setCategory6(Lists.newArrayList(1, 2, 3))
                .setAge(19)
                .setMobile("15912345678")
                .setEmail("123456@gmail.com");
    }


}
