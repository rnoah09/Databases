package com.mistershorr.databases;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import static java.lang.Integer.parseInt;

public class GameDetailActivity extends AppCompatActivity {

    private TextView textViewGameDetailName;
    private TextView textViewGameDetailPlayers;
    private TextView textViewGameDetailObjective;
    private TextView textViewGameDetailRules;
    private RatingBar ratingBarGameDetailRating;
    private ChipGroup chipGroupGameDetailTags;

    private EditText editTextEditDetailName;
    private EditText editTextEditDetailMin;
    private EditText editTextEditDetailMax;
    private EditText editTextEditDetailObjective;
    private EditText editTextEditDetailRules;
    private CheckBox checkBoxEditDetailCards;
    private CheckBox checkBoxEditDetailCoins;
    private CheckBox checkBoxEditDetailDice;
    private CheckBox checkBoxEditDetailGameMaster;
    private CheckBox checkBoxEditDetailPaper;
    private RatingBar ratingBarEditDetailRating;
    private Button buttonEditDetailSave;

    private boolean isCreate;

    private Game game;
    private Chip chip;

    private Menu editMenu;

    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gamedetail);

        Intent lastIntent = getIntent();
        boolean create = lastIntent.getBooleanExtra(GameListActivity.EXTRA_CREATE, false);

        isCreate = create;

        setContentView(R.layout.activity_gamedetail);

        wireWidgets();
        setListeners();

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(create == true){
            viewFlipper.setDisplayedChild(1);
            game = new Game();
        } else {
            viewFlipper.setDisplayedChild(0);
            game = lastIntent.getParcelableExtra(GameListActivity.EXTRA_GAME);
            update();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        if(viewFlipper.getDisplayedChild() == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(GameDetailActivity.this);

            builder.setMessage("Leave without saving?")
                    .setPositiveButton("Don't Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GameDetailActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            builder.create().show();
        }
        else{
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(viewFlipper.getDisplayedChild() == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(GameDetailActivity.this);

            builder.setMessage("Leave without saving?")
                    .setPositiveButton("Don't Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                            GameDetailActivity.super.onBackPressed();
                        }
                    })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

            builder.create().show();
        }
        else{
            super.onBackPressed();
//            Intent targetIntent = new Intent(GameDetailActivity.this, GameListActivity.class);
//            startActivity(targetIntent);
//            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        editMenu = menu;
        if(isCreate){
            editMenu.findItem(R.id.item_edit_detail).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_edit_detail:

                editMenu.findItem(R.id.item_edit_detail).setVisible(false);
                viewFlipper.setDisplayedChild(1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void update(){
        textViewGameDetailName.setText(game.getName());
        if (game.getMaxPlayer() == game.getMinPlayer()){
            textViewGameDetailPlayers.setText("" + game.getMinPlayer());
        }
        else{
            textViewGameDetailPlayers.setText(game.getMinPlayer() + "-" + game.getMaxPlayer());
        }
        textViewGameDetailObjective.setText(game.getObjective());
        textViewGameDetailRules.setText(game.getRule());
        ratingBarGameDetailRating.setRating((float) game.getRating());
        ratingBarGameDetailRating.setIsIndicator(true);

        editTextEditDetailName.setText(game.getName());
        editTextEditDetailMin.setText("" + game.getMinPlayer());
        editTextEditDetailMax.setText("" + game.getMaxPlayer());
        editTextEditDetailObjective.setText(game.getObjective());
        editTextEditDetailRules.setText(game.getRule());
        ratingBarEditDetailRating.setRating((float) game.getRating());

        String tagged = "";

        if (game.isUsesCards()) {
            tagged.equals(tagged + "Cards ");
        }
        if (game.isUsesCoins()) {
            tagged.equals(tagged + "Coins ");
        }
        if (game.isUsesDice()) {
            tagged.equals(tagged + "Dice ");
        }
        if (game.isUsesGameMaster()) {
            tagged.equals(tagged + "Game-Master ");
        }
        if (game.isUsesPaper()) {
            tagged.equals(tagged + "Paper");
        }
        if (tagged != "")
        {
            String[] tags = tagged.split(" ");

            for (String text : tags) {
                chip = (Chip) getLayoutInflater().inflate(R.layout.chip_item, null, false);
                chip.setText(text);

                chipGroupGameDetailTags.addView(chip);
            }
        }
    }


    public void wireWidgets() {
        textViewGameDetailName = findViewById(R.id.textview_gamedetail_name);
        textViewGameDetailPlayers = findViewById(R.id.textview_gamedetail_players);
        textViewGameDetailObjective = findViewById(R.id.textview_gamedetail_objective);
        textViewGameDetailRules = findViewById(R.id.textview_gamedetail_rules);
        ratingBarGameDetailRating = findViewById(R.id.ratingbar_gamedetail_rating);
        chipGroupGameDetailTags = findViewById(R.id.chipgroup_gamedetail_tags);

        editTextEditDetailName = findViewById(R.id.edittext_editdetail_name);
        editTextEditDetailMin = findViewById(R.id.edittext_editdetail_min);
        editTextEditDetailMax = findViewById(R.id.edittext_editdetail_max);
        editTextEditDetailObjective = findViewById(R.id.edittext_editdetail_obj);
        editTextEditDetailRules = findViewById(R.id.edittext_editdetail_rules);
        checkBoxEditDetailCards = findViewById(R.id.checkbox_editdetail_cards);
        checkBoxEditDetailCoins = findViewById(R.id.checkbox_editdetail_coins);
        checkBoxEditDetailDice = findViewById(R.id.checkbox_editdetail_dice);
        checkBoxEditDetailGameMaster = findViewById(R.id.checkbox_editdetail_gm);
        checkBoxEditDetailPaper = findViewById(R.id.checkbox_editdetail_paper);
        ratingBarEditDetailRating = findViewById(R.id.ratingbar_editdetail_rating);
        buttonEditDetailSave = findViewById(R.id.button_editdetail_save);
        viewFlipper = findViewById(R.id.viewflipper_gamedetail);
    }

    public void setListeners(){
        buttonEditDetailSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("save", "onClick");
                if (editTextEditDetailMin.getText().toString().trim().isEmpty()
                    || editTextEditDetailMax.getText().toString().trim().isEmpty()
                    || editTextEditDetailName.getText().toString().trim().isEmpty()
                    || editTextEditDetailObjective.getText().toString().trim().isEmpty()
                    || editTextEditDetailRules.getText().toString().trim().isEmpty())
                {
                    Log.e("save", "if");
                    Toast.makeText(GameDetailActivity.this, "Please fill out all required entry fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.e("save", "else");

                    game.setMinPlayer(parseInt(editTextEditDetailMin.getText().toString()));
                    game.setMaxPlayer(parseInt(editTextEditDetailMax.getText().toString()));
                    game.setName(editTextEditDetailName.getText().toString());
                    game.setObjective(editTextEditDetailObjective.getText().toString());
                    game.setRating(ratingBarEditDetailRating.getRating());
                    game.setRule(editTextEditDetailRules.getText().toString());
                    game.setUsesCards(checkBoxEditDetailCards.isChecked());
                    game.setUsesCoins(checkBoxEditDetailCoins.isChecked());
                    game.setUsesDice(checkBoxEditDetailDice.isChecked());
                    game.setUsesGameMaster(checkBoxEditDetailGameMaster.isChecked());
                    game.setUsesPaper(checkBoxEditDetailPaper.isChecked());
                    game.setOwnerId(Backendless.UserService.CurrentUser().getObjectId());

                    Backendless.Persistence.save(game, new AsyncCallback<Game>() {
                        public void handleResponse(Game response) {

                            Log.e("save", "backendless");
                        }

                        public void handleFault(BackendlessFault fault) {
                            Log.d("save fault", fault.getMessage());
                        }
                    });

                    viewFlipper.setDisplayedChild(0);
                    editMenu.findItem(R.id.item_edit_detail).setVisible(true);
                    update();
                }
            }
        });
    }

}
