package com.mixxitevaluatecall.callreception;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Intents;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mixxitevaluatecall.R;
import com.mixxitevaluatecall.Utils;
import com.mixxitevaluatecall.callreception.FragList.OnChoiceCatMade;
import com.mixxitevaluatecall.callreception.SimpleGestureFilter.SimpleGestureListener;
import com.mixxitevaluatecall.db.ContactsDB;
import com.mixxitevaluatecall.models.Contact;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends FragmentActivity implements OnChoiceCatMade,
		SimpleGestureListener {

	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	private int statu = 0;
	private ProgressBar progressBar;
	private boolean progress = true;
	private String Number;
	private String CallLength;
	public static final String FRAG_TAG = "frag";
	private Button buttonAddContact;
	private LinearLayout  imageSwipeLeft, imageSwipeRight;
	private ImageView contactPhoto,
			imageSwipUp;
	private TextView contactName, callLength, description;
	private ContactsDB db;
	private LinearLayout linearLayout, addNoteLayout;
	private FrameLayout layoutList;
	private int state = -1;
	private boolean swipings = false;
	private boolean swipeToClose = false;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private String name;
	private Contact contact;
	private SimpleGestureFilter detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
		// addNoteLayout = (LinearLayout) findViewById(R.id.addNoteLayout);
		imageSwipeLeft = (LinearLayout) findViewById(R.id.imageSwipeLeft);
		imageSwipeRight = (LinearLayout) findViewById(R.id.imageSwipeRight);
		imageSwipUp = (ImageView) findViewById(R.id.imageSwipUp);
		buttonAddContact = (Button) findViewById(R.id.buttonAddContact);
		contactPhoto = (ImageView) findViewById(R.id.contact_photo);
		contactName = (TextView) findViewById(R.id.contact_name_display_name);
		callLength = (TextView) findViewById(R.id.contact_call_length);
		description = (TextView) findViewById(R.id.description);

		layoutList = (FrameLayout) findViewById(R.id.layoutList);
		linearLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				progress = false;
				progressBar.setVisibility(View.INVISIBLE);
				return false;
			}
		});
		db = ContactsDB.getInstance(this);
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		// db.deleteAllContact();

		// addNoteLayout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(MainActivity.this, "Bientot dispo",
		// Toast.LENGTH_LONG).show();
		//
		// }
		// });
		imageSwipUp.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_UP)
					swipeToClose = false;
				if (action == MotionEvent.ACTION_DOWN)
					swipeToClose = true;
				return false;
			}
		});
		detector = new SimpleGestureFilter(this, this);
		
		Number = this.getIntent().getStringExtra("number");
		
		state = this.getIntent().getIntExtra("state", -1);
		if (Number == null)
			finish();
		else 
		{
			

			new Thread(new Runnable() {
				public void run() {
					while (progressBarStatus < 100 && progress) {

						progressBarStatus = getStatu();
						progressBarHandler.post(new Runnable() {
							public void run() {
								progressBar.setProgress(progressBarStatus);
							}
						});
					}
					if (progressBarStatus >= 100 && progress) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
//						 finish();
					}
				}
			}).start();

			Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
					R.anim.shake);
			linearLayout.startAnimation(shake);
			Vibrator v = (Vibrator) this
					.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(500);

			CallLength = this.getIntent().getStringExtra("call_length");

		}

	}

	private void initViews() {
		Uri uriImage = Utils.getPhotoFromNumber(this, Number);
		if (uriImage != null) {
			Bitmap ImageBitmap = Utils.getBitmapContactPhoto(this, Number);
			if (ImageBitmap != null)
				contactPhoto.setImageBitmap(ImageBitmap);
			else
				contactPhoto
						.setImageResource(R.drawable.ic_contact_picture_180_holo_light);
		} else
			contactPhoto
					.setImageResource(R.drawable.ic_contact_picture_180_holo_light);

		contact = db.getContact(Utils.getIdContact(MainActivity.this, Number));
		name = Utils.getContactName(this, Number);
		if (name != null)
			contactName.setText(name);
		if (CallLength != null)
			callLength.setText(CallLength);

		switch (state) {
		case 0:
			noContactView();
			break;

		case 1:
			colorView();
			break;
		case 2:
			proPersoView();
			break;
		case 3:
			categoriesView(name, contact);
			break;
		case 4:
			msgvView();
			break;

		}

	}

	private void proPersoView() {
		imageSwipeLeft.setVisibility(View.VISIBLE);
		imageSwipeRight.setVisibility(View.VISIBLE);
		layoutList.setVisibility(View.GONE);
		buttonAddContact.setVisibility(View.GONE);
		description.setVisibility(View.VISIBLE);
		swipings = true;
		description.setText("Classer ce contact");

	}

	private void noContactView() {
		if (name != null) {
			proPersoView();

		} else {
			layoutList.setVisibility(View.GONE);
			imageSwipeLeft.setVisibility(View.GONE);
			imageSwipeRight.setVisibility(View.GONE);
			// addNoteLayout.setVisibility(View.GONE);
			contactName.setText(R.string.contact_unkonown);
			buttonAddContact.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					progress = false;
					progressBar.setVisibility(View.INVISIBLE);
					Intent intent = new Intent(Intents.Insert.ACTION);
					// Sets the MIME type to match the Contacts Provider
					intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

					intent.putExtra(Intents.Insert.PHONE, Number);
					startActivity(intent);
				}
			});

			description.setVisibility(View.GONE);
		}
	}

	private void colorView() {
		Fragment frags;
		FragList sf;
		Log.i("EvaluateCall", "choing white /gry..");
		layoutList.setVisibility(View.VISIBLE);
		imageSwipeLeft.setVisibility(View.GONE);
		imageSwipeRight.setVisibility(View.GONE);
		buttonAddContact.setVisibility(View.GONE);

		description.setVisibility(View.VISIBLE);
		description
				.setText(Html
						.fromHtml("<p> Voulez vous autoriser  <b>"
								+ name
								+ "</b> à vous joindre l'orsque vous etes déclaré indisponible  ?</p>"));

		frags = fm.findFragmentByTag(FRAG_TAG);

		if (contact != null) {
			Log.i("EvaluateCall", "contact not null");
			sf = new FragList(contact.getIdContact(), state);
		} else {
			Log.i("EvaluateCall", "contact is null");

			sf = new FragList(Utils.getIdContact(MainActivity.this, Number),
					state);

		}
		if (frags == null) {
			ft.add(R.id.layoutList, sf, FRAG_TAG);
			ft.commit();
		}
	}

	private void msgvView() {

		Fragment frags;
		FragList sf;

		boolean isMsgv = db.IsContactMsgv(contact.getIdContact(),
				getString(R.string.cat_msgv));
		if (isMsgv) {
			layoutList.setVisibility(View.VISIBLE);
			imageSwipeLeft.setVisibility(View.GONE);
			imageSwipeRight.setVisibility(View.GONE);
			buttonAddContact.setVisibility(View.GONE);

			Log.i("EvaluateCall", "contact cat is msgv  && state=" + state);

			description.setVisibility(View.VISIBLE);
			description
					.setText(Html
							.fromHtml("<p> Vous n'avez pas souhaité renvoyer <b>"
									+ name
									+ "</b> vers un contact préselectionné, voulez vous le renvoyer ver un contact dédié ou le lesser en Messagerie vocale ?</p>"));

			frags = fm.findFragmentByTag(FRAG_TAG);
			sf = new FragList(contact.getIdContact(), state);
			if (frags == null) {
				ft.add(R.id.layoutList, sf, FRAG_TAG);
				ft.commit();
			}

		} else
			finish();
	}

	private void categoriesView(String name, Contact contact) {

		Log.i("EvaluateCall", "Contact exist in db");
		layoutList.setVisibility(View.VISIBLE);
		imageSwipeLeft.setVisibility(View.GONE);
		imageSwipeRight.setVisibility(View.GONE);
		buttonAddContact.setVisibility(View.GONE);

		description
				.setText(Html
						.fromHtml("<p><b>"
								+ name
								+ "</b> est un contact professionel, vers qui devriez-vous rediriger ses appels quand vous etes indisponible ?</p>"));

		Fragment frags = fm.findFragmentByTag(FRAG_TAG);
		FragList sf = new FragList(contact.getIdContact(), state);
		if (frags == null) {
			ft.add(R.id.layoutList, sf, FRAG_TAG);
			ft.commit();
		}
	}

	@Override
	protected void onResume() {
		initViews();
		super.onResume();
	}

	public int getStatu() {
		statu += 10;

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return statu;

	}

	@Override
	public void onChoiceCatMade() {
		progress = false;
		progressBar.setVisibility(View.INVISIBLE);
		description.setVisibility(View.VISIBLE);
		description
				.setText(Html
						.fromHtml("<p><b>"
								+ name
								+ "</b> est un contact professionel, vers qui devriez-vous rediriger ses appels quand vous etes indisponible ?</p>"));

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		// Call onTouchEvent of SimpleGestureFilter class
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		Log.i("EvaluateCall", "direction=" +direction);
		if (direction == SimpleGestureFilter.SWIPE_UP && swipeToClose)
			{
			finish();
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			}
		if (swipings) {
			if (direction == SimpleGestureFilter.SWIPE_LEFT)
				{
				if (contact != null && contact.getType() == -1)
					db.editType(contact.getId(), 0);
				else
					db.AddContact(new Contact(0, 0, -1, -1, Utils.getIdContact(
							MainActivity.this, Number)));
			contact = db.getContact(Utils.getIdContact(MainActivity.this,
					Number));
			if (state == 0)
			{
				finish();
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			
			}else {
				state = 3;
				categoriesView(name, contact);
			}
		}
			if (direction == SimpleGestureFilter.SWIPE_RIGHT) {

				if (contact != null && contact.getType() == -1)
					db.editType(contact.getId(), 1);
				else
					db.AddContact(new Contact(0, 1, -1, -1, Utils.getIdContact(
							MainActivity.this, Number)));
				finish();
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			}
		}
	}

	@Override
	public void onDoubleTap() {
	}

}
