package com.salesianostriana.dam.di.aroundme.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.salesianostriana.dam.di.aroundme.R;
import com.salesianostriana.dam.di.aroundme.fragments.AmigosFragment;
import com.salesianostriana.dam.di.aroundme.fragments.MensajesFragment;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout contenedor;
    private DrawerLayout drawer;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //ANOTACIONES CLASE MIGUEL
        //Importar de gms lo de google
        //Ver comentarios y métodos de miguel

        // Rescato una referencia del FrameLayout en el que voy
        // a cargar las páginas (Fragments)
        contenedor = (FrameLayout)findViewById(R.id.contenedor);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        prefs = getSharedPreferences("preferencias",MODE_PRIVATE);

        // Para rescatar una referencia a la cabecera del Menú lateral...
        View cabeceraMenuLateral = navigationView.getHeaderView(0);
        ImageView avatar = (ImageView)cabeceraMenuLateral.findViewById(R.id.imageViewAvatar);
        TextView nom_usuario = (TextView) cabeceraMenuLateral.findViewById(R.id.textViewNombre);

        Picasso.with(this).load(AvatarActivity.URL_AVATAR(prefs.getString("avatar",null))).resize(80,80).into(avatar);
        //nom_usuario.setText(prefs.getString("nick_name",null));

        this.transicionPagina(new AmigosFragment());

        // Marcar como elemento actual el 2º de la lista (quitar de aqui)
        //navigationView.setCheckedItem(R.id.nav_mensajes);
        //transicionPagina(new MensajesFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.closeDrawer(GravityCompat.START);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String mensaje = "";
        Fragment f = null;

        if (id == R.id.nav_usuarios) {
            f = new AmigosFragment();
            mensaje = "Amigos";
        } else if (id == R.id.nav_mensajes) {
            f = new MensajesFragment();
            mensaje = "Mensajes";
        } else if (id == R.id.nav_mapas) {
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(i);
            mensaje = "Mapas";
        } else if (id == R.id.nav_cerrar_sesion) {
            mensaje = "Cerrar sesión";

            // Crea el boton cerrar sesion que está alojado en menu_main
            // En caso de pulsar borra las preferencias y va hacia la pantalla de registro
            //btn_cerrar_sesion = (Button) findViewById(R.id.btn_cerrar_sesion);

            SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_APPEND);
            final SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            MainActivity.this.finish();




        }

        if(f!=null) {
            transicionPagina(f);
        }

        // Marco el elemento del menú como elemento
        // seleccionado.
        item.setChecked(true);
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void transicionPagina(Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor,f).commit();
    }

}
