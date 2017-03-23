package catan.settlers.server.model.units;

import catan.settlers.server.model.Player;

public class Knight implements IntersectionUnit {

	public enum KnightType {
		BASICKNIGHT, STRONGKNIGHT, MIGHTYKNIGHT
	}
	
	private Player myOwner;
	private KnightType knightType;
	private boolean activated;
	
	public Knight(Player p) {
		if(canHire(KnightType.BASICKNIGHT)){
			myOwner = p; 
			knightType = KnightType.BASICKNIGHT;
			activated = false; //need one wool and one ore to activate this kid
		}
	}

	
	public void upgradeKnight(Knight k){
		if (k.getKnightType() == KnightType.BASICKNIGHT && canHire(KnightType.STRONGKNIGHT)){
			k.setKnightType(KnightType.STRONGKNIGHT);
		}
		if (k.getKnightType() == KnightType.STRONGKNIGHT && canHire(KnightType.MIGHTYKNIGHT)){
			k.setKnightType(KnightType.MIGHTYKNIGHT);
		}
	}
	
	public boolean canHire(KnightType kType){
		if(kType == KnightType.BASICKNIGHT){
			if(myOwner.getKnightCount(kType)<2){
				return true;
			}
			return false;
		}
		else if(kType == KnightType.STRONGKNIGHT){
			if(myOwner.getKnightCount(kType)<2){
				return true;
			}
			return false;
		}
		else if(kType == KnightType.MIGHTYKNIGHT){
			if(myOwner.getKnightCount(kType)<2){
				return true;
			}
			return false;
		}
		return true;
	}
	@Override
	public Player getOwner() {
		// TODO Auto-generated method stub
		return myOwner;
	}

	public KnightType getKnightType(){
		return knightType;
	}
	
	private void setKnightType(KnightType kType){
		knightType = kType;
	}
	
	public void activateKnight(){ //need to pay one grain to activate
		activated = true;
	}
	
	@Override
	public boolean isKnight() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isVillage() {
		// TODO Auto-generated method stub
		return false;
	}

}
