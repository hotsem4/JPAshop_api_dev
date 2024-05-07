package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller  // spring에서 제공하는 이 코드가 controller라고 알려주는 코드
@Slf4j    // log를 찍기 위해 제공하는 라이브러리를 위한 어노테이션
public class HomeController {

    @RequestMapping("/")     // 요청 메핑 /가 들어오면
    public String home(){    // return type이 String인 home 인스턴스
        log.info("home controller");    // log home controller 기록용
        return "home";   // url / 가 요청되면 home.html을 찾아서 반환하라.
    }
}
