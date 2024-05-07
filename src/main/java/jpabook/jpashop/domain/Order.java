package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 빈 생성자를 protected로 만들어주는 것
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;      //  [ORDER, CANCLE]

    //==연관관계 메서드==//
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    // 주문을 생성하기 위해선 order만 생성한다고 되는 것이 아니고 배달, 품목 등을 넣어줘야 하는 복잡한 과정을 거친다.
    // 그래서 이런 생성 메서드를 쓰는 것이 좋다.
    //
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        // order 객체 생성
        order.setMember(member); // order에 Member 생성
        order.setDelivery(delivery);  // Delivery 생성
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem); // orderItem을 order에 넣는 작업
        }
        order.setStatus(OrderStatus.ORDER); //order의 상태를 ORDER로 변경
        order.setOrderDate(LocalDateTime.now());  // 날씨 객체에 넣기
        return order;   // 다 만든 order 반환
    }
    /* 이거 왜써?
    * 내가 new Order()하면 밖에서 set, set, set 하면서 값을 넣어줘야 하는데 이렇게 만들면 컴파일 오류로 내가 실수로 넣지
    * 않은 값을 알 수 있고 캡슐화를 활용한 응집도 측면에서도 장점으로 볼 수 있어.
    * 그리고 확장성 측면에서 나중에 Order 객체 생성 메소드를 변경해야 할 경우, 생성 메소드를 사용하면 해당 메소드만 수정하면 되기 때문에
    * 아주 개이득이지.
    * */

    //==비지니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if(delivery.getOrderStatus() == DeliveryStatus.COMP) {  // 주문 취소를 했는데 배달 상태가 COMP라면
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다."); // 불가 문구 반환
        }
        this.setStatus(OrderStatus.CANCEL);  // 주문 상태 CANCEL로 변경
        for (OrderItem orderItem : orderItems) { // 객체 안에 있는 orderItems 객체 돌면서
            // 고객이 한번 주문할 때 한 주문에 여러 상품이 있을 수 있기 때문에 다 취소해줘야 한다.
            orderItem.cancel();  // orderItem.cancel 함수 반환
        }
    }
    // this 를 쓰는 것은 개인의 취향이다.

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
