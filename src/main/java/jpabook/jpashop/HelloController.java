package jpabook.jpashop;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("hello")   // hello 라는 url이 오면 이 컨트롤러를 호출하겠다.
    public String hello(Model model) { // 여기서 model이란? model에 데이터를 실어서 view에 넘겨주는 역할을 수행하게 된다.
        model.addAttribute("data", "hello!!"); // data라는 key에 hello!라는 value를 넘겨주는 것이다.
        return "hello";  // 화면 이름이 들어가는 곳    -> .html이 생략된 형태
    }
}
