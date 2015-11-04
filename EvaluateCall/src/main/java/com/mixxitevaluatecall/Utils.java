package com.mixxitevaluatecall;

import java.io.BufferedInputStream;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

public class Utils {
	@SuppressLint("NewApi")
	public static Bitmap getBitmapContactPhoto(Context context, String phoneNumber)
	{
		try {
			Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(phoneNumber));
//			Uri photoUri = null;
			ContentResolver cr = context.getContentResolver();
			Cursor contact = cr.query(phoneUri,
					new String[] { ContactsContract.Contacts._ID }, null, null,
					null);

			if (contact.moveToFirst()) {
				long id = contact.getLong(contact
						.getColumnIndex(ContactsContract.Contacts._ID));
				
				Uri my_contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(id));
				 InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(cr, my_contact_Uri, true);
				 BufferedInputStream buf = new BufferedInputStream(photo_stream);
				 Bitmap my_btmp = BitmapFactory.decodeStream(buf);
				 buf.close();
				 return my_btmp;
			} else {
				if (contact != null && !contact.isClosed())
					contact.close();
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	
	}
	
	public static Uri getPhotoFromId(Context context , int userId) {
		try {
			Uri photoUri = null;
			Uri uriContact = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI, userId);
				InputStream stream = ContactsContract.Contacts
						.openContactPhotoInputStream(
								context.getContentResolver(), uriContact);
				if (stream == null) {
					return null;
				}
				photoUri = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI, userId);
				return Uri.withAppendedPath(photoUri,
						ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
		} catch (Exception e) {
			return null;
		}

	}
	
	public static Uri getPhotoFromNumber(Context context, String phoneNumber) {
		try {
			Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(phoneNumber));
			Uri photoUri = null;
			ContentResolver cr = context.getContentResolver();
			Cursor contact = cr.query(phoneUri,
					new String[] { ContactsContract.Contacts._ID }, null, null,
					null);

			if (contact.moveToFirst()) {
				long userId = contact.getLong(contact
						.getColumnIndex(ContactsContract.Contacts._ID));
				Uri uriContact = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI, userId);
				InputStream stream = ContactsContract.Contacts
						.openContactPhotoInputStream(
								context.getContentResolver(), uriContact);
				if (stream == null) {
					contact.close();
					return null;
				}
				photoUri = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI, userId);
				if (contact != null && !contact.isClosed())
					contact.close();
				return Uri.withAppendedPath(photoUri,
						ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

			} else {
				if (contact != null && !contact.isClosed())
					contact.close();
				return null;
			}
		} catch (Exception e) {
			return null;
		}

	}
	
	public static String getIdContact(Context context, String phoneNumber)
	{
		try {
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(phoneNumber));
			ContentResolver cr = context.getContentResolver();
			Cursor contact = cr.query(uri,
					new String[] { ContactsContract.Contacts._ID }, null, null,
					null);

			if (contact.moveToFirst()) {
				long id = contact.getLong(contact
						.getColumnIndex(ContactsContract.Contacts._ID));
				
				
				 return ""+id;
			} else {
				if (contact != null && !contact.isClosed())
					contact.close();
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	
	}
	
	public static String getContactNamebyId(Context context, String contactId) {
		Cursor c = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
				new String[] { contactId }, null);
		if (c != null) {
			if (c.moveToFirst()) {

				String info = c.getString(0);
				c.close();
				return info;
			}
			c.close();
		}
		return null;
	}
	
	public static String queryContactInfo(Context context, String rawContactId,
			String type) {
		Cursor c = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { type },
				ContactsContract.CommonDataKinds.Phone._ID + "=?",
				new String[] { rawContactId }, null);
		if (c != null) {
			if (c.moveToFirst()) {

				String info = c.getString(0);
				c.close();
				return info;
			}
			c.close();
		}
		return null;
	}
	
	public static String getContactName(Context context, String phoneNumber) {
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(phoneNumber));
			Cursor cursor = cr
					.query(uri, new String[] { PhoneLookup.DISPLAY_NAME },
							null, null, null);
			if (cursor == null) {
				return null;
			}
			String contactName = null;
			if (cursor.moveToFirst()) {
				contactName = cursor.getString(cursor
						.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}

			return contactName;
		} catch (Exception e) {
			return null;
		}
	}
}
