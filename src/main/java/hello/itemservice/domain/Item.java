package hello.itemservice.domain;

import lombok.Data;

@Data
public class Item {
    //NULL이 들어갈 수 있게 Long Integer 사용
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
