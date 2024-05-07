package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {   // merge와 동작 방식이 같다.
        // merge를 사용할 경우 모든 속성이 변경되는 불상사가 발생할 수도 있다.
        // 바꾸고 싶은 값이 name 뿐이라면 merge를 했을 때 나머지도 다 null이 되어 버린다.
        // merge는 쓰지 않는다. 라고 생각하자.
        Item findItem = itemRepository.findOne(itemId);  // 이것은 영속 상태이다.
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
