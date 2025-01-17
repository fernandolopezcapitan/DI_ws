package com.salesianostriana.dam.di.navigationdrawer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout contenedor;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Rescato una referencia del FrameLayout en el que voy
        // a cargar las páginas (Fragments)
        contenedor = (FrameLayout)findViewById(R.id.contenedor);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Para rescatar una referencia a la cabecera del Menú lateral...
        View cabeceraMenuLateral = navigationView.getHeaderView(0);
        ImageView avatar = (ImageView)cabeceraMenuLateral.findViewById(R.id.imageViewAvatar);

        // Marcar como elemento actual el 2º de la lista
        navigationView.setCheckedItem(R.id.nav_gallery);
        transicionPagina(new MensajesFragment());
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
        // Handle navigation view item clicks here.xx
        int id = item.getItemId();
        String mensaje = "";

        Fragment f = null;

        if (id == R.id.nav_camera) {
            mensaje = "Cámara";
            f = new UsuariosFragment();
        } else if (id == R.id.nav_gallery) {
            mensaje = "Galería";
            f = new MensajesFragment();
        } else if (id == R.id.nav_slideshow) {
            mensaje = "Slideshow";
        } else if (id == R.id.nav_manage) {
            mensaje = "Gestión";
        } else if (id == R.id.nav_share) {
            mensaje = "Compartir";
        } else if (id == R.id.nav_send) {
            mensaje = "Enviar";
        }

        if(f!=null) {
            transicionPagina(f);
        }

        // Marco el elemento del menú como elemento
        // seleccionado.
        item.setChecked(true);

        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void transicionPagina(Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor,f).commit();
    }


}
