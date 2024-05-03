package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor

public class OrderRepository {
    private final EntityManager em;
    // EntityManager란?
    // spring은 이를 통해 데이터베이스의 CRUD(Create, Read, Update, Delete) 작업을 수행한다.
    // 객체와 데이터베이스 사이의 모든 상호작용을 관리하며, 엔티티의 생명주기를 관리한다.

    // final 키워드
    // 변수가 한 번 할당된 후에는 재할당 될 수 없음을 의미한다. 이는 EntityManager인스턴스가 초기화된 후에
    // 변경되지 않도록 보장하여, 클래스의 안정성과 예측 가능성을 높인다.
    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch){
        String jpql = "select o from Order o join o.member m";
        return em.createQuery(jpql,Order.class)
                .setMaxResults(1000)   // 최대 1000건 호출
                .getResultList();
    }

//    public List<Order> findAll(OrderSearch orderSearch){
    // 이건 orderSearch에 값이 있다는 가정이고
//        return em.createQuery("select o from Order o join o.member m" +
//                " where o.status = :status " +
//                " and m.name like :name", Order.class)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name",orderSearch.getMemberName())
//                .setMaxResults(1000)   // 최대 1000건 호출
//                .getResultList();
//    }

    /**
     * JPA Criteria
     *  이 코드는 유지 보수가 거의 안된다고 보면 된다. 그리고 엄청 복잡하다.
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);

        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null){
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"),"%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }
}
