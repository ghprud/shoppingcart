package rakuten.shoppingcart.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import rakuten.shoppingcart.R;
import rakuten.shoppingcart.model.Item;

public class ShoppingCartAdapter extends BaseExpandableListAdapter{

    HashMap<String, ArrayList<Item>> mapShopItems = new HashMap<>();
    HashMap<String, String> mapShopNames = new HashMap<>();
    ArrayList<String> shopIds = new ArrayList<>();

    Context context;

    public ShoppingCartAdapter(Context context, HashMap<String, ArrayList<Item>> mapShopItems,
                               HashMap<String, String> mapShopNames, ArrayList<String> shopIds) {
        this.context = context;
        this.mapShopItems = mapShopItems;
        this.mapShopNames = mapShopNames;
        this.shopIds = shopIds;
    }

    @Override
    public int getGroupCount() {
        return shopIds.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mapShopItems.get(shopIds.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mapShopNames.get(shopIds.get(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mapShopItems.get(shopIds.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String shopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.shop);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(shopName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Item item = (Item) getChild(groupPosition, childPosition);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        ImageView itemImage = (ImageView) convertView.findViewById(R.id.itemImage);

        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        TextView itemCount = (TextView) convertView.findViewById(R.id.itemCount);
        TextView itemPrice = (TextView) convertView.findViewById(R.id.itemPrice);

        itemImage.setImageBitmap(item.getItemImage());
        itemName.setText("Item Name: " + item.getItemName());
        itemCount.setText("Item Quantity: " + item.getItemquantity().toString());
        itemPrice.setText("Item Price: " + item.getItemprice().toString() + " " + item.getItemCurrency().toString());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
