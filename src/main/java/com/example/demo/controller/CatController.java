package com.example.demo.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Cat;
import com.example.demo.repository.CatRepository;

@RestController
@RequestMapping(  // 将指定路由的请求映射到相应的处理器并配置其返回内容类型
    path = "api/cats",             // 处理 /api/cats 的请求
    produces = {"application/json", "application/xml"}  // 产生 application/json 的内容（Content-Type）
                                   // 也可配置多个产生内容，例如：produces = {"application/json", "application/xml"}
)
@CrossOrigin(origins = "http://localhost:8089")  // 允许来自 http://localhost:8089 的跨域请求
                                                 // 也可配置多个允许跨域的源，例如：origins = {"http://localhost:8089", "http://catcloud:8089"}
public class CatController {
    
    private CatRepository catRepo;

    public CatController(CatRepository catRepo) {
        this.catRepo = catRepo;
    }

    @GetMapping(params = "recent")       // 对于 GET 请求，并且携有 recent 参数，也就是 /api/cats?recent
    public Iterable<Cat> recentCats() {  // 获取最近n个 Cats，也就是按倒序排列的前n个（每页n个的分页第一页<页码为0>）
        PageRequest page = PageRequest.of(
            0/*pageNo*/, 12/*pageSize*/, Sort.by("createdAt").descending()
        );
        return catRepo.findAll(page).getContent();
    }

}
