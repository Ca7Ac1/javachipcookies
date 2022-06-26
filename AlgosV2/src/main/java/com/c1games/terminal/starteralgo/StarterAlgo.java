package com.c1games.terminal.starteralgo;

import com.c1games.terminal.algo.*;
import com.c1games.terminal.algo.io.GameLoop;
import com.c1games.terminal.algo.io.GameLoopDriver;
import com.c1games.terminal.algo.map.GameState;
import com.c1games.terminal.algo.map.MapBounds;
import com.c1games.terminal.algo.map.Unit;
import com.c1games.terminal.algo.units.UnitType;

import com.c1games.terminal.starteralgo.*;

import java.util.*;

/**
 * Java implementation of the standard starter algo.
 */
public class StarterAlgo implements GameLoop {
    private Attack attack;
    private Defences defend;

    boolean prevAttacked = false;

    private final Random rand = new Random();

    public static void main(String[] args) {        
        new GameLoopDriver(new StarterAlgo()).run();
    }

    private ArrayList<Coords> damagedLocations;
    private ArrayList<Coords> scoredLocations;

    @Override
    public void initialize(GameIO io, Config config) {
        attack = new Attack();
        defend = new Defences();

        damagedLocations = new ArrayList<Coords>();
        scoredLocations = new ArrayList<Coords>();

        GameIO.debug().println("Configuring your custom java algo strategy...");
        long seed = rand.nextLong();
        rand.setSeed(seed);
        GameIO.debug().println("Set random seed to: " + seed);
    }

    /**
     * Make a move in the game.
     */
    @Override
    public void onTurn(GameIO io, GameState move) {
        GameIO.debug().println("Performing turn " + move.data.turnInfo.turnNumber + " of your custom algo strategy");

        int damageScore = damagedLocations.size();
        int attackScore = scoredLocations.size();
        if (prevAttacked && attackScore == 0) {
            attackScore = -1;
        }

        defend.endTurn(damageScore, attackScore);
        attack.endTurn(attackScore);

        prevAttacked = attack.startTurn(state);
        defend.startTurn(state, prevAttacked);

        scoredLocations.clear();
        damagedLocations.clear();
    }

    /**
     * Save process action frames. Careful there are many action frames per turn!
     */
    @Override
    public void onActionFrame(GameIO io, GameState move) {
        // Save locations that the enemy scored on against us to reactively build defenses
        for (FrameData.Events.BreachEvent breach : move.data.events.breach) {
            if (breach.unitOwner == PlayerId.Player1) {
                scoredLocations.add(breach.coords);
            } else {
                damagedLocations.add(breach.coords);
            }
        }
    }
}
