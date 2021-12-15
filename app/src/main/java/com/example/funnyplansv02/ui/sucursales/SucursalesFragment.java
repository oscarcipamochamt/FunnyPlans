package com.example.funnyplansv02.ui.sucursales;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.funnyplansv02.BuildConfig;
import com.example.funnyplansv02.FormActivity;
import com.example.funnyplansv02.R;
import com.example.funnyplansv02.databinding.FragmentSucursalesBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class SucursalesFragment extends Fragment {

    View v;

    private MapView myOpenMapView;
    private MapController myMapController;

    GeoPoint CentroAndino, UnicentroBogota, CentroInt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_sucursales, container, false);
        //-----------------------------------------------------------------------------

        myOpenMapView = (MapView) v.findViewById(R.id.openmapview);

        /* ---- necesitamos establecer el valor del agente de usuario en la configuración ------- */
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        /*   punto de geolocalizacion de ejemplo */
        CentroAndino = new GeoPoint(4.667166995550591, -74.05355261685786);
        UnicentroBogota = new GeoPoint(4.702384251072413, -74.04147738802182);
        CentroInt = new GeoPoint(4.619698434370676, -74.0674164117846);

        myOpenMapView.setBuiltInZoomControls(true);

        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setCenter(CentroAndino);
        myMapController.setZoom(14);

        myOpenMapView.setMultiTouchControls(true);

        /* -------------------------------------------------------------------------------------------------- */
        final MyLocationNewOverlay myLocationoverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), myOpenMapView);
        myOpenMapView.getOverlays().add(myLocationoverlay); //No añadir si no quieres una marca
        myLocationoverlay.enableMyLocation();

        myLocationoverlay.runOnFirstFix(new Runnable() {
            public void run() {
                myMapController.animateTo(myLocationoverlay.getMyLocation());
            }
        });
        /* -------------------------------------------------------------------------------------------------- */

        /* MARCAS EN EL MAPA */

        ArrayList<OverlayItem> puntos = new ArrayList<OverlayItem>();
        puntos.add(new OverlayItem("Centro Andino", "Sucursal Principal", CentroAndino));
        puntos.add(new OverlayItem("Unicentro", "Sucursal Norte", UnicentroBogota));
        puntos.add(new OverlayItem("Centro Internacional", "Sucursal Centro", CentroInt));

        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> tap = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemLongPress(int arg0, OverlayItem arg1) {
                return false;
            }
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }
        };

        ItemizedOverlayWithFocus<OverlayItem> capa = new ItemizedOverlayWithFocus<OverlayItem>(getContext(), puntos, tap);
        capa.setFocusItemsOnTap(true);
        myOpenMapView.getOverlays().add(capa);

        //-----------------------------------------------------------------------------
        return v;
    }
}