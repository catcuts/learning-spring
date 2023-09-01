package com.example.demo.controller;

import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<Cat> catById(@PathVariable("id") Long id) {
        Optional<Cat> optCat = catRepo.findById(id);
        // 若找到了 Cat 对象，则返回它，否则返回 404
        if (optCat.isPresent()) {
            return new ResponseEntity<>(optCat.get(), HttpStatus.OK/*200*/);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND/*404*/);
        // 注：这里 return new ResponseEntity<>... 省略了泛型参数，因为它们可以从方法的返回值类型中推断出来，也就是 Cat。
        //     也就是说，因为返回值已经指定为 ResponseEntity<Cat>，所以在返回的时候可以省略泛型参数，即 ResponseEntity<>。
    }

    // 这个接口保留用于示例，与 catById 相比，它的返回值类型为 Optional<Cat>，当找不到 Cat 对象时，返回值为 null 且响应码是 200 而不是 404。
    @GetMapping("/_/{id}")
    public Optional<Cat> _catById(@PathVariable("id") Long id) {
        // 注：
        //    这里使用 Optional<Ingredient> 作为返回值类型，
        //    是为了避免返回 null 值，从而避免空指针异常（NullPointerException）。
        //    它会强制调用者在使用返回值之前，先检查返回值是否为空或者做防止返回空值的处理，否则编译器会报错。
        //    例如：
        //      - 调用者可以在返回值上：
        //        - 使用 orElse() 方法来在其为空时使用一个默认值；
        //        - 使用 orElseThrow() 方法来在其为空时抛出一个异常；
        //        - 使用 ifPresent() 方法来在其为非空时执行一个操作等。
        //      - 调用者也可以在 Optional 类上：
        //        - 使用 empty() 方法来创建一个空的 Optional 对象等。
        //    这里无需自行处理空值，原因是：
        //    因为 CatRepository 继承自 PagingAndSortingRepository 又继承自 CrudRepository， 
        //    而 CrudRepository 的 findById() 方法返回的就是 Optional<T> 类型。
        return catRepo.findById(id);
    }

    @PostMapping(consumes = "application/json")  // 对于 POST 请求，并且请求内容类型是 application/json
    @ResponseStatus(HttpStatus.CREATED)          // 返回 201 状态码，表示请求成功而且还创建了新资源
    public Cat postCat(@RequestBody Cat cat) {   // 基于提交的数据创建并保存 Cat 对象
        // 注：@RequestBody 注解表示请求体 body 中的内容会被反序列化为 Cat 对象，从而可以通过 cat 对象获取请求体中的内容。
        return catRepo.save(cat);
    }

}
