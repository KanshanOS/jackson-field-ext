package com.kanshan.jackson.ext;

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
        return new User(
                1,
                category,
                category,
                category,
                "[" + category + "]",
                Lists.newArrayList(1, 2, 3),
                19,
                "15979991234",
                "123456@gmail.com"
        );
    }


}
