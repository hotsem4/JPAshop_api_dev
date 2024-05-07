package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)    // 상속에서 어떤 전략으로 진행할 지 정하는 단계
@DiscriminatorColumn(name = "dtype")  // -> dtype 자동 생성, dtype는 상속받은 값들을 구분하기 위한 열
@Getter @Setter
public abstract class Item {
    @Id @GeneratedValue  // 엔티티의 아이디 표시와 값을 스스로 올리는 어노테이션
    @Column(name = "item_id") // 컬럼의 이름은 item_id라고 한다.
    private Long id;

    private String name;
    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items") // 연관관계의 주인이 아닌 쪽에 사용되는 어노테이션 반대쪽에 어느 속성과 묶을 건지 결정한다.
    private List<Category> categories = new ArrayList<>();

    //==비지니스 로직==//
    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity; // quantity가 들어오면 stockQuantity에 더해준다.
    }
    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;   // quantity가 들어 오면 재고에서 빼서 restStock에 저장
        if (restStock < 0) {  // 남은 재고가 0보다 작으면
            throw new NotEnoughStockException("need more stock");  // NotEnoughStockException 에러 발생
        }
        this.stockQuantity = restStock; // 조건문을 무사히 통과하면 현재 객체의 재고에 restStock을 적용한다.
    }
}
