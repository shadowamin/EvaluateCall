package com.mixxitevaluatecall.preconfig;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mixxitevaluatecall.Alarms.AlarmsManager;
import com.mixxitevaluatecall.R;
import com.mixxitevaluatecall.TitelsSeekBar;
import com.mixxitevaluatecall.db.ContactsDB;

import java.util.ArrayList;

@SuppressLint("ResourceAsColor")
public class PreConfigActivity extends FragmentActivity implements
		OnClickListener {
	private TextView textSuivant;
	private ProgressDialog DialogCalls;;
	private ViewPager viewPager;
	private ArrayList<Fragment> fragmentsList;
	private ViewPagerAdapter adapter;
	private Context context;
	private TitelsSeekBar seekbarTitels;

	private ContactsDB db;
	private FragEquipe fragEquipe ;
	private FragContacts fragContacts;
	private FragEco fragEco;
	private FragHoraires fragHoraire;
	private FragCommunicate fragCommunicate;
	private int fragIndex = 0;
	private LinearLayout retourBtn;
	private LinearLayout suivantBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_config);


		seekbarTitels = (TitelsSeekBar) findViewById(R.id.seekbarTitels);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		retourBtn = (LinearLayout) findViewById(R.id.retourBtn);
		suivantBtn = (LinearLayout) findViewById(R.id.suivantBtn);

		textSuivant = (TextView) findViewById(R.id.textSuivant);

		seekbarTitels.setEnabled(false);
		retourBtn.setOnClickListener(this);
		suivantBtn.setOnClickListener(this);
		fragmentsList = new ArrayList<Fragment>();
		fragEquipe =  FragEquipe.getInstance();
		fragContacts = FragContacts.getInstance();
		fragEco = FragEco.getInstance();
		fragHoraire = FragHoraires.getInstance();
		fragCommunicate = FragCommunicate.getInstance();

		
//		seekbarTitels.initViews();
		fragmentsList.add(fragEquipe);
		fragmentsList.add(fragContacts);
		fragmentsList.add(fragEco);
		fragmentsList.add(fragHoraire);
		fragmentsList.add(fragCommunicate);
		context = this;
		db = ContactsDB.getInstance(this);
		adapter = new ViewPagerAdapter(getApplicationContext(),
				getSupportFragmentManager(), fragmentsList);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(fragIndex);
		retourBtn.setVisibility(View.INVISIBLE);
		
		
	
		viewPager.setOffscreenPageLimit(5);
		AlarmsManager.getInstance(this).startAlarms(new String[]{"14:52","14:56","14:45","15:55"});
	}

	@Override
	protected void onResumeFragments() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				fragIndex = position;
				seekbarTitels.handleDirectMove(position * 25);
				if (position == 4) {
					suivantBtn.setOnClickListener(null);
					textSuivant.setText(getString(R.string.btn_finir));
				}
				if (position == 3) {
					textSuivant.setText(getString(R.string.btn_suivant));
					suivantBtn.setOnClickListener(PreConfigActivity.this);
				}
				if ((fragIndex) <= 4) {
					retourBtn.setVisibility(View.VISIBLE);
					if (fragIndex == 4)
						suivantBtn.setVisibility(View.INVISIBLE);
				}
				if ((fragIndex) >= 0) {
					suivantBtn.setVisibility(View.VISIBLE);
					if (fragIndex == 0)
						retourBtn.setVisibility(View.INVISIBLE);
				}
			}

		});
		super.onResumeFragments();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.suivantBtn:
			if ((fragIndex) <= 4) {
				retourBtn.setVisibility(View.VISIBLE);
				fragIndex++;
				viewPager.setCurrentItem(fragIndex);
				seekbarTitels.handleDirectMove(fragIndex * 25);
				textSuivant.setText(getString(R.string.btn_suivant));
				if (fragIndex == 4) {
					textSuivant.setText(getString(R.string.btn_finir));
					suivantBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
//							new PreConfigTask().execute();
							finish();
						}
					});
				}
			}
			break;
		case R.id.retourBtn:
			if ((fragIndex) >= 0) {
				suivantBtn.setVisibility(View.VISIBLE);
				fragIndex--;
				viewPager.setCurrentItem(fragIndex);
				seekbarTitels.handleDirectMove(fragIndex * 25);
				suivantBtn.setOnClickListener(this);
				if (fragIndex == 0)
					retourBtn.setVisibility(View.INVISIBLE);
			}

			break;
		}
	}

//	private class PreConfigTask extends AsyncTask<Integer, Integer, Integer> {
//
//		@Override
//		protected void onPreExecute() {
//			DialogCalls = ProgressDialog.show(context,
//					getString(R.string.wait_please2),
//					getString(R.string.in_progress2));
//		}
//
//		@Override
//		protected Integer doInBackground(Integer... params) {
//
//			prefs.setPreconfigMade(true);
//
//			ArrayList<Contact> equipe = fragEquipe.getContacts();
//			HashMap<String, Boolean> mails = fragContacts.getSelectedMails();
//			int proEco = fragEco.getproEco();
//			int persoEco = fragEco.getpersoEco();
//			int othersEco = fragEco.getOthersEco();
//			int[] days = fragHoraire.getDays();
//			int[] hours = fragHoraire.getHoures();
//			boolean proCallsWork = fragHoraire.getProCallsState();
//			boolean persoCallsWork = fragHoraire.getPersoCallsState();
//
//			prefs.setProEco(proEco);
//			prefs.setPersoEco(persoEco);
//			prefs.setOthersEco(othersEco);
//			prefs.setProCallsWork(proCallsWork);
//			prefs.setPersoCallsWork(persoCallsWork);
//			prefs.setDaysWork(tableToString(days));
//			prefs.setHouresWork(tableToString(hours));
//
//			for (Entry<String, Boolean> entry : mails.entrySet()) {
//				String key = entry.getKey();
//				boolean value = entry.getValue();
//				AddAllContacts(key, value);
//			}
//			for (int i = 0; i < equipe.size(); i++)
//				db.AddContact(equipe.get(i));
//
//			MyCallHistoryDB dbCall = MyCallHistoryDB.getDB(context);
//			String timestamp = prefs.getLastDate();
//			ArrayList<MyCallHistory> calls;
//			if (timestamp == null) {
//				dbCall.deleteAll();
//				calls = LocalCallsManager.getInstance(PreConfigActivity.this)
//						.getCallDetails();
//				dbCall.saveCallHistory(calls, false);
//			} else {
//				calls = LocalCallsManager.getInstance(PreConfigActivity.this)
//						.getCallDetails();
//				if (calls.size() > 0) {
//					dbCall.saveCallHistory(calls, false);
//				}
//			}
//
//			return 0;
//		}
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			deactivateCallForwarding();
//			DialogCalls.dismiss();
//			Intent intent = new Intent(context, MainActivity.class);
//			startActivity(intent);
//			finish();
//		}
//
//	}

//	private void deactivateCallForwarding() {
//
//		Thread thread = new Thread() {
//
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(1000);
//					Log.i(AppConstants.TAG, "#21#");
//					Intent intent = new Intent(Intent.ACTION_CALL,
//							Uri.fromParts("tel", "#21#", null));
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					startActivity(intent);
//				} catch (InterruptedException e) {
//				}
//			}
//		};
//		thread.start();
//
//	}
//
//	private String tableToString(int[] table) {
//		String var = "";
//		for (int i = 0; i < table.length; i++) {
//			if (i == 0)
//				var = "" + table[i];
//			else
//				var = var + "," + table[i];
//		}
//		return var;
//	}
//
//	private void AddAllContacts(String choice, boolean perso) {
//		if (choice != null) {
//			Cursor cursor = null;
//			ContentResolver cr = getContentResolver();
//			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
//					null, null, null);
//			if (cur.getCount() > 0) {
//				while (cur.moveToNext()) {
//					String id = cur.getString(cur
//							.getColumnIndex(ContactsContract.Contacts._ID));
//					try {
//						ContentResolver contentResolver = getContentResolver();
//						cursor = contentResolver
//								.query(ContactsContract.RawContacts.CONTENT_URI,
//										new String[] { ContactsContract.RawContacts.CONTACT_ID },
//										ContactsContract.RawContacts.CONTACT_ID
//												+ "=? AND "
//												+ ContactsContract.RawContacts.ACCOUNT_NAME
//												+ "=? ",
//										new String[] { String.valueOf(id),
//												choice }, null);
//
//						if (cursor != null && cursor.getCount() > 0) {
//							cursor.moveToFirst();
//							if (perso)
//								db.AddContact(new Contact(-1, 1, -1, -1, id));
//							else
//								db.AddContact(new Contact(-1, 0, -1, -1, id));
//
//							cursor.close();
//						}
//					} catch (Exception e) {
//						Log.i(this.getClass().getName(), e.getMessage());
//					} finally {
//						cursor.close();
//					}
//
//				}
//				cur.close();
//			}
//		}
//	}

	public class ViewPagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> fragments;

		public ViewPagerAdapter(Context mcontext, FragmentManager fm,
				ArrayList<Fragment> fragmentsList) {
			super(fm);
			fragments = fragmentsList;
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

	}

	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
//		inflater.inflate(R.menu.add, menu);
//
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		if (id == R.id.action_add) {
//			new PreConfigTask().execute();
//
//		}
//		if (id == android.R.id.home) {
//			finish();
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
}
