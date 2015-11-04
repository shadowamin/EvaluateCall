package com.mixxitevaluatecall.preconfig;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mixxitevaluatecall.EconomieSeekBar;
import com.mixxitevaluatecall.R;

public class FragEco extends Fragment {

	private static FragEco m_Instance = null;
	private EconomieSeekBar proSeek, persoSeek, othersSeek;

	public static FragEco getInstance() {
		if (m_Instance == null) {
			m_Instance = new FragEco();
		}
		return m_Instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View result = inflater.inflate(R.layout.frag_eco, container, false);

		proSeek = (EconomieSeekBar) result.findViewById(R.id.seekbarPro);
		persoSeek = (EconomieSeekBar) result.findViewById(R.id.seekbarPerso);
		othersSeek = (EconomieSeekBar) result.findViewById(R.id.seekbarOthers);
		TextView fragTitle=(TextView ) result.findViewById(R.id.fragTitle);
		TextView descriptionFrag=(TextView ) result.findViewById(R.id.descriptionFrag);
		fragTitle.setText(R.string.preconfig_eco_title);
		descriptionFrag.setText(Html.fromHtml("Optimiser le <b>cout de vos appels</b> entrants pour chaque catégorie. Plus vous placez le curseur ver la droite, plus vous etes joigniable, mais plus le cout est eleve "));
				
		return result;
	}

	public int getproEco() {
		return proSeek.getSelectedItemIndex();
	}

	public int getpersoEco() {
		return persoSeek.getSelectedItemIndex();
	}

	public int getOthersEco() {
		return othersSeek.getSelectedItemIndex();
	}
}
