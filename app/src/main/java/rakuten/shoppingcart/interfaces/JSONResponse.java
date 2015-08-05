package rakuten.shoppingcart.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import rakuten.shoppingcart.model.Item;

public interface JSONResponse {
    void publishShoppingCartData(HashMap<String, String> mapShopNames,
                 HashMap<String, ArrayList<Item>> mapShopGroup);
}
