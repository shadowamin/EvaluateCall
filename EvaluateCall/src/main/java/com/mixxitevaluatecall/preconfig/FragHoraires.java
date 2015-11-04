package com.mixxitevaluatecall.preconfig;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mixxitevaluatecall.R;

public class FragHoraires extends Fragment implements OnClickListener {
	static final int PICK_CONTACT_REQUEST = 1;
	private TextView textDays, textHoures;
	private CheckBox checkBoxPro, checkBoxPerso;
	private static FragHoraires m_Instance = null;
	boolean proCallsState, persoCallsState;
	private int[] days = { 1, 1, 1, 1, 1, 0, 0 };
private String[] hours={"08:00","12:00", "14:00", "18:00"};
	public static FragHoraires getInstance() {
		if (m_Instance == null) {
			m_Instance = new FragHoraires();
		}
		return m_Instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View result = inflater
				.inflate(R.layout.frag_horaires, container, false);
		textDays = (TextView) result.findViewById(R.id.textDays);
		textHoures = (TextView) result.findViewById(R.id.textHoures);
		checkBoxPro = (CheckBox) result.findViewById(R.id.checkBoxPro);
		checkBoxPerso = (CheckBox) result.findViewById(R.id.checkBoxPerso);
		TextView fragTitle = (TextView) result.findViewById(R.id.fragTitle);
		TextView descriptionFrag = (TextView) result
				.findViewById(R.id.descriptionFrag);
		fragTitle.setText(R.string.preconfig_time_title);
		descriptionFrag.setText(R.string.preconfig_time_msg);

		textDays.setText(Html
				.fromHtml("Jours de travail: lundi, mardi, mercredi, jeudi et vendredi. <i><u><b>Modifier les jours</b></u></i>"));
		textHoures
				.setText(Html
						.fromHtml("Horaires de travail: 9:00 à 12:30 et 14h00 à 18h00.<i><u><b>Modifier les Horaires</b></u></i>"));

		textDays.setOnClickListener(this);
		textHoures.setOnClickListener(this);

		checkBoxPro.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				proCallsState = isChecked;
			}
		});

		checkBoxPerso.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				persoCallsState = isChecked;
			}
		});

		return result;
	}

	public int[] getDays() {
		return days;
	}

	public String[] getHoures() {
		return hours;
	}

	public boolean getProCallsState() {
		return proCallsState;
	}

	public boolean getPersoCallsState() {
		return persoCallsState;
	}

	private void showDaysDialog() {

		final String[] items = getResources().getStringArray(
				R.array.days_titles);
		boolean[] choisen = new boolean[days.length];
		for (int i = 0; i < days.length; i++) {
			if (days[i] == 0)
				choisen[i] = false;
			else
				choisen[i] = true;
		}
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());
		builder.setTitle("Jours de travail");
		builder.setPositiveButton("Valider",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						ArrayList<String> stringDays = new ArrayList<String>();
						for (int i = 0; i < days.length; i++) {
							if (days[i] == 1)
								stringDays.add(items[i]);
						}
						String messageDays = "Jours de travail: ";
						for (int i = 0; i < stringDays.size(); i++) {
							if (i == 0)
								messageDays = messageDays + " "
										+ stringDays.get(i);
							else if (i == stringDays.size() - 1)
								messageDays = messageDays + " et "
										+ stringDays.get(i);
							else
								messageDays = messageDays + ", "
										+ stringDays.get(i);
						}
						textDays.setText(Html.fromHtml(messageDays
								+ " <i><u><b>Modifier les jours</b></u></i>"));
					}
				});
		builder.setNegativeButton("Annuler",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}
				});

		builder.setMultiChoiceItems(items, choisen,
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						if (isChecked)
							days[which] = 1;
						else
							days[which] = 0;
					}
				});

		builder.show();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textDays:
			showDaysDialog();
			break;

		case R.id.textHoures:

			LayoutInflater li = LayoutInflater.from(getActivity());
			View promptsView = li.inflate(R.layout.dialogue_horaire, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity());

			alertDialogBuilder.setView(promptsView);
			final TextView startTimeMorning = (TextView) promptsView
					.findViewById(R.id.startTimeMorning);
			final TextView endTimeMorning = (TextView) promptsView
					.findViewById(R.id.endTimeMorning);
			final TextView startTimeAfternoon = (TextView) promptsView
					.findViewById(R.id.startTimeAfternoon);
			final TextView endTimeAfternoon = (TextView) promptsView
					.findViewById(R.id.endTimeAfternoon);
			startTimeMorning.setText(Html.fromHtml("<u><b>"+hours[0]+"</b></u>"));
			endTimeMorning.setText(Html.fromHtml("<u><b>"+hours[1]+"</b></u>"));
			startTimeAfternoon.setText(Html.fromHtml("<u><b>"+hours[2]+"</b></u>"));
			endTimeAfternoon.setText(Html.fromHtml("<u><b>"+hours[3]+"</b></u>"));

			OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					showTimer((TextView) v);
				}
			};
			startTimeMorning.setOnClickListener(listener);
			endTimeMorning.setOnClickListener(listener);
			startTimeAfternoon.setOnClickListener(listener);
			endTimeAfternoon.setOnClickListener(listener);


			alertDialogBuilder.setCancelable(false).setPositiveButton(
					"Valider", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							hours[0]=startTimeMorning.getText().toString();
							hours[1]= endTimeMorning.getText().toString();
							hours[2]=startTimeAfternoon.getText().toString();
							hours[3]= endTimeAfternoon.getText().toString();
							textHoures
							.setText(Html
									.fromHtml("Horaires de travail: "+hours[0]+" à "+hours[1]+" et "+hours[2]+" à "+hours[3]+".<i><u><b>Modifier les Horaires</b></u></i>"));

						}
					});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

			break;
		}

	}
	private void showTimer(final TextView time) {
		String timeText = time.getText().toString();
		String[] table = timeText.split(":");
		int hour = Integer.parseInt(table[0]);
		int minute = Integer.parseInt(table[1]);
		TimePickerDialog mTimePicker;
		mTimePicker = new TimePickerDialog(getActivity(),
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker,
							int selectedHour, int selectedMinute) {
						String hours;
						if (selectedHour < 10)
							hours = "0" + selectedHour;
						else
							hours = "" + selectedHour;

						String minutes;
						if (selectedMinute < 10)
							minutes = "0" + selectedMinute;
						else
							minutes = "" + selectedMinute;

						time.setText(Html.fromHtml("<u><b>"+hours + ":" + minutes+"</b></u>"));
					}
				}, hour, minute, true);
		mTimePicker.show();
	}

}
