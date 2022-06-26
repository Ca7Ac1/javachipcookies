package com.c1games.terminal.starteralgo;

import com.c1games.terminal.algo.*;
import com.c1games.terminal.algo.io.GameLoop;
import com.c1games.terminal.algo.io.GameLoopDriver;
import com.c1games.terminal.algo.map.GameState;
import com.c1games.terminal.algo.map.MapBounds;
import com.c1games.terminal.algo.map.Unit;
import com.c1games.terminal.algo.units.UnitType;

import java.util.*;

public class Attack {

    private final int STRATAGIES = 4;

    private ArrayList<ArrayList<Coords>> scoutSpawns;
    private ArrayList<ArrayList<Coords>> demoSpawns;
    
    private ArrayList<ArrayList<Integer>> scoutAmount;
    private ArrayList<ArrayList<Integer>> demoAmount;

    private int[] score;
    private int[] cost;

    private int currentLayout;

    private void initStratagies() {
        
        // STRAT 0 // (early game)
        scoutSpawns.get(0).add(new Coords(12, 1));
        scoutAmount.get(0).add(4);
        
        score[0] = 30;
        cost[0] = 4;

        // 1 // (early game)
        scoutSpawns.get(1).add(new Coords(15, 1));
        scoutAmount.get(1).add(9);
        
        score[1] = 30;
        cost[1] = 9;

        // 2 // (left staircase attack)
        scoutSpawns.get(2).add(new Coords(15, 1)); 
        scoutAmount.get(2).add(10);

        scoutSpawns.get(2).add(new Coords(16, 2));
        scoutAmount.get(2).add(7);

        scoutSpawns.get(2).add(new Coords(17, 3)); 
        scoutAmount.get(2).add(3);
  
        score[2] = 30;
        cost[2] = 20;

        // 3 // (right staircase attack)
        scoutSpawns.get(3).add(new Coords(10, 3)); // 10
        scoutAmount.get(3).add(10);
        
        scoutSpawns.get(3).add(new Coords(11, 2)); // 7
        scoutAmount.get(3).add(7);

        scoutSpawns.get(3).add(new Coords(12, 1)); // 3
        scoutAmount.get(3).add(3);

        score[3] = 30;
        cost[3] = 20;

        // // 2 //
        // scoutAmount[2] = 14;
        // demoAmount[2] = 0;
        // score[2] = 30;
        // cost[2] = 14;
        // split[2] = false;

        // // 3 //
        // scoutAmount[3] = 14;
        // demoAmount[3] = 0;
        // score[3] = 30;
        // cost[3] = 14;
        // split[3] = true;

        // // 4 //
        // scoutAmount[4] = 18;
        // demoAmount[4] = 9;
        // score[4] = 30;
        // cost[4] = 45;
        // split[4] = false;

        // // 5 //
        // scoutAmount[5] = 23;
        // demoAmount[5] = 0;
        // score[5] = 30;
        // cost[5] = 23;
        // split[5] = true;

    }

    public Attack() {
        scoutSpawns = new ArrayList<ArrayList<Coords>>(STRATAGIES);
        demoSpawns = new ArrayList<ArrayList<Coords>>(STRATAGIES);

        scoutAmount = new ArrayList<ArrayList<Integer>>(STRATAGIES);
        demoAmount = new ArrayList<ArrayList<Integer>>(STRATAGIES);

        for (int i = 0; i < STRATAGIES; i++) {
            scoutSpawns.add(new ArrayList<Coords>());
            demoSpawns.add(new ArrayList<Coords>());

            scoutAmount.add(new ArrayList<Integer>());
            demoAmount.add(new ArrayList<Integer>());
        }
        
        score = new int[STRATAGIES];
        cost = new int[STRATAGIES];

        currentLayout = 0;

        initStratagies();
    }

    public boolean startTurn(GameState state) {
        int best = 0;
        for (int i = 0; i < STRATAGIES; i++) {
            if (score[i] > score[best] || (score[i] == score[best] && cost[i] < cost[best])) {
                best = i;
            }
        }

        if (cost[best] > state.data.p1Stats.bits) {
            return false;
        }
        
        currentLayout = best;
        deployLayout(state, best);
        
        return true;
    }

    public void endTurn(int attackScore) {
        score[currentLayout] += attackScore;
    }

    private void deployLayout(GameState state, int layout) {
        for (int i = 0; i < scoutSpawns.get(layout).size(); i++) {  
            spawn(state, scoutSpawns.get(layout).get(i), UnitType.Scout, scoutAmount.get(layout).get(i));
        }

        for (int i = 0; i < demoSpawns.get(layout).size(); i++) {  
            spawn(state, demoSpawns.get(layout).get(i), UnitType.Demolisher, demoAmount.get(layout).get(i));
        }
    }

    private void spawn(GameState state, Coords c, UnitType unit, int amt) {
        for (int i = 0; i < amt; i++) {
            state.attemptSpawn(c, unit);
        }
    }
}