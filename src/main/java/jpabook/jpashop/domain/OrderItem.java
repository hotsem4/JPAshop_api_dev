package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // Protected 형태의 기본 생성자 생성
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private int orderPrice;    // 주문 당시 가격

    private int count;    // 주문 수량

//    protected OrderItem() {  // protected으로 하게 되면 외부에서 new OrderItem을 해서 객체가 생성되는 것을 막아주는 역할을 한다.
//    }

    //==생성 메소드==//
    // item에 가격 있지 않나요?
    // 있지만 쿠폰 등의 이유로 가격이 변경될 수 있기 때문에 분리해야한다.
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비지니스 로직==//
    public void cancel() {
        getItem().addStock(count);
    }

    //==조회 로직==//

    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();   // 수량이 1이 아닐수 있기 때문에 getCount를 곱하는 것이다.
    }
}
