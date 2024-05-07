package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
    private Long id;   // 아이디를 받는 이유? id를 이용한 상품 수정이 있기 때문이다.
    // item과 관련된 데이터
    private String title;
    private String name;
    private int price;
    private int stockQuantity;
    // Book와 관련된 데이터들
    private String author;
    private String isbn;
}
