package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.")  // name이 비어있다면? hasError에서 name으로 반환
    private String name;

    private String city;
    private String street;
    private String zipcode;

    // 왜 엔티티를 그냥 쓰지 않고 따로 memberForm을 만들었는가?
    // 엔티티를 그냥 쓰면 편할지는 몰라도 엔티티와 Form은 분리시키는 것이 좋다.
    // 사용가능한 건 개인이 혼자 작은 프로젝트할 때 뿐.
}
