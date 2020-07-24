package com.nebula;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;

@ScriptManifest(author = "Alexei", name = "DreamBot Skeleton Template Script", version = 1.0, description = "Example script for DreamBot", category = Category.MISC)
public class SkeletonScript extends AbstractScript {

    private enum State{
        Bank, Fish, Wait
    }

    private State getAction(){
        if( true ){
            return State.Bank;
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
                log("Walking to lumbridge bank");
                if (!BankLocation.LUMBRIDGE.getArea(2).contains(getLocalPlayer())) {
                    if (getWalking().walk(BankLocation.LUMBRIDGE.getArea(2).getRandomTile())) {
                        MethodProvider.sleepUntil(() -> getWalking().shouldWalk(), 4000);
                    }
                }
        }
        return Calculations.random(500, 600);
    }

    public int bank() {
        return 0;
        }
    }
}
