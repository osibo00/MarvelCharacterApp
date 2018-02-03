package productions.darthplagueis.marvelapp.recyclerview.controller;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import productions.darthplagueis.marvelapp.R;
import productions.darthplagueis.marvelapp.database.Character;
import productions.darthplagueis.marvelapp.recyclerview.DiffUtility;
import productions.darthplagueis.marvelapp.recyclerview.view.CharacterViewHolder;

/**
 * Created by oleg on 1/30/18.
 */

public class CharacterAdapter extends RecyclerView.Adapter<CharacterViewHolder> {

    private List<Character> characterList;

    public CharacterAdapter() {
        characterList = new ArrayList<>();
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View childView = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_item_view, parent, false);
        return new CharacterViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        holder.onBind(characterList.get(position));
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    public void passCharacterList(List<Character> newList) {
        characterList.addAll(newList);
        notifyItemRangeInserted(getItemCount(), characterList.size() - 1);
    }

    public void removeCharacter(int position) {
        characterList.remove(position);
        notifyItemRemoved(position);
    }

    public void clearList() {
        characterList.clear();
    }

    public void updateWithDifference(List<Character> newList) {
        DiffUtility diffUtility = new DiffUtility(characterList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtility);
        characterList.clear();
        characterList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}
