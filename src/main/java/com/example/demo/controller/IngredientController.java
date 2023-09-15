package com.example.demo.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Ingredient;
import com.example.demo.repository.IngredientRepository;

@RestController
@RequestMapping(  // 将指定路由的请求映射到相应的处理器并配置其返回内容类型
    path = "api/ingredients",             // 处理 /api/ingredients 的请求
    produces = "application/json"  // 产生 application/json 的内容（Content-Type）
                                   // 也可配置多个产生内容，例如：produces = {"application/json", "application/xml"}
)
@CrossOrigin(origins = "http://localhost:8089")  // 允许来自 http://localhost:8089 的跨域请求
                                                 // 也可配置多个允许跨域的源，例如：origins = {"http://localhost:8089", "http://catcloud:8089"}
public class IngredientController {

  private IngredientRepository ingredientRepo;

  public IngredientController(IngredientRepository ingredientRepo) {
    this.ingredientRepo = ingredientRepo;
  }

  @GetMapping
  public Iterable<Ingredient> allIngredients() {
    return ingredientRepo.findAll();
  }

  @PostMapping(consumes = "application/json")  // 对于 POST 请求，并且请求内容类型是 application/json
  @ResponseStatus(HttpStatus.CREATED)          // 返回 201 状态码，表示请求成功而且还创建了新资源
  public Ingredient postIngredient(@RequestBody Ingredient ingredient) {   // 基于提交的数据创建并保存 Ingredient 对象
      // 注：@RequestBody 注解表示请求体 body 中的内容会被反序列化为 Ingredient 对象，从而可以通过 ingredient 对象获取请求体中的内容。
      return ingredientRepo.save(ingredient);
  }

}
