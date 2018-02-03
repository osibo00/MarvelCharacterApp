package productions.darthplagueis.marvelapp.database;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import productions.darthplagueis.marvelapp.model.marvelserviceresults.CharacterResults;
import productions.darthplagueis.marvelapp.model.marvelserviceresults.CharacterUrls;


public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final CharacterDatabase dataBase, @NonNull final List<CharacterResults> resultsList) {
        PopulateDatabase task = new PopulateDatabase(dataBase, resultsList);
        task.execute();
    }

    public static void removeSpecificCharacter(@NonNull final CharacterDatabase dataBase, @NonNull final Character character) {
        RemoveSpecificCharacter task = new RemoveSpecificCharacter(dataBase, character);
        task.execute();
    }

    public static void removeCharacters(@NonNull final CharacterDatabase dataBase) {
        RemoveCharacters task = new RemoveCharacters(dataBase);
        task.execute();
    }

    private static void characterResultsInput(CharacterDatabase dataBase, List<CharacterResults> resultsList) {
        Date date = getDownloadDate();
        for (CharacterResults result : resultsList) {
            Character character = new Character();
            character.setCharacterId(result.getId());
            character.setDownloadDate(date);
            character.setName(result.getName());
            character.setImageUrl(result.getThumbnail().getPath() + "." + result.getThumbnail().getExtension());
            character.setComicsAvail(result.getComics().getAvailable());

            List<CharacterUrls> characterUrls = result.getUrls();
            character.setType(characterUrls.get(0).getType());
            character.setCharactersUrl(characterUrls.get(0).getUrl());

            addCharacter(dataBase, character);
        }
    }

    private static Character addCharacter(final CharacterDatabase dataBase, Character character) {
        dataBase.characterDao().insertAll(character);
        return character;
    }

    private static Date getDownloadDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        Log.d(TAG, "getDownloadDate: " + calendar.getTime());
        return calendar.getTime();
    }

    private static class PopulateDatabase extends AsyncTask<Void, Void, Void> {

        private final CharacterDatabase database;
        private final List<CharacterResults> characterResults;

        PopulateDatabase(CharacterDatabase dataBase, List<CharacterResults> characterResults) {
            this.database = dataBase;
            this.characterResults = characterResults;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characterResultsInput(database, characterResults);
            return null;
        }
    }

    private static class RemoveSpecificCharacter extends AsyncTask<Void, Void, Void> {

        private final CharacterDatabase database;
        private final Character character;

        RemoveSpecificCharacter(CharacterDatabase dataBase, Character character) {
            this.database = dataBase;
            this.character = character;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            database.characterDao().delete(character);
            return null;
        }
    }

    private static class RemoveCharacters extends AsyncTask<Void, Void, Void> {

        private final CharacterDatabase database;

        RemoveCharacters(CharacterDatabase dataBase) {
            this.database = dataBase;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            database.characterDao().deleteAll();
            return null;
        }
    }
}
