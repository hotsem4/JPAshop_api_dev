package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 3;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수와 정확해야 한다.",1,getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000* orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 7, book.getStockQuantity());
    }

//    @Test
//    public void 주문취소() throws Exception {
//        // given
//        Member member = new Member();
//        member.setName("회원B");
//        member.setAddress(new Address("서울","시골","1233-212"));
//        em.persist(member);
//
//        Item book = new Book();
//        book.setName("서울쥐 대모험");
//        book.setPrice(10000);
//        book.setStockQuantity(100);
//        em.persist(book);
//
//        int orderCount = 10;
//        // when
//        Long orderId = orderService.order(member.getId(), book.getId(), orderCount); // -> 수량 90개
//        orderService.cancelOrder(orderId);
//        // then
//        Order order = orderRepository.findOne(orderId);
//        book.getStockQuantity();
//        assertEquals();
//
//    }

@Test
public void 주문취소() throws Exception {
    // given
    Member member = createMember();
    Book item = createBook("시골 jpa", 10000, 10);

    int orderCount = 2;

    Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
    // when
    assertEquals("주문 전 상품의 재고 감소 테스트", 8, item.getStockQuantity());
    orderService.cancelOrder(orderId);
    // then
    Order getOrder = orderRepository.findOne(orderId);
    assertEquals("주문 취소시 상태는 CANCLE", OrderStatus.CANCEL, getOrder.getStatus());
    assertEquals("주문이 취소된 상품은 그 만큼 재고가 증가해야 한다.",10, item.getStockQuantity());

}

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 11;
        // when
        orderService.order(member.getId(), item.getId(), orderCount);
        // then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }



    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","경기","123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}