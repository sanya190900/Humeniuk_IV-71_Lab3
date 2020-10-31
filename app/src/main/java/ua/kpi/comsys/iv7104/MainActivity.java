package ua.kpi.comsys.iv7104;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.*;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends ListActivity {

    private String[] titles;
    private String[] years;
    private String[] type;
    private int[] posters;
    private String[] posterNames;
    String data = "";
    private FilmAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            InputStream inputStream = getApplicationContext().getAssets().open("MoviesList.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            data = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = jsonObject.getJSONArray("Search");
            titles = new String[array.length()];
            years = new String[array.length()];
            type = new String[array.length()];
            posterNames = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                titles[i] = array.getJSONObject(i).getString("Title");
                years[i] = array.getJSONObject(i).getString("Year");
                type[i] = array.getJSONObject(i).getString("Type");
                posterNames[i] = array.getJSONObject(i).getString("Poster").toLowerCase().replaceAll(".jpg", "");
            }
        }catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        posters = new int[posterNames.length];
        for (int i = 0; i < posters.length; i++) {
            if(posterNames[i].equals("")){
                posters[i] = R.drawable.background;
            }else{
                posters[i] = getResources().getIdentifier(posterNames[i], "drawable", getPackageName());
            }
        }
        mAdapter = new FilmAdapter(this);
        setListAdapter(mAdapter);
    }

    private class FilmAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        FilmAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.activity_customlist, null);

            ImageView image = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            image.setImageResource(posters[position]);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.textViewTitle);
            titleTextView.setText(titles[position]);

            TextView dateTextView = (TextView) convertView.findViewById(R.id.textViewDate);
            dateTextView.setText(years[position]);

            TextView typeTextView = (TextView) convertView.findViewById(R.id.textViewType);
            typeTextView.setText(type[position]);

            return convertView;
        }
    }
}