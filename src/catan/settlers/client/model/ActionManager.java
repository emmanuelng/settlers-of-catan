package catan.settlers.client.model;

import java.util.ArrayList;

import catan.settlers.client.view.game.actions.ActivateKnightAction;
import catan.settlers.client.view.game.actions.BuildKnightAction;
import catan.settlers.client.view.game.actions.BuildRoadAction;
import catan.settlers.client.view.game.actions.BuildShipAction;
import catan.settlers.client.view.game.actions.BuildWallAction;
import catan.settlers.client.view.game.actions.DisplaceKnightAction;
import catan.settlers.client.view.game.actions.ExitMoveKnightMode;
import catan.settlers.client.view.game.actions.GameAction;
import catan.settlers.client.view.game.actions.ManageLevelsAction;
import catan.settlers.client.view.game.actions.MoveKnightAction;
import catan.settlers.client.view.game.actions.PlaceSettlementAction;
import catan.settlers.client.view.game.actions.UpgradeKnightAction;
import catan.settlers.client.view.game.actions.UpgradeToCityAction;
import catan.settlers.client.view.game.actions.cards.CardAction;
import catan.settlers.client.view.game.actions.cards.PlayBishopCardAction;
import catan.settlers.client.view.game.actions.cards.PlayCommercialHarbourCardAction;
import catan.settlers.client.view.game.actions.cards.PlayConstitutionCardAction;
import catan.settlers.client.view.game.actions.cards.PlayCraneCardAction;
import catan.settlers.client.view.game.actions.cards.PlayDeserterCardAction;
import catan.settlers.client.view.game.actions.cards.PlayDiplomatCardAction;
import catan.settlers.client.view.game.actions.cards.PlayEngineerCardAction;
import catan.settlers.client.view.game.actions.cards.PlayIntrigueCardAction;
import catan.settlers.client.view.game.actions.cards.PlayInventorCardAction;
import catan.settlers.client.view.game.actions.cards.PlayIrrigationCardAction;
import catan.settlers.client.view.game.actions.cards.PlayMasterMerchantCardAction;
import catan.settlers.client.view.game.actions.cards.PlayMedicineCardAction;
import catan.settlers.client.view.game.actions.cards.PlayMerchantCardAction;
import catan.settlers.client.view.game.actions.cards.PlayMerchantFleetCardAction;
import catan.settlers.client.view.game.actions.cards.PlayMiningCardAction;
import catan.settlers.client.view.game.actions.cards.PlayPrinterCardAction;
import catan.settlers.client.view.game.actions.cards.PlayResourceMonopolyCardAction;
import catan.settlers.client.view.game.actions.cards.PlayRoadBuildingCardAction;
import catan.settlers.client.view.game.actions.cards.PlaySaboteurCardAction;
import catan.settlers.client.view.game.actions.cards.PlaySmithCardAction;
import catan.settlers.client.view.game.actions.cards.PlaySpyCardAction;
import catan.settlers.client.view.game.actions.cards.PlayTradeMonopolyCardAction;
import catan.settlers.client.view.game.actions.cards.PlayWarlordCardAction;
import catan.settlers.client.view.game.actions.cards.PlayWeddingCardAction;

public class ActionManager {

	private ArrayList<GameAction> tradeActions;
	private ArrayList<GameAction> politicActions;
	private ArrayList<GameAction> scienceActions;
	private ArrayList<GameAction> miscActions;
	private ArrayList<GameAction> moveKnightActions;

	private ArrayList<CardAction> cardActions;

	public ActionManager() {
		this.tradeActions = new ArrayList<>();
		this.politicActions = new ArrayList<>();
		this.scienceActions = new ArrayList<>();
		this.miscActions = new ArrayList<>();
		this.moveKnightActions = new ArrayList<>();
		this.cardActions = new ArrayList<>();

		init();
	}

	public void init() {
		// Build settlement, road and ships
		tradeActions.add(new BuildRoadAction());
		tradeActions.add(new PlaceSettlementAction());
		tradeActions.add(new BuildShipAction());

		// Build city and city walls
		politicActions.add(new UpgradeToCityAction());
		politicActions.add(new BuildWallAction());

		// Hire knight, promote, activate
		scienceActions.add(new BuildKnightAction());
		scienceActions.add(new UpgradeKnightAction());
		scienceActions.add(new ActivateKnightAction());

		// Other actions
		miscActions.add(new MoveKnightAction());
		miscActions.add(new ManageLevelsAction());

		// Move knight actions
		moveKnightActions.add(new DisplaceKnightAction());
		moveKnightActions.add(new ExitMoveKnightMode());

		registerCardActions();
	}

	private void registerCardActions() {
		cardActions.add(new PlayBishopCardAction());
		cardActions.add(new PlayCommercialHarbourCardAction());
		cardActions.add(new PlayCraneCardAction());
		cardActions.add(new PlayConstitutionCardAction());
		cardActions.add(new PlayDeserterCardAction());
		cardActions.add(new PlayDiplomatCardAction());
		cardActions.add(new PlayEngineerCardAction());
		cardActions.add(new PlayIntrigueCardAction());
		cardActions.add(new PlayInventorCardAction());
		cardActions.add(new PlayIrrigationCardAction());
		cardActions.add(new PlayMasterMerchantCardAction());
		cardActions.add(new PlayMedicineCardAction());
		cardActions.add(new PlayMerchantCardAction());
		cardActions.add(new PlayMerchantFleetCardAction());
		cardActions.add(new PlayMiningCardAction());
		cardActions.add(new PlayPrinterCardAction());
		cardActions.add(new PlayResourceMonopolyCardAction());
		cardActions.add(new PlayRoadBuildingCardAction());
		cardActions.add(new PlaySaboteurCardAction());
		cardActions.add(new PlaySmithCardAction());
		cardActions.add(new PlaySpyCardAction());
		cardActions.add(new PlayTradeMonopolyCardAction());
		cardActions.add(new PlayWarlordCardAction());
		cardActions.add(new PlayWeddingCardAction());

	}

	public ArrayList<GameAction> getTradeActions() {
		ArrayList<GameAction> ret = new ArrayList<>();
		ret.addAll(tradeActions);
		return ret;
	}

	public ArrayList<GameAction> getPoliticActions() {
		ArrayList<GameAction> ret = new ArrayList<>();
		ret.addAll(politicActions);
		return ret;
	}

	public ArrayList<GameAction> getScienceActions() {
		ArrayList<GameAction> ret = new ArrayList<>();
		ret.addAll(scienceActions);
		return ret;
	}

	public ArrayList<GameAction> getMiscActions() {
		ArrayList<GameAction> ret = new ArrayList<>();
		ret.addAll(miscActions);
		return ret;
	}

	public ArrayList<GameAction> getMoveKnightActions() {
		ArrayList<GameAction> ret = new ArrayList<>();
		ret.addAll(moveKnightActions);
		return ret;
	}

	public ArrayList<CardAction> getProgressCardActions() {
		ArrayList<CardAction> ret = new ArrayList<>();

		for (CardAction card : cardActions)
			if (card.isPossible())
				ret.add(card);

		return ret;
	}

}
