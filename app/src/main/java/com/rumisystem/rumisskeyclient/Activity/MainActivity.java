package com.rumisystem.rumisskeyclient.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.rumisystem.rumisskeyclient.R;

public class MainActivity extends AppCompatActivity {
	private DrawerLayout DRAWER_LAYOUT;
	private ActionBarDrawerToggle TOGGLE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);

		DRAWER_LAYOUT = findViewById(R.id.drawer_layout);
		TOGGLE = new ActionBarDrawerToggle(
				this, DRAWER_LAYOUT, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		DRAWER_LAYOUT.addDrawerListener(TOGGLE);
		TOGGLE.syncState();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		NavigationView navigationView = findViewById(R.id.nav_view);

		FrameLayout PAGE_HOME = findViewById(R.id.PAGE_HOME);
		FrameLayout PAGE_NOTIFY = findViewById(R.id.PAGE_NOTIFY);
		FrameLayout PAGE_SETTING = findViewById(R.id.PAGE_SETTING);

		navigationView.setNavigationItemSelectedListener(
			new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem) {
					int id = menuItem.getItemId();

					if(id == R.id.MENU_CONTENTS_HOME){
						PAGE_HOME.setVisibility(View.VISIBLE);
						PAGE_NOTIFY.setVisibility(View.GONE);
						PAGE_SETTING.setVisibility(View.GONE);
					} else if(id == R.id.MENU_CONTENTS_NOTIFY){
						PAGE_HOME.setVisibility(View.GONE);
						PAGE_NOTIFY.setVisibility(View.VISIBLE);
						PAGE_SETTING.setVisibility(View.GONE);
					} else if(id == R.id.MENU_CONTENTS_SETTING){
						PAGE_HOME.setVisibility(View.GONE);
						PAGE_NOTIFY.setVisibility(View.GONE);
						PAGE_SETTING.setVisibility(View.VISIBLE);
					}

					//ドロワーを閉じる
					DRAWER_LAYOUT.closeDrawer(GravityCompat.START);
					return true;
				}
			}
		);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (TOGGLE.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}