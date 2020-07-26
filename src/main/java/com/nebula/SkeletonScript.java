package com.nebula;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;
import org.dreambot.api.wrappers.interactive.NPC;

@ScriptManifest(author = "Alexei", name = "DreamBot Skeleton Template Script", version = 1.109, description = "Testing bank withdraws", category = Category.MISC)
public class SkeletonScript extends AbstractScript {

    private enum State{
        Bank, Fish, Pause
    }

    private Area FISHING_SPOT_LUMBRIDGE_SOUTH = new Area(new Tile(3242, 3155), new Tile(3238, 3150));

    private State getAction(){
        if( getLocalPlayer().isAnimating() ||
            getLocalPlayer().getAnimation() == 621
        ){
            log("Player is animating");
            return State.Pause;
        }
        if( getInventory().isFull() || !getInventory().contains("Small fishing net")){
            // if not net make sure to grab that
            log("Inventory is full or missing fishing net");
            return State.Bank;
        }
        if( getInventory().contains("Small fishing net") && !getInventory().isFull() ){
            log("Inventory contains 303 or small net, lets go fishing");
            return State.Fish;
        }
        return State.Pause;
    }

    public void onStart() {

    }

    public void onExit() {

    }

    @Override
    public int onLoop() {
        switch (getAction()) {
            case Bank:
                log("Walking to Lumbridge bank");
                if (!BankLocation.LUMBRIDGE.getArea(2).contains(getLocalPlayer())) {
                    if (getWalking().walk(BankLocation.LUMBRIDGE.getArea(2).getRandomTile())) {
                        MethodProvider.sleepUntil(() -> getWalking().shouldWalk(), 4000);
                    }
                }
                if(BankLocation.LUMBRIDGE.getArea(3).contains( getLocalPlayer()) ){
                    if( !getBank().isOpen() ){
                        MethodProvider.sleepUntil(() -> getBank().open( getBank().getClosestBankLocation()), 1000);
                    }else{
                        getBank().depositAll(item -> item != null && !item.getName().equals("Small fishing net"));
                    }
                    if( !getInventory().contains(303) || !getInventory().contains("Small fishing net") ) {
                        log("Needs Small fishing net");
                        getBank().withdraw(303,1);
                    }
                    if(getBank().open() && getInventory().contains(303) && !getInventory().isFull() ){
                        getBank().close();
                    }
                }
            case Fish:
                if ( !getInventory().isFull() && !FISHING_SPOT_LUMBRIDGE_SOUTH.contains((getLocalPlayer() ))) {
                    if (getWalking().walk(FISHING_SPOT_LUMBRIDGE_SOUTH.getRandomTile())) {
                        MethodProvider.sleepUntil(() -> getWalking().shouldWalk(), 10000);
                    }
                }
                NPC FishingSpot = getNpcs().closest(1530);
                if ( FishingSpot != null && !getInventory().isFull() ) {
                    log("Fishing spot found");
                    FishingSpot.interact("Net");
                }else{
                    log("No fishing spot found");
                }
            case Pause:
                return Calculations.random(1,10000);
            }
        return Calculations.random(1000, 5000);
    }
}
