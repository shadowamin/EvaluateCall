package com.mixxitevaluatecall.preconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mixxitevaluatecall.R;

public class FragContacts extends Fragment {

	ListView listContacts;
	ContactsAdapter adapter;
	HashMap<String, Boolean> maps = new HashMap<String, Boolean>();
	ArrayList<String> contacts;
	private static FragContacts m_Instance = null;
	ArrayAdapter<String> dataAdapter;

	public static FragContacts getInstance() {
		if (m_Instance == null) {
			m_Instance = new FragContacts();
		}
		return m_Instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View result = inflater
				.inflate(R.layout.frag_contacts, container, false);
		listContacts = (ListView) result.findViewById(R.id.contactsList);

		contacts = new ArrayList<String>();
		TextView fragTitle = (TextView) result.findViewById(R.id.fragTitle);
		TextView descriptionFrag = (TextView) result
				.findViewById(R.id.descriptionFrag);
		fragTitle.setText(R.string.preconfig_contacts_title);
		descriptionFrag
				.setText(Html
						.fromHtml("Utiliser <b>vos adresses mail</b> pour avoir rapidement une premiere classification des contacts.\nCochez ceux qui doivent etre prises en compte  et choisissez s'il sont \"pro\" ou \"perso\""));

		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		android.accounts.Account[] accounts = AccountManager.get(getActivity())
				.getAccounts();
		for (android.accounts.Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				if (!accountNamerExist(account.name))
					contacts.add(account.name);
			}
		}

		adapter = new ContactsAdapter(getActivity(), contacts);
		listContacts.setAdapter(adapter);

		return result;
	}

	private boolean accountNamerExist(String account) {
		for (String name : contacts) {
			if (name.equals(account))
				return true;
		}
		return false;
	}

	public HashMap<String, Boolean> getSelectedMails() {
		return maps;
	}

	public class ContactsAdapter extends ArrayAdapter<String> {
		Context ctxt;
		Activity activity;
		ArrayList<String> ListContacts;

		public ContactsAdapter(Context context, ArrayList<String> items) {

			super(context, R.layout.item_contact, items);
			ctxt = context;
			ListContacts = items;

		}

		@Override
		public int getCount() {
			if (ListContacts != null) {
				return ListContacts.size();
			}
			return 0;
		}

		@Override
		public String getItem(int position) {
			return ListContacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewContactHolder holder;
			final String item = ListContacts.get(position);

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) ctxt
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				holder = new ViewContactHolder();
				convertView = inflater.inflate(R.layout.item_contact, parent,
						false);
				holder.nameAccount = (TextView) convertView
						.findViewById(R.id.accountMail);
				holder.typeAccount = (CheckBox) convertView
						.findViewById(R.id.checkType);
				holder.checkMail = (CheckBox) convertView
						.findViewById(R.id.checkMail);
				convertView.setTag(holder);
			} else
				holder = (ViewContactHolder) convertView.getTag();

			holder.nameAccount.setText(item);
			holder.typeAccount
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							maps.put(item, isChecked);
						}
					});
			holder.checkMail
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								maps.put(item, holder.typeAccount.isChecked());
								holder.typeAccount
										.setButtonDrawable(R.drawable.contact_type_on);
								holder.typeAccount.setEnabled(true);
							} else {
								maps.remove(item);
								holder.typeAccount
										.setButtonDrawable(R.drawable.contact_type_off);
								holder.typeAccount.setEnabled(false);
							}
						}
					});
			return convertView;
		}

	}

	public class ViewContactHolder {
		public TextView nameAccount;
		public CheckBox typeAccount;
		public CheckBox checkMail;
	}
}
