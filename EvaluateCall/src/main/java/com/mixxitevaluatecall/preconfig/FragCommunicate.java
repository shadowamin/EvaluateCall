package com.mixxitevaluatecall.preconfig;

import android.accounts.Account;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Intents;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mixxitevaluatecall.R;


public class FragCommunicate extends Fragment implements OnClickListener{

	
	private static FragCommunicate m_Instance = null;
	private LinearLayout buttonAddToContact,buttonSendMail,buttonSendSms;
//	private Account mAccount;

	public static FragCommunicate getInstance() {
		if (m_Instance == null) {
			m_Instance = new FragCommunicate();
		}
		return m_Instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View result = inflater.inflate(R.layout.frag_communicate, container, false);
		buttonAddToContact= (LinearLayout) result.findViewById(R.id.buttonAddToContact);
		buttonSendMail= (LinearLayout) result.findViewById(R.id.buttonSendMail);
		buttonSendSms= (LinearLayout) result.findViewById(R.id.buttonSendSms);
		TextView fragTitle=(TextView) result.findViewById(R.id.fragTitleCommunicate);
		TextView descriptionFrag=(TextView) result.findViewById(R.id.descriptionCommunicate);
//		mAccount=Account.getInstance(getActivity());	
		fragTitle.setText(R.string.preconfig_communicate_title);
		descriptionFrag.setText(Html.fromHtml(getString(R.string.preconfig_communicate_msg)+" <b>%number%</b>"));
	
		buttonAddToContact.setOnClickListener(this);
		buttonSendMail.setOnClickListener(this);
		buttonSendSms.setOnClickListener(this);
			
		return result;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.buttonAddToContact:
			Intent intent = new Intent(Intents.Insert.ACTION);
			intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
			intent.putExtra(Intents.Insert.PHONE, "%number%");
			startActivity(intent);
			break;
		case R.id.buttonSendMail:
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setData(Uri.parse("mailto:"));
			i.setType("message/rfc822");
			String mailBody="Bonjour\n\n Voila mon nouveau numero Unique :%number%\nUtilisez le pour me contacter s'il vous plait\n\nMerci.";
			i.putExtra(Intent.EXTRA_SUBJECT,
					"Mon Numéro Unique");
			i.putExtra(Intent.EXTRA_TEXT, mailBody);
			try { 
			        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(i,
						"Envoie mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
			}
			break;
		case R.id.buttonSendSms:
			String sms="Voila mon numero Unique :%number%\nUtilisez le pour me contacter s'il vous plait."; 
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.putExtra("sms_body", sms); 
			sendIntent.setType("vnd.android-dir/mms-sms");
			startActivity(sendIntent);
			break;
		}
		
	}





}
