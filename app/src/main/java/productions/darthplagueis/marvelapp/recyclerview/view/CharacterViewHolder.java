package productions.darthplagueis.marvelapp.recyclerview.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import productions.darthplagueis.marvelapp.R;
import productions.darthplagueis.marvelapp.database.Character;
import productions.darthplagueis.marvelapp.model.marvelserviceresults.CharacterResults;

/**
 * Created by oleg on 1/30/18.
 */

public class CharacterViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private RelativeLayout layout;
    private ImageView imageView;
    private TextView nameText, comicText, typeText;
    private Button moreInfoBtn;
    private Character character;

    public CharacterViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        layout = itemView.findViewById(R.id.character_layout);
        imageView = itemView.findViewById(R.id.character_image);
        nameText = itemView.findViewById(R.id.character_name);
        comicText = itemView.findViewById(R.id.character_comic);
        typeText = itemView.findViewById(R.id.character_type);
        moreInfoBtn = itemView.findViewById(R.id.character_url_btn);
    }

    public Character getCharacter() {
        return character;
    }

    public void onBind(final Character character) {
        this.character = character;
        Glide.with(context)
                .asBitmap()
                .load(character.getImageUrl())
                .apply(new RequestOptions().override(imageView.getWidth(), imageView.getHeight()))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        onPalette(Palette.from(resource).generate());
                        imageView.setImageBitmap(resource);
                        return false;
                    }

                    private void onPalette(Palette palette) {
                        if (palette != null) {
                            String color = Integer.toHexString(palette.getVibrantColor(context.getResources().getColor(R.color.cardview_light_background)));
                            String newColor = color.substring(2, color.length());
                            newColor = new StringBuilder(newColor).insert(0, "#a6").toString();
                            try {
                                layout.setBackgroundColor(Color.parseColor(newColor));
                            } catch (IllegalArgumentException e) {
                                layout.setBackgroundColor(Color.parseColor(color));
                            }

                        }
                    }
                })
                .into(imageView);

        nameText.setText(character.getName());
        String comicString = "Appears in " + String.valueOf(character.getComicsAvail() + " Comics");
        comicText.setText(comicString);

        moreInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = character.getCharactersUrl();
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }
}
