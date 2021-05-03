package tatwa.example.com.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import tatwa.example.com.recyclerview.adapter.dataholder.MapData;

public class CusomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CusomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_snippet_view, null);

        TextView registered = (TextView) view.findViewById(R.id.tv_registered);
        TextView solved = (TextView) view.findViewById(R.id.tv_solved);
        TextView pending = (TextView) view.findViewById(R.id.tv_pending);

       // MapData data = (MapData) marker.getTag();

       /* registered.setText(data.getRegistered());
        solved.setText(data.getSolved());
        pending.setText(data.getPending());*/

        return view;
    }
}
