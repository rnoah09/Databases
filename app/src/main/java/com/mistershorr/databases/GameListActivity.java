package com.mistershorr.databases;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class GameListActivity extends AppCompatActivity {

    private List<Game> fullGameList;
    private ListView gameListView;
    private FloatingActionButton actionButtonAdd;
    private EditText editTextSearch;
    private EditText editTextPlayerSearch;
    public static final String EXTRA_GAME = "game";
    public static final String EXTRA_CREATE = "";
    private GameAdapter gameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        wireWidgets();
        setListeners();

        backendlessUpdate();

        setListeners();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerForContextMenu(gameListView);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameListActivity.this);

        builder.setMessage("Return to Login?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GameListActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.create().show();
        return true;
    }

    @Override
    public void onBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder(GameListActivity.this);

            builder.setMessage("Leave without saving?")
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent targetIntent = new Intent(GameListActivity.this, LoginActivity.class);
                            startActivity(targetIntent);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        backendlessUpdate();
    }

    public void backendlessUpdate(){
        String ownerId = Backendless.UserService.CurrentUser().getObjectId();
        String whereClause = "ownerId = '"+ ownerId + "'";

        // if (editTextSearch.getText().toString().isEmpty() == false){
        //  whereClause = whereClause + "and name LIKE '%"+ editTextSearch.getText().toString() + "%'";
        // }
        // if (editTextPlayerSearch.getText().toString().isEmpty() == false){
        // whereClause = whereClause + " and minPlayer <= "+ editTextPlayerSearch.getText().toString() + " and maxPlayer >=" + editTextPlayerSearch.getText().toString();
        // }

        Log.d("whereClause", whereClause);

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        Backendless.Data.of(Game.class).find(queryBuilder, new AsyncCallback<List<Game>>() {
            @Override
            public void handleResponse(List<Game> foundGames) {
                fullGameList = new ArrayList<>(foundGames);
                gameAdapter = new GameAdapter(foundGames, GameListActivity.this);
                gameListView.setAdapter(gameAdapter);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    public void wireWidgets(){
        gameListView = findViewById(R.id.listview_gamelist_gamelist);
        actionButtonAdd = findViewById(R.id.fab_gamelist_add);
        editTextPlayerSearch = findViewById(R.id.edittext_gamelist_playercount);
        editTextSearch = findViewById(R.id.edittext_gamelist_search);
    }

    public void setListeners(){
        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent targetIntent = new Intent(GameListActivity.this, GameDetailActivity.class);
                targetIntent.putExtra(EXTRA_GAME, fullGameList.get(i));
                targetIntent.putExtra(EXTRA_CREATE, false);
                startActivity(targetIntent);
            }
        });
        actionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent targetIntent = new Intent(GameListActivity.this, GameDetailActivity.class);
                targetIntent.putExtra(EXTRA_CREATE, true);
                startActivity(targetIntent);
            }
        });

        // TODO: SEARCH
        // TODO: SEARCH
        // TODO: SEARCH

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                gameAdapter.getFilter().filter(s);
//                if(!editTextSearch.getText().toString().isEmpty()){
//                    gameAdapter.gamesList.clear();
//                    for(Game g: fullGameList){
//                        if(g.getName().contains(s.toString())){
//                            gameAdapter.gamesList.add(g);
//                            Log.d("SEARCHING", "onTextChanged: " + g.toString() + " found");
//                        }
//                    }
//                    gameAdapter.notifyDataSetChanged();
//                    // backendlessUpdate();
//                }
//                else{
//                    gameAdapter.gamesList = new ArrayList<>(fullGameList);
//                    gameAdapter.notifyDataSetChanged();
//                }
//                Log.d("SEARCHING", "onTextChanged: " + gameAdapter.gamesList.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editTextPlayerSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                gameAdapter.getFilter().filter(s);

//                if (!editTextPlayerSearch.getText().toString().isEmpty()){
//                    gameAdapter.gamesList.clear();
//                    for(Game g: fullGameList){
//                        if(g.getMinPlayer() <= parseInt(s.toString()) && g.getMaxPlayer() >= parseInt(s.toString())){
//                            gameAdapter.gamesList.add(g);
//                        }
//                        gameAdapter.notifyDataSetChanged();
//                    }
//                    //  backendlessUpdate();
//                }
//                else{
//                    gameAdapter.gamesList = new ArrayList<Game>(fullGameList);
//                    gameAdapter.notifyDataSetChanged();
//                }
                // loop through the full list and add to the gameAdapter.gamesList
                // add from the full list
                // notify data set changed

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private class GameAdapter extends ArrayAdapter<Game> implements Filterable {
        private List<Game> gamesList;

        private List<Game> originalData = null;
        private List<Game>filteredData = null;
        private LayoutInflater mInflater;
        private ItemFilter mFilter = new ItemFilter();

        public GameAdapter(List<Game> gamesList, Context context){
            super(GameListActivity.this,-1, gamesList);
            this.gamesList = gamesList;
            this.filteredData = gamesList ;
            this.originalData = gamesList ;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return filteredData.size();
        }

        public Game getItem(int position) {
            return gamesList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.item_game, parent, false);
            }

            TextView textViewName = convertView.findViewById(R.id.textview_gameitem_name);
            TextView textViewPlayerCount = convertView.findViewById(R.id.textview_gameitem_playercount);
            RatingBar ratingBar = convertView.findViewById(R.id.ratingbar_gameitem_rating);

            Game game = gamesList.get(position);
            textViewName.setText(game.getName());
            if (game.getMaxPlayer() == game.getMinPlayer()){
                textViewPlayerCount.setText("" + game.getMinPlayer());
            }
            else{
                textViewPlayerCount.setText(game.getMinPlayer() + "-" + game.getMaxPlayer());
            }
            float rating = (float) game.getRating();
            ratingBar.setRating(rating);
            ratingBar.setIsIndicator(true);

            return convertView;
        }

        public Filter getFilter() {
            return mFilter;
        }

        private class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                final List<Game> list = originalData;

                int count = list.size();
                final ArrayList<Game> nlist = new ArrayList<Game>(count);

                Game filterableGame ;

                if (!editTextSearch.getText().toString().isEmpty() && editTextPlayerSearch.getText().toString().isEmpty()){
                    String filterGameString = constraint.toString().toLowerCase();

                    for (int i = 0; i < count; i++) {
                        filterableGame = list.get(i);
                        if (filterableGame.getName().toLowerCase().contains(filterGameString)) {
                            nlist.add(filterableGame);
                        }
                    }

                    results.values = nlist;
                    results.count = nlist.size();
                }

                if (!editTextPlayerSearch.getText().toString().isEmpty() && editTextSearch.getText().toString().isEmpty()){
                    int filterGameInt = parseInt(constraint.toString());

                    for (int i = 0; i < count; i++) {
                        filterableGame = list.get(i);
                        if (filterableGame.getMinPlayer() <= filterGameInt && filterableGame.getMaxPlayer() >= filterGameInt) {
                            nlist.add(filterableGame);
                        }
                    }

                    results.values = nlist;
                    results.count = nlist.size();
                }

                if (!editTextSearch.getText().toString().isEmpty() && !editTextPlayerSearch.getText().toString().isEmpty()){
                    int filterGameInt = parseInt(constraint.toString());
                    String filterGameString = constraint.toString().toLowerCase();

                    for (int i = 0; i < count; i++) {
                        filterableGame = list.get(i);
                        if (filterableGame.getMinPlayer() <= filterGameInt && filterableGame.getMaxPlayer() >= filterGameInt && filterableGame.getName().toLowerCase().contains(filterGameString)) {
                            nlist.add(filterableGame);
                        }
                    }

                    results.values = nlist;
                    results.count = nlist.size();
                }

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<Game>) results.values;
                notifyDataSetChanged();
            }

        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index = info.position;
        switch (item.getItemId()) {
            case R.id.contextmenu_delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(GameListActivity.this);

                builder.setMessage("Delete \"" + fullGameList.get(index).getName() + "\"?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Backendless.Persistence.of(Game.class).remove(fullGameList.get(index), new AsyncCallback<Long>() {
                                    @Override
                                    public void handleResponse(Long response) {
                                        gameAdapter.gamesList.remove(index);
                                        gameAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                builder.create().show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
