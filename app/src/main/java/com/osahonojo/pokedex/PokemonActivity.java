package com.osahonojo.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.util.Log;

public class PokemonActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView numberTextView;
    private TextView ability1TextView;
    private TextView ability2TextView;
    private TextView ability3TextView;
    private TextView ability4TextView;

    private String url;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        url = getIntent().getStringExtra("url");

        nameTextView = findViewById(R.id.pokemon_name);
        numberTextView = findViewById(R.id.pokemon_number);
        ability1TextView = findViewById(R.id.pokemon_ability1);
        ability2TextView = findViewById(R.id.pokemon_ability2);
        ability3TextView = findViewById(R.id.pokemon_ability3);
        ability4TextView = findViewById(R.id.pokemon_ability4);

        load();
    }

    public void load() {
        ability1TextView.setText("");
        ability2TextView.setText("");
        ability3TextView.setText("");
        ability4TextView.setText("");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = response.getString("name");
                    nameTextView.setText(name.substring(0, 1).toUpperCase() + name.substring(1));

                    int number = response.getInt("id");
                    numberTextView.setText(String.format("#%03d", number));

                    JSONArray abilities = response.getJSONArray("abilities");
                    for (int i = 0; i < abilities.length(); i++) {
                        JSONObject abilityObject = abilities.getJSONObject(i);  // get item from abilities array
                        int slot = abilityObject.getInt("slot");
                        String ability = abilityObject.getJSONObject("ability").getString("name");
                        if (slot == 1) {
                            ability1TextView.setText(ability);
                        }
                        else if (slot == 2) {
                            ability2TextView.setText(ability);
                            //ability2TextView.setVisibility(View.VISIBLE);
                        }
                        else if (slot == 3) {
                            ability3TextView.setText(ability);
                            ability3TextView.setVisibility(View.VISIBLE);
                        }
                        else if (slot == 4) {
                            ability4TextView.setText(ability);
                            //ability4TextView.setVisibility(View.VISIBLE);
                        }
                    }

                }catch (JSONException e) {
                    Log.e("pokemon activity", "JSONArray request error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("pokemon activity", "JSONArray request error from error listener");
            }
        });
        requestQueue.add(request);
    }
}