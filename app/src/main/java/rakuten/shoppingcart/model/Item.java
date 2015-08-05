package rakuten.shoppingcart.model;

import android.graphics.Bitmap;


public class Item {
    private Integer itemquantity;
    private Double itemprice;
    private Bitmap itemImage;
    private String itemName;
    private String itemCurrency;

    public Item(Integer itemquantity, Double itemprice, String itemCurrency){
        this.itemquantity = itemquantity;
        this.itemprice = itemprice;
        this.itemCurrency = itemCurrency;
    }
    public String getItemCurrency() {
        return itemCurrency;
    }

    public void setItemCurrency(String itemCurrency) {
        this.itemCurrency = itemCurrency;
    }
    
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemquantity() {
        return itemquantity;
    }

    public void setItemquantity(Integer itemquantity) {
        this.itemquantity = itemquantity;
    }

    public Bitmap getItemImage() {
        return itemImage;
    }

    public void setItemImage(Bitmap itemImage) {
        this.itemImage = itemImage;
    }

    public Double getItemprice() {
        return itemprice;
    }

    public void setItemprice(Double itemprice) {
        this.itemprice = itemprice;
    }

}
