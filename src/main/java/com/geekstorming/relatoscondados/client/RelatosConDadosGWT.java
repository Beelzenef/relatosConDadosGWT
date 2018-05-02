package com.geekstorming.relatoscondados.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class RelatosConDadosGWT implements EntryPoint {
	
	@Override
	public void onModuleLoad() {
		Label saludito = new Label("hey there!");
		
		RootPanel.get().add(saludito);
	}
	
}
