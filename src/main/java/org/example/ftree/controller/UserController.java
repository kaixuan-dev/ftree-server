package org.example.ftree.controller;

import org.example.ftree.model.vo.ResultEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/users")
@RestController
public class UserController {

    /**
     * 我的信息
     *
     * @return
     */
    @GetMapping("/me")
    public ResultEntity<Map<String, Object>> me() {
        return ResultEntity.success("登录成功", new HashMap<>() {{
            put("roles", List.of("admin"));
            put("username", "zengkaixuan");
        }});
    }

}
