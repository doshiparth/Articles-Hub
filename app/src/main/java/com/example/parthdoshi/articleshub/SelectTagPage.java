package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.neel.articleshubapi.restapi.beans.TagDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class SelectTagPage extends AppCompatActivity {
    //ListView lv_select;
    SwipeMenuListView lv_selected;
    //MaterialSearchView searchView;
    EditText userSearchText;
    Button userSearchButton;
    SharedPreferences sharedPref;
    String token = null;
    String userName = null;
    Boolean NO_SELECTION_FLAG = true;
    Boolean TAG_ALREADY_PRESENT = false;

    List<String> listSelected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_select_tag_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        Calligrapher calligrapher = new Calligrapher(SelectTagPage.this);
        calligrapher.setFont(SelectTagPage.this, FixedVars.FONT_NAME, true);

        //Code to display and manage the search view in the toolbar
        //Toolbar toolbar = (Toolbar)findViewById(R.id.search_toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Tag Search Bar");
        //toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        //Getting user details from the shared preferences file
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");

        userSearchText = (EditText) findViewById(R.id.txt_user_search);
        userSearchButton = (Button) findViewById(R.id.btn_user_search);

        //ArrayAdapter<String> sourceAdapter;
        //ArrayAdapter<String> foundAdapter;
        //ArrayAdapter<String> selectedAdapter;

        Log.i("Select Tag Page Token", token);

        /*
        //Initializing Tags
        listSource.add("tutorial");
        listSource.add("story");
        listSource.add("fashion");
        listSource.add("science");
        listSource.add("game");
        listSource.add("smartphone");
        listSource.add("philosophy");
        listSource.add("programming");
        listSource.add("study");
        listSource.add("news");
        listSource.add("movie");
        listSource.add("ai");
        listSource.add("future");
        listSource.add("book");


        for (String str:listSource) {
            System.out.println(str);
        }
        */

        lv_selected = (SwipeMenuListView) findViewById(R.id.select_page_listview);
        lv_selected.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        lv_selected.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        //lv_selected.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ListDataAdapter selectedAdapter = new ListDataAdapter();
        lv_selected.setAdapter(selectedAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                SwipeMenuItem removeItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                removeItem.setBackground(new ColorDrawable(Color.rgb(0x30, 0xB1, 0xF5)));
                // set item width
                removeItem.setWidth(dp2px(90));
                // set a icon
                removeItem.setIcon(R.drawable.ic_action_discard);
                // add to menu
                menu.addMenuItem(removeItem);
                // create "delete" item
                SwipeMenuItem cancelItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                cancelItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                cancelItem.setWidth(dp2px(90));
                // set a icon
                cancelItem.setIcon(R.drawable.ic_action_cancel);
                // add to menu
                menu.addMenuItem(cancelItem);
            }
        };

        // set creator
        lv_selected.setMenuCreator(creator);

        lv_selected.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        //Remove the selected tag from the list
                        Toast.makeText(SelectTagPage.this, "" + listSelected.get(position) + " deleted", Toast.LENGTH_LONG).show();
                        listSelected.remove(listSelected.get(position));
                        lv_selected.setAdapter(selectedAdapter);
                        break;
                    case 1:
                        lv_selected.setAdapter(selectedAdapter);
                        //Cancel action
                        //Toast.makeText(SelectTagPage.this, "Tag deleted", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        lv_selected.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {

            }

            @Override
            public void onMenuClose(int position) {

            }
        });

        lv_selected.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });


        userSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usersTag = userSearchText.getText().toString().trim().toLowerCase();
                RequestTask<TagDetail> tagRead = new RequestTask<>(TagDetail.class, CONTENT_TYPE_JSON);
                tagRead.execute(FixedVars.BASE_URL + "/tag/" + usersTag);
                // initiate waiting logic
                TagDetail tag = tagRead.getObj();
                // terminate waiting logic
                HttpStatus status = tagRead.getHttpStatus();

                if (status == HttpStatus.OK && tag != null) {
                    //To check if the tag user has selected is already present in his selection list
                    for (String addedTag : listSelected) {
                        if ((usersTag.equals(addedTag))) {
                            Toast.makeText(SelectTagPage.this, "You already selected this tag!!", Toast.LENGTH_LONG).show();
                            TAG_ALREADY_PRESENT = true;
                        }
                    }
                    //If the tag is not already present and is available in the Database, ENTER it into the list
                    if (!TAG_ALREADY_PRESENT) {
                        listSelected.add(tag.getTagName());
                        userSearchText.setText("");
                        ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
                        lv_selected.setAdapter(selectedAdapter);
                        NO_SELECTION_FLAG = false;
                    }
                } else if (usersTag.equals(""))
                    Toast.makeText(SelectTagPage.this, "Please Enter something!!!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(SelectTagPage.this, "The entered tag does not exist in the database.... Please try another tag", Toast.LENGTH_LONG).show();
            }
        });
/*
        lv_selected.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Remove the selected tag from the list
                listSelected.remove(listSelected.get(i));

                ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
                lv_selected.setAdapter(selectedAdapter);
            }
        });
*/

        //lv_select = (ListView)findViewById(R.id.select_page_display_listview);
        //lv_select.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice, listSource);
        //ArrayAdapter<String> foundAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listFound);

        //lv_select.setAdapter(sourceAdapter);

        /*
        searchView = (MaterialSearchView)findViewById(R.id.select_tag_page_search_view);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                lv_selected = (ListView) findViewById(R.id.select_page_listview);
                ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
                lv_selected.setAdapter(selectedAdapter);

                //ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(SelectTagPage.this,android.R.layout.simple_list_item_multiple_choice, listSource);
                //If Search View closed, list view will return default
                //lv_select = (ListView)findViewById(R.id.select_page_display_listview);
                //lv_select.setAdapter(sourceAdapter);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String usersTag = query.toLowerCase();
                RequestTask<TagDetail> tagRead=new RequestTask<>(TagDetail.class,CONTENT_TYPE_JSON);
                tagRead.execute(FixedVars.BASE_URL+"/tag/"+usersTag);
                // initiate waiting logic
                TagDetail tag=tagRead.getObj();
                // terminate waiting logic
                HttpStatus status = tagRead.getHttpStatus();

                if(status==HttpStatus.OK && tag!=null) {
                    listSelected.add(tag.getTagName());
                    NO_SELECTION_FLAG = false;
                    ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
                    lv_selected.setAdapter(selectedAdapter);
                }
                else
                    Toast.makeText(SelectTagPage.this, "The entered tag does not exist in the database.... Please try another tag", Toast.LENGTH_LONG).show();
                return true;
            }

            //Check if the entered Text in the search box matches with our list of tags
            //If it does load those items in the new ArrayAdapter object
            //else display the adapter with the old list
            @Override
            public boolean onQueryTextChange(String newText) {
                for (String str:listSource) {
                    System.out.println(str);
                }

                if(newText != null && !newText.isEmpty()){
                    for(String item:listSource){
                        if(item.contains(newText))
                            listFound.add(item);
                    }
                    ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
                    ArrayAdapter<String> foundAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listFound);
                    lv_select.setAdapter(foundAdapter);
                    lv_selected.setAdapter(selectedAdapter);
                }
                else{
                    //if search text is null
                    //return default
                    ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(SelectTagPage.this,android.R.layout.simple_list_item_multiple_choice, listSource);
                    ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
                    lv_select.setAdapter(sourceAdapter);
                    lv_selected.setAdapter(selectedAdapter);
                }
                return true;
            }

        });
        */
        /*
        lv_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                NO_SELECTION_FLAG = false;
                //for(int j=0 ; j<listSelected.size() ; j++) {
                //    if (!(listSource.get(i).equalsIgnoreCase(listSelected.get(j)))) {
                for (String str:listSource) {
                    System.out.println(str);
                }
                        listSelected.add(listSource.get(i));
                        listSource.remove((listSource.get(i)));
                        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(SelectTagPage.this,android.R.layout.simple_list_item_multiple_choice, listSource);
                        ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
                        //ArrayAdapter<String> foundAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listFound);
                        lv_select.setAdapter(sourceAdapter);
                        lv_selected.setAdapter(selectedAdapter);
                //        return;
                //   }
                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        */

        /*
        lv_selected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_select_tag_page_search_view,menu);
        //MenuItem item = menu.findItem(R.id.select_tag_page_action_search);
        //searchView.setMenuItem(item);
        return true;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void goToHome(View v) {
        //Converting ArrayList to list of strings
        //String selectedTags[] = new String[listSelected.size()];
        //for(int j =0;j<listSelected.size();j++){
        //    selectedTags[j] = listSelected.get(j);
        //}


        //Sending user's favorite tags to the server

        if (!listSelected.isEmpty()) {
            for (String str : listSelected) {
                System.out.println("Selected tags");
                System.out.println(str);
            }
            TagDetail[] tagDetails = new TagDetail[listSelected.size()];
            for (int i = 0; i < listSelected.size(); i++) {
                TagDetail tagDetail = new TagDetail();
                tagDetail.setTagName(listSelected.get(i));
                tagDetails[i] = tagDetail;
            }
            AddRequestTask<String, TagDetail[]> rt = new AddRequestTask<String, TagDetail[]>(String.class,
                    tagDetails, HttpMethod.POST, CONTENT_TYPE_JSON,
                    HeaderTools.makeAuth(token));
            rt.execute(FixedVars.BASE_URL + "/user/" + userName + "/favorite-tags");
            // initiate waiting logic
            rt.getObj();
            // terminate waiting logic
            HttpStatus status = rt.getHttpStatus();
            if (status == HttpStatus.OK)
                Toast.makeText(SelectTagPage.this, "Tags saved", Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent(SelectTagPage.this, HomePage.class);
            SelectTagPage.this.startActivity(myIntent);
            startActivity(myIntent);
            finish();
        } else {
            Toast.makeText(SelectTagPage.this, "You need to select atleast one tag to go ahead", Toast.LENGTH_LONG).show();
        }
    }

    private class ListDataAdapter extends BaseAdapter {

        ViewHolder holder;

        @Override
        public int getCount() {
            return listSelected.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.list_item_tags, parent);
                holder.mTextview = (TextView) convertView.findViewById(R.id.txt_list_tag_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mTextview.setText(listSelected.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView mTextview;
        }
    }
}
