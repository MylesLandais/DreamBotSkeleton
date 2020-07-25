package com.nebula;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;

@ScriptManifest(author = "Alexei", name = "DreamBot Skeleton Template Script", version = 1.1, description = "Example script for DreamBot", category = Category.MISC)
public class SkeletonScript extends AbstractScript {

    private enum State{
        Bank, Fish, Wait
    }

    private State getAction(){
        if( !getInventory().contains(303) || getInventory().isFull() ){
            log("Need something from bank");
            return State.Bank;
        }
        if( getInventory().contains(303) ){
            return State.Fish;
        }
        return State.Wait;
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
                    }
                    if( getInventory().isFull() ){
                        getDepositBox().depositAllItems();
                    }
                    if( !getInventory().contains(303) ) {
                        getBank().withdraw(303,1);
                    }
                    if(getBank().open()){
                        getBank().close();
                    }
                }
            case Fish:
                log("TODO Fishing code");
        }
        return Calculations.random(1000, 5000);
    }
}
