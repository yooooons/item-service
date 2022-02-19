package hello.itemservice.web.basic;

import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

/*//    @Autowired //생략가능
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }*/

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);//모델에서 key,value저장하고 템플릿에서는 key값으로 찾고 추출한다.
        return "basic/item";
    }

    //선호하는방시 methodMapping 을 이용한 구분
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }
//    @PostMapping("/add")
    //form의 name으로 받아온다
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model
    ) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item,
                            Model model    //*@ModelAttribute("item")을 사용하면 자동화로 인해 생략 가능
    ) {

        itemRepository.save(item);
        //model.addAttribute("item", item);    *@ModelAttribute("item")을 사용해서 이름을 item으로 한 model.addAttribute 자동으로 만들어 준다. 따라서 생략해도 가능하다
        return "basic/item";
    }


//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item //클래스의 첫글자 소문자 바꿔서 naming 한다
    ) {

        itemRepository.save(item);
        return "basic/item";
    }


//    @PostMapping("/add")
    public String addItemV4(Item item
    ) {

        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV5(Item item
    ) {
        itemRepository.save(item);
        return "redirect:/basic/items/"+item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item,
                            RedirectAttributes redirectAttributes
    ) {
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId());
        redirectAttributes.addAttribute("status", true);//치환이 되고 없는것은 나머지는 쿼리parameter 로 넘어간다.
        return "redirect:/basic/items/{itemId}";
    }



    @GetMapping("/{itemId}/edit")
    public  String editForm(@PathVariable Long itemId,Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }
    //prg패턴 post/redirect/get방식 -  새로고침오류 방지
/*"redirect:/basic/items/" + item.getId() redirect에서 +item.getId() 처럼 URL에 변수를
더해서 사용하는 것은 URL 인코딩이 안되기 때문에 위험하다. RedirectAttributes 를
사용하자.*/


    @PostMapping("/{itemId}/edit")
    public  String edit(@PathVariable Long itemId,@ModelAttribute Item item) {
        itemRepository.update(itemId,item);
        return "redirect:/basic/items/{itemId}";
    }



    /*
    테스트용 데이터추가
    * */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
