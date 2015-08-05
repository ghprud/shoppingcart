package rakuten.shoppingcart.aynctasks;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import rakuten.shoppingcart.interfaces.JSONResponse;
import rakuten.shoppingcart.model.Item;

public class JsonObjectTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = "JsonObjectTask";
    // JSON Node names
    private static final String TAG_SHOP_CART = "shopCarts";
    private static final String TAG_SHOP_ID = "shopId";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_ITEM_ID = "itemId";
    private static final String TAG_PRICE = "price";
    private static final String TAG_QUANTITY = "quantities";
    private static final String TAG_CURRENCY = "currency";

    private static final String TAG_SHOP_ITEMS_RESOURCES = "resources";
    private static final String TAG_SHOP_ITEMS_NAME = "name";
    private static final String TAG_SHOP_ITEMS_RESPONSE = "response";
    private static final String TAG_SHOP_ITEMS_SHOP_ITEM = "shopItem";
    private static final String TAG_SHOP_ITEMS_IMAGES = "images";

    HashMap<String, HashMap<String, Item>> mapShopItems = new HashMap<>();
    HashMap<String, String> mapShopNames = new HashMap<>();
    HashMap<String, ArrayList<Item>> mapShopGroup = new HashMap<>();

    public JSONResponse jsonResponse = null; //callback interface

    public JsonObjectTask(JSONResponse jsonResponse){
        this.jsonResponse = jsonResponse;
    }

    protected String doInBackground(String... jsonObjects) {
        if (jsonObjects[0] != null && jsonObjects[1] != null) {
            processJSONData(jsonObjects[0], jsonObjects[1]);
        }

        //TO DO: Error checks around the data
        return "success";
    }

    protected void onProgressUpdate(Integer... progress){

    }

    protected void onPreExecute(){

    }

    protected void onPostExecute(String result){

        if (result == "success"){
            jsonResponse.publishShoppingCartData(mapShopNames, mapShopGroup);
        }
        else
        {
            Log.w(TAG, "json response publish failure");
        }

    }

    private void processJSONData(String cartData, String itemData){

        try{
            //cart data
            if (cartData != null){
                processCartData(new JSONObject(cartData));
            }
            //item data
            if (itemData != null){
                processItemData(new JSONObject(itemData));
            }

            if (mapShopNames != null) {
                genShopGroup(new ArrayList<String>(mapShopNames.keySet()));
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void genShopGroup(ArrayList<String> listShopIds){
        for (String shopId : listShopIds){
            mapShopGroup.put(shopId, new ArrayList<Item>(mapShopItems.get(shopId).values()));
        }
    }

    private void processCartData(JSONObject jsonObjShoppingCart){


        try {
            JSONArray shoppingCart = jsonObjShoppingCart.getJSONArray(TAG_SHOP_CART);

            for (int i = 0; i < shoppingCart.length(); i++) {
                JSONObject c = shoppingCart.getJSONObject(i);

                //shopping cart data
                String shopId = c.getString(TAG_SHOP_ID);
                JSONArray items = c.getJSONArray(TAG_ITEMS);

                HashMap<String, Item> mapItems = new HashMap<>();
                for (int j = 0; j < items.length(); j++) {
                    JSONObject d = items.getJSONObject(j);

                    String price = d.getString(TAG_PRICE);
                    String quantity = d.getString(TAG_QUANTITY);
                    String currency = d.getString(TAG_CURRENCY);
                    String itemId = d.getString(TAG_ITEM_ID);

                    //create a map of items...use them later to update the name and the image..
                    Item item = new Item(Integer.parseInt(quantity),
                            Double.parseDouble(price), currency);

                    mapItems.put(itemId, item);

                    //create a list of items..
                    mapShopItems.put(shopId, mapItems);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void processItemData(JSONObject jsonObjShoppingItems){

        try {
            JSONArray shoppingItemsInfo = jsonObjShoppingItems.getJSONArray(TAG_SHOP_ITEMS_RESOURCES);

            //for loop to get item related info from item_data.json
            //use the already populated items above to update the name and the image of the item
            for (int k = 0; k < shoppingItemsInfo.length(); k++){
                JSONObject c = shoppingItemsInfo.getJSONObject(k);

                if (c.getString("name").equals("shop/get")){
                    //get the shop names..need the shop names to dispaly the items based on the names
                    String shopName = c.getJSONObject("response").getJSONObject("shop").getJSONObject("name").getString("value");
                    String shopId = c.getJSONObject("response").getJSONObject("shop").getString("shopId");

                    mapShopNames.put(shopId, shopName);
                }

                if (c.getString("name").equals("shop-item/get")){
                    String itemName = c.getJSONObject("response").getJSONObject("shopItem").getString("name");
                    JSONObject image = c.getJSONObject("response").getJSONObject("shopItem")
                            .getJSONArray("images").getJSONObject(0);
                    String itemId = c.getJSONObject("response").getJSONObject("shopItem").getString("itemId");
                    String shopId = c.getJSONObject("response").getJSONObject("shopItem").getString("shopId");


                    String imageUrl = image.getString("location");

                    Bitmap bitmap = null;

                    try {
                        bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());

                        HashMap<String, Item> mapTemp =  mapShopItems.get(shopId);
                        Item item = mapTemp.get(itemId);

                        item.setItemName(itemName);
                        item.setItemImage(bitmap);

                        mapTemp.put(itemId, item);
                        mapShopItems.put(shopId, mapTemp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}


