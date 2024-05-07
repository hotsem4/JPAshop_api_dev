package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany // 예제에서는 사용하지만 실무에서는 절대 사용하지 마라. 유지보수가 불가능할 것이다.
    @JoinTable(
        name = "category_item",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
        // 다대다 관계를 구현할 때 필요한 조인 테이블에서, 현재 엔티티와 연결된 반대편 엔티티의 외래 키를 지정하는 데 사용된다.
    )
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)  // FetchType.LAZY를 하게 될 경우 지연로딩으로 설졍하여 즉시로딩의 단점을 상쇄
    @JoinColumn(name = "parent_id") // 연관관계의 주인일 때 많이 사용하는 어노테이션으로 테이블에서 열의 이름을 지정
    private Category parent;

    @OneToMany(mappedBy = "parent") // 위에 있는 연관관계의 종속성에 부여하는 어노테이션
    private List<Category> child = new ArrayList<>();

    //--연관관계
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
