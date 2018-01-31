package productions.darthplagueis.marvelapp.database;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import productions.darthplagueis.marvelapp.model.marvelserviceresults.CharacterResults;
import productions.darthplagueis.marvelapp.model.marvelserviceresults.CharacterUrls;

/**
 * Created by oleg on 1/31/18.
 */

public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final CharacterDataBase dataBase, @NonNull final List<CharacterResults> resultsList) {
        PopulateDatabase task = new PopulateDatabase(dataBase, resultsList);
        task.execute();
    }

    private static void characterResultsInput(CharacterDataBase dataBase, List<CharacterResults> resultsList) {
        for (CharacterResults result : resultsList) {
            Character character = new Character();
            character.setName(result.getName());
            character.setImageUrl(result.getThumbnail().getPath() + "." + result.getThumbnail().getExtension());
            character.setNumber(result.getId());
            character.setComicsAvail(result.getComics().getAvailable());

            List<CharacterUrls> characterUrls = result.getUrls();
            character.setType(characterUrls.get(0).getType());
            character.setCharactersUrl(characterUrls.get(0).getUrl());

            addCharacter(dataBase, character);
        }
    }

    private static Character addCharacter(final CharacterDataBase dataBase, Character character) {
        dataBase.characterDao().insertAll(character);
        return character;
    }

    private static class PopulateDatabase extends AsyncTask<Void, Void, Void> {

        private final CharacterDataBase db;
        private final List<CharacterResults> resultsList;

        PopulateDatabase(CharacterDataBase dataBase, List<CharacterResults> characterResults) {
            db = dataBase;
            resultsList = characterResults;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterResultsInput(db, resultsList);
            return null;
        }
    }
}
