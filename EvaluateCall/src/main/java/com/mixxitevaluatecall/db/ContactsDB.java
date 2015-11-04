package com.mixxitevaluatecall.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mixxitevaluatecall.R;
import com.mixxitevaluatecall.models.Categorie;
import com.mixxitevaluatecall.models.Contact;

public class ContactsDB extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static ContactsDB m_Instance = null;
	private static final String DATABASE_NAME = "Evaluate";

	private static final String TABLE_CONTACTS = "contacts";
	private static final String TABLE_CAT = "categories";

	private static final String CONTACT_ID = "id";
	private static final String CONTACT_ID_CONTACT = "id_contact";
	private static final String CONTACT_CAT = "cat_id";
	private static final String CONTACT_TYPE = "type";
	private static final String CONTACT_COLOR = "color";

	public ContactsDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static ContactsDB getInstance(Context ctx) {
		m_Instance = new ContactsDB(ctx);
		return m_Instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	public SQLiteDatabase openDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();

		return db;
	}

	public void AddCategorie(String cat) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (!tableExists(TABLE_CAT)) {
			String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_CAT
					+ " (id INTEGER PRIMARY KEY , title TEXT)";
			db.execSQL(CREATE_WORDS_TABLE);
		}
		ContentValues values = new ContentValues();
		values.put("title", cat);
		db.insert(TABLE_CAT, null, values);

	}

	public ArrayList<String> geCategoriesNames(Context ctx) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (!tableExists(TABLE_CAT)) {
			String[] defaultCats = { ctx.getString(R.string.cat_equipe),
					ctx.getString(R.string.cat_assistant),
					ctx.getString(R.string.cat_supirior),
					ctx.getString(R.string.cat_msgv) };
			String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_CAT
					+ " (id INTEGER PRIMARY KEY , title TEXT)";
			db.execSQL(CREATE_WORDS_TABLE);
			for (int i = 0; i < defaultCats.length; i++)
				AddCategorie(defaultCats[i]);
		}

		ArrayList<String> cats = new ArrayList<String>();
		String selectQuery = "SELECT title FROM " + TABLE_CAT;
		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					cats.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
		}
		return cats;
	}

	public ArrayList<Categorie> geCategories(Context ctx) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (!tableExists(TABLE_CAT)) {
			String[] defaultCats = { ctx.getString(R.string.cat_equipe),
					ctx.getString(R.string.cat_assistant),
					ctx.getString(R.string.cat_supirior),
					ctx.getString(R.string.cat_msgv) };
			String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_CAT
					+ " (id INTEGER PRIMARY KEY , title TEXT)";
			db.execSQL(CREATE_WORDS_TABLE);
			for (int i = 0; i < defaultCats.length; i++)
				AddCategorie(defaultCats[i]);
		}

		ArrayList<Categorie> cats = new ArrayList<Categorie>();
		String selectQuery = "SELECT  id, title FROM " + TABLE_CAT;
		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					cats.add(new Categorie(cursor.getInt(0), cursor
							.getString(1)));
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
		}
		return cats;
	}

	public String GetCategorie(int id) {
		if (id < 0)
			return null;
		String cat = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  title FROM " + TABLE_CAT + " WHERE id="
				+ id;
		try {
			Cursor cursor = db.rawQuery(selectQuery, null);
			cursor.moveToFirst();

			cat = cursor.getString(0);
			cursor.close();
		} catch (Exception e) {

		}
		return cat;
	}

	public void deleteCat(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CAT, "id = ?", new String[] { String.valueOf(id) });
		db.close();
	}

	public void AddContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (!tableExists(TABLE_CONTACTS)) {
			String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS
					+ "(" + CONTACT_ID + " INTEGER PRIMARY KEY ,"
					+ CONTACT_ID_CONTACT + " TEXT , " + CONTACT_CAT
					+ " INTEGER, " + CONTACT_TYPE + " INTEGER, "
					+ CONTACT_COLOR + " INTEGER)";
			db.execSQL(CREATE_CONTACTS_TABLE);

		}
		ContentValues values = new ContentValues();
		values.put(CONTACT_ID_CONTACT, contact.getIdContact());
		values.put(CONTACT_CAT, contact.getIdCat());
		values.put(CONTACT_TYPE, contact.getType());
		values.put(CONTACT_COLOR, contact.getColor());

		String selectQuery = "SELECT " + CONTACT_ID_CONTACT + " FROM "
				+ TABLE_CONTACTS + " WHERE " + CONTACT_ID_CONTACT + " = '"
				+ contact.getIdContact() + "'";
		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.getCount() > 0)
				db.update(TABLE_CONTACTS, values, CONTACT_ID_CONTACT + " = '"
						+ contact.getIdContact() + "'", null);
			else
				db.insert(TABLE_CONTACTS, null, values);
			cursor.close();

		} catch (Exception e) {
		}

	}

	public void editType(int id, int type) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("type", type);
		db.update(TABLE_CONTACTS, values, "id = ?",
				new String[] { String.valueOf(id) });
	}

	public void editColor(int id, int color) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(CONTACT_COLOR, color);
		db.update(TABLE_CONTACTS, values, "id = ?",
				new String[] { String.valueOf(id) });
	}

	public void editCat(int id, int cat) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(CONTACT_CAT, cat);
		db.update(TABLE_CONTACTS, values, "id = ?",
				new String[] { String.valueOf(id) });
	}

	public ArrayList<Contact> getContacts() {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + "";
		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					contacts.add(new Contact(cursor.getInt(0),
							cursor.getInt(3), cursor.getInt(2), cursor
									.getInt(4), cursor.getString(1)));

				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
		}
		return contacts;
	}

	public Contact getContact(String contactId) {
		Contact contact = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE "
				+ CONTACT_ID_CONTACT + " = " + contactId;
		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.getCount() > 0) {
				if (cursor.moveToFirst()) {

					contact = new Contact(cursor.getInt(0), cursor.getInt(3),
							cursor.getInt(2), cursor.getInt(4),
							cursor.getString(1));

				}
			}
			cursor.close();

		} catch (Exception e) {
		}

		return contact;
	}

	public boolean IsContactMsgv(String contactId, String type) {
		boolean isMsgv = false;
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT " + CONTACT_ID_CONTACT + " FROM "
				+ TABLE_CONTACTS + ", " + TABLE_CAT + " WHERE "
				+ CONTACT_ID_CONTACT + " = " + contactId + " AND " + ""
				+ CONTACT_CAT + " = " + TABLE_CAT + ".id AND title='" + type
				+ "'";
		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.getCount() > 0)
				isMsgv = true;

			cursor.close();

		} catch (Exception e) {
		}

		return isMsgv;
	}

	public void deleteContact(int id) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_CONTACTS, "id = ?", new String[] { String.valueOf(id) });
		db.close();
	}

	public void deleteAllContact() {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_CONTACTS, null, null);
		db.close();
	}

	public boolean tableExists(String tableName) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.query("sqlite_master", new String[] { "name" },
				"name = '" + tableName + "' AND type = 'table'", null, null,
				null, null);
		boolean result = cur.moveToFirst();
		cur.close();
		return result;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}