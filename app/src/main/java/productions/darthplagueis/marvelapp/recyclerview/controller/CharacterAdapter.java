package productions.darthplagueis.marvelapp.recyclerview.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import productions.darthplagueis.marvelapp.R;
import productions.darthplagueis.marvelapp.database.Character;
import productions.darthplagueis.marvelapp.model.marvelserviceresults.CharacterResults;
import productions.darthplagueis.marvelapp.recyclerview.view.CharacterViewHolder;

/**
 * Created by oleg on 1/30/18.
 */

public class CharacterAdapter extends RecyclerView.Adapter<CharacterViewHolder> {

    private List<Character> characterResultsList = new ArrayList<>();

    public CharacterAdapter(List<Character> characterResultsList) {
        this.characterResultsList = characterResultsList;
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View childView = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_item_view, parent, false);
        return new CharacterViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        holder.onBind(characterResultsList.get(position));
    }

    @Override
    public int getItemCount() {
        return characterResultsList.size();
    }
}
