package com.mixxitevaluatecall.preconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mixxitevaluatecall.R;
import com.mixxitevaluatecall.Utils;
import com.mixxitevaluatecall.db.ContactsDB;
import com.mixxitevaluatecall.models.Categorie;
import com.mixxitevaluatecall.models.Contact;

public class FragEquipe extends Fragment  {
	private static final int CONTACT_PICKER_RESULT = 1001;
	private static final int RESULT_OK = -1;

	ListView listContacts;
	ArrayList<Contact> contacts;
	ArrayList<Categorie> cats;
	int positionClicked = 0;
	private static FragEquipe m_Instance = null;

	public static FragEquipe getInstance() {
		if (m_Instance == null) {
			m_Instance = new FragEquipe();
		}
		return m_Instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View result = inflater.inflate(R.layout.frag_equipe, container, false);
		listContacts = (ListView) result.findViewById(R.id.ListEquipe);
		
		cats = ContactsDB.getInstance(getActivity()).geCategories(getActivity());
		
		contacts = new ArrayList<Contact>();
		TextView fragTitle=(TextView ) result.findViewById(R.id.fragTitle);
		TextView descriptionFrag=(TextView ) result.findViewById(R.id.descriptionFrag);
		fragTitle.setText(R.string.preconfig_team_title);
		descriptionFrag.setText(R.string.preconfig_team_msg);
		
		loadList();
		return result;
	}

	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	private void loadList() {

		SeparatedListAdapter adapter = new SeparatedListAdapter(getActivity());
		HashMap<Integer, ArrayList<Contact>> maps = new HashMap<Integer, ArrayList<Contact>>();
		
		for (int i = 0; i < cats.size(); i++) {
			if (!cats.get(i).getTitle().equals(getString(R.string.cat_msgv))) {
				maps.put(cats.get(i).getId(), new ArrayList<Contact>());
				for (int j = 0; j < contacts.size(); j++) {
					if (contacts.get(j).getIdCat() == cats.get(i).getId())
						maps.get(cats.get(i).getId()).add(contacts.get(j));
				}
				adapter.addSection(cats.get(i).getTitle(), new ContactsAdapter(
						getActivity(), maps.get(cats.get(i).getId())));
			}
		}
		
		listContacts.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		loadList();
		super.onResume();
	}

	public class ContactsAdapter extends ArrayAdapter<Contact> {
		Context ctxt;
		Activity activity;
		ArrayList<Contact> ListContacts;

		public ContactsAdapter(Context context, ArrayList<Contact> items) {

			super(context, R.layout.item_equipe, items);
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
		public Contact getItem(int position) {
			return (Contact) ListContacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewContactHolder holder;
			final Contact item = (Contact) ListContacts.get(position);

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) ctxt
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				holder = new ViewContactHolder();
				convertView = inflater.inflate(R.layout.item_equipe, parent,
						false);
				holder.nameContact = (TextView) convertView
						.findViewById(R.id.nameMember);
				holder.addPhoto = (ImageView) convertView
						.findViewById(R.id.imgContact);
				convertView.setTag(holder);
			} else
				holder = (ViewContactHolder) convertView.getTag();

			if (item.getIdContact() != null) {
				Uri uriImage = Utils.getPhotoFromId(getActivity(),
						Integer.parseInt(item.getIdContact()));
				if (uriImage != null) {
					holder.addPhoto.setImageURI(uriImage);
				} else
					holder.addPhoto
							.setImageResource(R.drawable.ic_contact_picture_180_holo_light);

				String name = Utils.getContactNamebyId(getActivity(),
						item.getIdContact());
				if (name != null) {
					holder.nameContact.setVisibility(View.VISIBLE);
					holder.nameContact.setText(name);
				}
			} else {
				holder.nameContact.setVisibility(View.GONE);
				holder.addPhoto
						.setImageResource(android.R.drawable.ic_menu_add);
			}


			return convertView;
		}

	}


	
	
	public class SeparatedListAdapter extends BaseAdapter {

		public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();
		public final ArrayAdapter<Section> headers;
		public final static int TYPE_SECTION_HEADER = 0;

		public SeparatedListAdapter(Context context) {
			headers = new SectionsAdapter(context);
		}

		public class Section {
			public String name;
			public Section(String name) {
				super();
				this.name = name;
			}
			
			
		}
		public class SectionsAdapter extends ArrayAdapter<Section> {
			Context ctxt;
			ArrayList<Section> ListSection;

			public SectionsAdapter(Context context) {

				super(context, R.layout.item_team_header);
				ctxt = context;
				ListSection = new ArrayList<Section>();

			}

			@Override
			public int getCount() {
				if (ListSection != null) {
					return ListSection.size();
				}
				return 0;
			}

			@Override
			public Section getItem(int position) {
				return (Section) ListSection.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}
	@Override
	public void add(Section object) {
		ListSection.add(object);
	}
			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				
					LayoutInflater inflater = (LayoutInflater) ctxt
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					
					convertView = inflater.inflate(R.layout.item_team_header, parent,
							false);
					TextView title = (TextView) convertView
							.findViewById(R.id.list_header_title);
					title.setText(ListSection.get(position).name);
					
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						positionClicked = position;
						Log.i("Preconfig", "position clicked="+positionClicked);
						Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
						intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
						startActivityForResult(intent, CONTACT_PICKER_RESULT);
					}
				});
					
				return convertView;
			}

		}
		
		
		public void addSection(String section, Adapter adapter, boolean flight) {
			this.headers.add(new Section(section));
			this.sections.put(section, adapter);
		}
		public void addSection(String section, Adapter adapter) {
			this.headers.add(new Section(section));
			this.sections.put(section, adapter);
		}

		public Object getItem(int position) {
			for (Object section : this.sections.keySet()) {
				Adapter adapter = sections.get(section);
				int size = adapter.getCount() + 1;

				if (position == 0)
					return section;
				if (position < size)
					return adapter.getItem(position - 1);

				position -= size;
			}
			return null;
		}

		public int getCount() {
			int total = 0;
			for (Adapter adapter : this.sections.values())
				total += adapter.getCount() + 1;
			return total;
		}

		public int getViewTypeCount() {
			int total = 1;
			for (Adapter adapter : this.sections.values())
				total += adapter.getViewTypeCount();
			return total;
		}

		public int getItemViewType(int position) {
			int type = 1;
			for (Object section : this.sections.keySet()) {
				Adapter adapter = sections.get(section);
				int size = adapter.getCount() + 1;

				if (position == 0)
					return TYPE_SECTION_HEADER;
				if (position < size)
					return type + adapter.getItemViewType(position - 1);

				position -= size;
				type += adapter.getViewTypeCount();
			}
			return -1;
		}

		public boolean areAllItemsSelectable() {
			return false;
		}

		public boolean isEnabled(int position) {
			return (getItemViewType(position) != TYPE_SECTION_HEADER);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int sectionnum = 0;
			for (Object section : this.sections.keySet()) {
				Adapter adapter = sections.get(section);
				int size = adapter.getCount() + 1;

				if (position == 0)
					return headers.getView(sectionnum, convertView, parent);
				if (position < size)
					return adapter.getView(position - 1, convertView, parent);

				position -= size;
				sectionnum++;
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}
	
private boolean contactExist(String contactId)
{
	for(Contact c :contacts)
	{
		if(c.getIdContact().equals(contactId))
			return true;
	}
	return false;
}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == CONTACT_PICKER_RESULT) {
				Uri ContactUri = data.getData();
				@SuppressWarnings("deprecation")
				Cursor c = getActivity().managedQuery(ContactUri, null, null,
						null, null);
				if (c.moveToFirst()) {
					int idNumber = c
							.getInt(c
									.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone._ID));
					String ContactId = Utils.queryContactInfo(getActivity(), ""
							+ idNumber,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
					if(!contactExist(ContactId))
					contacts.add(new Contact(-1,0,cats.get(positionClicked).getId(),-1, ContactId));
						
					loadList();
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	public class ViewContactHolder {
		public ImageView addPhoto;
		public TextView nameContact;
	}
}
