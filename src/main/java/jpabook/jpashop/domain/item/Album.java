package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
// jpa에서 상속 구조를 갖는 엔티티들을 하나의 테이블로 저장할 때 사용하는 단일 테이블 전략에서, 각 각의 하위 클래스를 구별하기 위해 사용하는 식별자를 지정하는데 사용되는 어노테이션
@Getter
@Setter
public class Album extends Item {
    private String artist;
    private String etc;
}
