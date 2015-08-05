package rakuten.shoppingcart.view;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import rakuten.shoppingcart.R;
import rakuten.shoppingcart.adapters.ShoppingCartAdapter;
import rakuten.shoppingcart.aynctasks.JsonObjectTask;
import rakuten.shoppingcart.interfaces.JSONResponse;
import rakuten.shoppingcart.model.Item;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCartActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    final static String TAG = "ShoppingCartActivity";



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);


        String cartData = null;
        String itemData = null;

        try {
            cartData = loadShoppingData(getAssets().open("cart_data.json"));
            itemData = loadShoppingData(getAssets().open("item_data.json"));

            JsonObjectTask jsonObjectTask = new JsonObjectTask(new JSONResponse() {
                @Override
                public void publishShoppingCartData(HashMap<String, String> mapShopNames, HashMap<String, ArrayList<Item>> mapShopGroup) {
                    Log.w(TAG, "processing the shopping cart output");

                    final ExpandableListAdapter expListAdapter;
                    ExpandableListView expListView;
                    expListView = (ExpandableListView) findViewById(R.id.shopCart);

                    expListAdapter = new ShoppingCartAdapter(getApplicationContext(), mapShopGroup,
                            mapShopNames, new ArrayList<String>(mapShopNames.keySet()));

                    expListView.setAdapter(expListAdapter);
                }
            });

            jsonObjectTask.execute(cartData, itemData);

            //new JsonObjectTask(this).execute(cartData, itemData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private String loadShoppingData(InputStream inputStream){
        String json = null;
        try {

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}
