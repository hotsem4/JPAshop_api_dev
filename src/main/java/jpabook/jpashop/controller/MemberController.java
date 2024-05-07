package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller  // 이 자바 코드가 spring의 Controller라는 것을 인식 시켜서 자동으로 빈 관리를 하게 해주는 어노테이션
@RequiredArgsConstructor  // final이 있는 객체의 생성자를 생성하고 자동 Autowired를 수행하는 어노테이션
public class MemberController {
    private final MemberService memberService;   // 해당 코드는 MemberService를 필요로 하기 때문에 외부에 있는 MemberService에 대한 의존성 주입코드
    // 이를 통해 MemberController는 필요허ㅏㄴ 비즈니스 로직을 memberService를 통해 수행할 수 있게 된다.
    // final 키워드를 붙여 필드가 수정될 수 없다는 것을 명시적으로 표현한 것이다.
    @GetMapping("members/new")  // url이 members/new로 들어오면!
    public String createForm(Model model) {  // Model이란? controller에서 view로 갈 때 데이터를 실어 넘기는 역할을 수행함
        model.addAttribute("memberForm", new MemberForm());
        /*
         * model의 역할
         * 1. 데이터 전달: 컨트롤러에서 처리한 데이터를 뷰에 전달하기 위해 사용한다. 예를 들어, 데이터베이스에서 조회한 정보를 사용자에게 보여주기 위해
         * 뷰로 데이터를 전송할 수 있다.
         * 2. 뷰 템플릿과의 통신: 뷰 템플릿(예: Thymeleaf, JSP 등)에서는 Model에 저장된 데이터를 사용하여 동적으로 웹 페이지를 생성한다.
         * Model에 저장된 데이터는 HTML 태그 내에서 사용되어 사용자에게 보여지는 컨텐츠를 구성한다.
         * 이 코드는 Model 객체에 memberForm이라는 이름의 MemberForm 객체를 추가한다.
         * 이렇게 Model에 추가된 데이터는 컨트롤러에서 뷰(view)로 넘어갈 때 사용자 입력 품을 구성하게 된다.
         */
        return "members/createMemberForm"; // members 폴더 안에 createMemberForm 파일을 찾아 반환한다.
    }

    @PostMapping("/members/new")  // post형태로 members/new가 넘어온다면
    public String create(@Valid MemberForm form, BindingResult result){
        // BindingResult가 있다면 오류가 result에 담겨서 밑에 코드가 실행이 된다.

        if (result.hasErrors()) { // result에 값이 존재한다면
            return "members/createMemberForm";   // result를 담아서 createMemberForm을 반환하라
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode()); //  form에 입력한 주소값을 이용해 address 객체 생성
        Member member = new Member(); // member 객체 생성
        member.setName(form.getName());  // form 객체에 있는 name을 member에 적용
        member.setAddress(address);   // 위에서 만든 address 객체를 member에 주입

        memberService.join(member);   // memberService에서 만든 join 메서드를 통해 member 등록
        return "redirect:/";  // 페이지를 다시 /로 반환
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
