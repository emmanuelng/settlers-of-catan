package catan.settlers.client.view.game;

import org.minueto.image.MinuetoImage;

public class TradeMenu extends MinuetoImage{
	
	// private ArrayList<ResourceType> offer;
	// private ArrayList<ResourceType> desired;
	// private MinuetoImage surface;
	// private boolean isOffer;
	//
	// public TradeMenu(){
	// super(ClientWindow.WINDOW_WIDTH,ClientWindow.WINDOW_HEIGHT);
	// //display the trade
	// drawSurface();
	//
	// offer = new ArrayList<ResourceType>();
	// desired = new ArrayList<ResourceType>();
	// ClientWindow.getInstance().getGameWindow().setTradeMenu(this);
	// }
	//
	// private void drawSurface(){
	// isOffer=true;
	// this.clear(MinuetoColor.BLACK.lighten(100));
	// drawResource(ResourceType.GRAIN, 100, 25);
	// drawResource(ResourceType.LUMBER, 100, 100);
	// drawResource(ResourceType.ORE, 100, 175);
	// drawResource(ResourceType.WOOL, 100, 250);
	// drawResource(ResourceType.BRICK, 100, 325);
	// drawResource(ResourceType.CLOTH, 100, 400);
	// drawResource(ResourceType.PAPER, 100, 475);
	// drawResource(ResourceType.COIN, 100, 550);
	// isOffer=false;
	// drawResource(ResourceType.GRAIN, ClientWindow.WINDOW_WIDTH-300, 25);
	// drawResource(ResourceType.LUMBER, ClientWindow.WINDOW_WIDTH-300, 100);
	// drawResource(ResourceType.ORE, ClientWindow.WINDOW_WIDTH-300, 175);
	// drawResource(ResourceType.WOOL, ClientWindow.WINDOW_WIDTH-300, 250);
	// drawResource(ResourceType.BRICK, ClientWindow.WINDOW_WIDTH-300, 325);
	// drawResource(ResourceType.CLOTH, ClientWindow.WINDOW_WIDTH-300, 400);
	// drawResource(ResourceType.PAPER, ClientWindow.WINDOW_WIDTH-300, 475);
	// drawResource(ResourceType.COIN, ClientWindow.WINDOW_WIDTH-300, 550);
	// }
	//
	// private void drawResource(ResourceType r, int x, int y) {
	//
	// MinuetoColor color;
	// ClickableText offerPlus = null;
	// ClickableText offerMinus = null;
	// ClickableText desiredPlus = null;
	// ClickableText desiredMinus = null;
	//
	//
	// switch (r) {
	// case GRAIN:
	// color = new MinuetoColor(198, 233, 175);
	// break;
	// case LUMBER:
	// color = new MinuetoColor(222, 170, 135);
	// break;
	// case ORE:
	// color = new MinuetoColor(200, 190, 183);
	// break;
	// case WOOL:
	// color = new MinuetoColor(255, 246, 213);
	// break;
	// case BRICK:
	// color = new MinuetoColor(255, 153, 85);
	// break;
	// case CLOTH:
	// color = new MinuetoColor(255, 230, 213);
	// break;
	// case PAPER:
	// color = new MinuetoColor(246, 255, 213);
	// break;
	// default:
	// color = new MinuetoColor(198, 233, 175);
	// break;
	// }
	//
	// MinuetoCircle resourceCircle = new MinuetoCircle(20, color, true);
	// MinuetoFont usedFont = new MinuetoFont("arial", 20, false, false);
	// MinuetoText numberOfResource = new MinuetoText("", usedFont,
	// MinuetoColor.BLACK);
	// MinuetoText typeOfResource = new MinuetoText("" + r, usedFont,
	// MinuetoColor.BLACK);
	// if(isOffer){
	// offerPlus = new ClickableText(x+200,y+100,r,"+",true,new
	// MinuetoFont("arial", 20, false, false),MinuetoColor.BLACK);
	// offerMinus = new ClickableText(x,y+100,r,"-",true,new
	// MinuetoFont("arial", 20, false, false),MinuetoColor.BLACK);
	// draw(offerMinus, x, y);
	// draw(offerPlus, x + 200, y);
	// ClientWindow.getInstance().getGameWindow().getMouseHandler().register(offerPlus);
	// ClientWindow.getInstance().getGameWindow().getMouseHandler().register(offerMinus);
	// }else{
	// desiredPlus = new ClickableText(x+200,y+100,r,"+",false,new
	// MinuetoFont("arial", 20, false, false),MinuetoColor.BLACK);
	// desiredMinus = new ClickableText(x,y+100,r,"-",false,new
	// MinuetoFont("arial", 20, false, false),MinuetoColor.BLACK);
	// draw(desiredMinus, x, y);
	// draw(desiredPlus, x + 200, y);
	// ClientWindow.getInstance().getGameWindow().getMouseHandler().register(desiredPlus);
	// ClientWindow.getInstance().getGameWindow().getMouseHandler().register(desiredMinus);
	// }
	// draw(resourceCircle, x+40, y);
	// draw(numberOfResource, x + 55, y + 10);
	// draw(typeOfResource, x + 90, y + 10);
	//
	//
	// }
	//
	// public void updateTradeMenu(ResourceType resourceType, String text,
	// boolean isOfferUpdate) {
	// surface = new
	// MinuetoImage(ClientWindow.getInstance().getGameWindow().getWidth(),ClientWindow.getInstance().getGameWindow().getHeight());
	//
	// MinuetoFont usedFont = new MinuetoFont("arial", 20, false, false);
	// if(text == "+" && isOfferUpdate == true){
	// offer.add(resourceType);
	// for(int i=0;i<offer.size();i++){
	// MinuetoText typeOfResource = new MinuetoText("1 " + offer.get(i),
	// usedFont, MinuetoColor.BLACK);
	// surface.draw(typeOfResource,450,200+i*50);
	// }
	// for(int i=0;i<desired.size();i++){
	// MinuetoText typeOfResource = new MinuetoText("1 " + desired.get(i),
	// usedFont, MinuetoColor.BLACK);
	// surface.draw(typeOfResource,550,200+i*50);
	// }
	// }else if(text == "-" && isOfferUpdate == true){
	// offer.remove(resourceType);
	// this.clear();
	// this.drawSurface();
	// for(int i=0;i<offer.size();i++){
	// MinuetoText typeOfResource = new MinuetoText("1 " + offer.get(i),
	// usedFont, MinuetoColor.BLACK);
	// surface.draw(typeOfResource,450,200+i*50);
	// }
	// for(int i=0;i<desired.size();i++){
	// MinuetoText typeOfResource = new MinuetoText("1 " + desired.get(i),
	// usedFont, MinuetoColor.BLACK);
	// surface.draw(typeOfResource,550,200+i*50);
	// }
	// }else if(text == "+" && isOfferUpdate == false){
	// desired.add(resourceType);
	// for(int i=0;i<offer.size();i++){
	// MinuetoText typeOfResource = new MinuetoText("1 " + offer.get(i),
	// usedFont, MinuetoColor.BLACK);
	// surface.draw(typeOfResource,450,200+i*50);
	// }
	// for(int i=0;i<desired.size();i++){
	// MinuetoText typeOfResource = new MinuetoText("1 " + desired.get(i),
	// usedFont, MinuetoColor.BLACK);
	// surface.draw(typeOfResource,550,200+i*50);
	// }
	// }else if(text == "-" && isOfferUpdate == false){
	// desired.remove(resourceType);
	// this.clear();
	// this.drawSurface();
	// for(int i=0;i<offer.size();i++){
	// MinuetoText typeOfResource = new MinuetoText("1 " + offer.get(i),
	// usedFont, MinuetoColor.BLACK);
	// surface.draw(typeOfResource,450,200+i*50);
	// }
	// for(int i=0;i<desired.size();i++){
	// MinuetoText typeOfResource = new MinuetoText("1 " + desired.get(i),
	// usedFont, MinuetoColor.BLACK);
	// surface.draw(typeOfResource,550,200+i*50);
	// }
	// }
	//
	// MinuetoText offerResource = new MinuetoText("Your offer",usedFont,
	// MinuetoColor.BLACK);
	// MinuetoText desiredResource = new MinuetoText("What you want",usedFont,
	// MinuetoColor.BLACK);
	// MinuetoText confirm = new MinuetoText("Press enter to confirm
	// trade.",usedFont, MinuetoColor.BLACK);
	// ClientWindow.getInstance().getGameWindow().registerKeyboardHandler(ClientWindow.getInstance().getGameWindow().getKeyBoardHandler(),
	// ClientWindow.getInstance().getGameWindow().getEventQueue());
	//
	//
	// surface.draw(offerResource,400,150);
	// surface.draw(desiredResource,550,150);
	// surface.draw(confirm, 400, 110);
	// draw(surface,0,0);
	// }
	//
	// public MinuetoImage getSurface(){
	// return surface;
	// }
	//
	// public void setSurface(MinuetoImage surface){
	// this.surface = surface;
	// }
	// public void confirmTradeOffer() {
	// this.clear();
	// if(offer.size() == 0 || desired.size() == 0){
	// return;
	// }
	//
	// ClientModel.instance.getNetworkManager().sendCommand(new
	// MaritimeTradeCommand(desired, offer, 0 /* normally 4 modify base on
	// port*/));
	//
	// offer = new ArrayList<ResourceType>();
	// desired = new ArrayList<ResourceType>();
	// drawSurface();
	// }
	
}


