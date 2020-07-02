package com.osahonojo.pokedex;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {
    public static class PokedexViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView textView;
        PokedexViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.pokedex_row_container);
            textView = view.findViewById(R.id.pokedex_row_text);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pokemon pokemon = (Pokemon) containerView.getTag();
                    Intent intent = new Intent(v.getContext(), PokemonActivity.class);
                    intent.putExtra("url", pokemon.getURL());

                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    private RequestQueue requestQueue;
    private List<Pokemon> pokedex = new ArrayList<>();

    PokedexAdapter(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        loadPokemon();
    }

    public void loadPokemon() {
        String url = "https://pokeapi.co/api/v2/pokemon?limit=151";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        String name = result.getString("name");
                        Pokemon pokemon = new Pokemon(name.substring(0, 1).toUpperCase() + name.substring(1), result.getString("url"));
                        pokedex.add(pokemon);
                    }
                    notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.e("adapter", "JSON request error from response listener", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("adapter class", "JSON request error from error listener", error);
            }
        });
        requestQueue.add(request);
    }

    // called by layout manager
    @NonNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokedex_row, parent, false);
        return new PokedexViewHolder(view);
    }

    // called by layout manager
    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        holder.textView.setText(pokedex.get(position).getName());
        holder.containerView.setTag(pokedex.get(position));
    }

    // called by layout manager
    @Override
    public int getItemCount() { // returns number of items in the list
        return pokedex.size();
    }
}
