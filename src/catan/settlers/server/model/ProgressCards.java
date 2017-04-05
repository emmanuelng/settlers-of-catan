package catan.settlers.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ProgressCards implements Serializable {

	private static final long serialVersionUID = 5315232581842690153L;

	public enum ProgressCardType {
		// Trade Cards
		COMMERCIAL_HARBOR, MASTER_MERCHANT, MERCHANT, MERCHANT_FLEET, RESOURCE_MONOPOLY, TRADE_MONOPOLY,
		// Politics Cards
		BISHOP, CONSTITUTION, DESERTER, DIPLOMAT, INTRIGUE, SABOTEUR, SPY, WARLORD, WEDDING,
		// Science Cards
		ALCHEMIST, CRANE, ENGINEER, INVENTOR, IRRIGATION, MEDICINE, MINING, PRINTER, ROAD_BUILDING, SMITH
	}

	private ArrayList<ProgressCardType> TradeCards;
	private ArrayList<ProgressCardType> PoliticsCards;
	private ArrayList<ProgressCardType> ScienceCards;

	public ProgressCards() {
		// Add one of each card to each deck
		TradeCards = new ArrayList<ProgressCardType>();
		PoliticsCards = new ArrayList<ProgressCardType>();
		ScienceCards = new ArrayList<ProgressCardType>();
		ProgressCardType[] progCards = ProgressCardType.values();
		for (int i = 0; i < progCards.length; i++) {
			if (i < 6) {
				TradeCards.add(progCards[i]);
			} else if (i < 15) {
				PoliticsCards.add(progCards[i]);
			} else {
				ScienceCards.add(progCards[i]);
			}
		}
		// Shuffle the decks
		Collections.shuffle(TradeCards);
		Collections.shuffle(PoliticsCards);
		Collections.shuffle(ScienceCards);
	}

	public ProgressCardType drawTradeCard() {
		if (!TradeCards.isEmpty()) {
			return TradeCards.remove(0);
		}
		return null;
	}

	public ProgressCardType drawPoliticsCard() {
		if (!PoliticsCards.isEmpty()) {
			return PoliticsCards.remove(0);
		}
		return null;
	}

	public ProgressCardType drawScienceCard() {
		if (!ScienceCards.isEmpty()) {
			return ScienceCards.remove(0);
		}
		return null;
	}
}
