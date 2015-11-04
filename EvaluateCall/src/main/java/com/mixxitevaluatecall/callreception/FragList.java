package com.mixxitevaluatecall.callreception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mixxitevaluatecall.R;
import com.mixxitevaluatecall.R.id;
import com.mixxitevaluatecall.R.layout;
import com.mixxitevaluatecall.db.ContactsDB;
import com.mixxitevaluatecall.models.Categorie;
import com.mixxitevaluatecall.models.Contact;

public class FragList extends Fragment {
	String idConttact;
	int state;
	private ContactsDB db;
	private ListView listofCats;
	ArrayList<Categorie> cats;
	OnChoiceCatMade mListener;
	StableArrayAdapter adapter;
	HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	Contact contact;

	public FragList(String id, int state) {
		super();
		this.idConttact = id;
		this.state = state;
	}

	@Override
	public void onAttach(Activity activity) {
		mListener = (OnChoiceCatMade) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.frag_list, container, false);
		listofCats = (ListView) result.findViewById(R.id.listOfItems);
		db = ContactsDB.getInstance(getActivity());
		

		loadList();

		listofCats
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent,
							final View view, int position, long id) {
						
						switch(state)
						{
						case 1:
							if(contact==null)
							{
								Log.i("EvaluateCall", "adding contact");
								db.AddContact(new Contact(0, -1, -1,position, idConttact));
							}else
								{
								Log.i("EvaluateCall", "updating contact");
								db.editColor(contact.getId(), position);
								}getActivity().finish();
							break;
						case 3:
							Categorie cat = cats.get(position);
							db.editCat(contact.getId(), cat.getId());

							getActivity().finish();
							break;
						case 4:
							if (position == 0) {
								mListener.onChoiceCatMade();
								state = 3;
								loadList();
							} else
								getActivity().finish();
							break;
						}
					}

				});
		return result;
	}

	public void loadList() {
		contact = db.getContact(idConttact);
		switch(state)
		{
		case 1:
			
			cats = new ArrayList<Categorie>();
			cats.add(new Categorie(0, "Oui, systématiquement"));
			cats.add(new Categorie(1, "Oui, sur ça demande (cas par cas)"));
			cats.add(new Categorie(2, "Non"));
			break;
		case 3:
			
			cats = db.geCategories(getActivity());
			break;
		case 4:
			cats = new ArrayList<Categorie>();
			cats.add(new Categorie(0, "Choisir un contact"));
			cats.add(new Categorie(1, "Lesser en Messagerie vocale"));
			break;
		}
		
		

		adapter = new StableArrayAdapter(getActivity(), cats, R.layout.item_cat);
		listofCats.setAdapter(adapter);

	}

	public interface OnChoiceCatMade {
		public void onChoiceCatMade();
	}

	private class StableArrayAdapter extends ArrayAdapter<Categorie> {
		private final Context context;
		private final List<Categorie> listCategories;
		private final int ressourceID;
		public StableArrayAdapter(Context context, List<Categorie> objects,
				int ressourceID) {
			super(context, ressourceID, objects);
			// TODO Auto-generated constructor stub
			this.context = context;
			this.listCategories = objects;
			this.ressourceID = ressourceID;

		}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		View view = convertView;
	
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(ressourceID, null);	

			TextView title = (TextView) view
					.findViewById(R.id.cat);
			
			title.setText(listCategories.get(position).getTitle());

		return view;
	}

	}
}
