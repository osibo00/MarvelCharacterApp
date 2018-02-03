package productions.darthplagueis.marvelapp.recyclerview;

import android.support.v7.util.DiffUtil;

import java.util.List;

import productions.darthplagueis.marvelapp.database.Character;


public class DiffUtility extends DiffUtil.Callback {
    private List<Character> oldCharacterList;
    private List<Character> newCharacterList;

    public DiffUtility(List<Character> oldCharacterList, List<Character> newCharacterList) {
        this.oldCharacterList = oldCharacterList;
        this.newCharacterList = newCharacterList;
    }

    @Override
    public int getOldListSize() {
        return oldCharacterList != null ? oldCharacterList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newCharacterList != null ? newCharacterList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCharacterList.get(oldItemPosition).getCharacterId() == newCharacterList.get(newItemPosition).getCharacterId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Character oldCharacter = oldCharacterList.get(oldItemPosition);
        Character newCharacter = newCharacterList.get(newItemPosition);
        return oldCharacter.getName().equalsIgnoreCase(newCharacter.getName());
    }
}
