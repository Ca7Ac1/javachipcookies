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

    private final int STRATAGIES = 5;

    private Coords[] scoutSpawn;
    private Coords[] demoSpawn;

    private int[] scoutAmount;
    private int[] demoAmount;
    private boolean[] split;
    private int[] score;
    private int[] cost;


    public Attack() {
        scoutAmount = new int[STRATAGIES];
        demoAmount = new int[STRATAGIES];
        score = new int[STRATAGIES];

        initSpawns();
        initStratagies();
    }

    private void initSpawns() {
        scoutSpawn = new Coords[10];
        demoSpawn = new Coords[10];
    }

    private void initStratagies() {
        // STRAT 1 //
        scoutAmount[0] = 4;
        demoAmount[0] = 0;
        score[0] = 30;
        cost[0] = 4;

        // 2 //
        scoutAmount[1] = 7;
        demoAmount[1] = 0;
        score[1] = 30;
        cost[1] = 7;

        // 3 //
        scoutAmount[2] = 12;
        demoAmount[2] = 0;
        score[2] = 30;
        cost[2] = 12;

        // 4 //
        scoutAmount[3] = 12;
        demoAmount[3] = 0;
        score[3] = 30;
        cost[3] = 12;
        
        // 5 //
        scoutAmount[4] = 12;
        demoAmount[4] = 0;
        score[4] = 30;
        cost[4] = 12;
    }

    private int chooseLayout(GameState state) {
        int best = 0;
        for (int i = 0; i < STRATAGIES; i++) {

            // if (cost[best] > state.data.p1Stats.bits && cost[i] < cost[best]) {
            //     best = i;
            //     continue;
            // }

            // if (cost[i] > state.data.p1Stats.bits) {
            //     continue;
            // }

            if (score[i] > score[best] || (score[i] == score[best] && cost[i] < cost[best])) {
                best = i;
            }
        }
        return best;
    }
 

    private void deployLayout(GameState state, int layout) {
        if (cost[layout] <= state.data.p1Stats.bits) {
            
            Coords bestLoc = leastDamageSpawnLocation(state, List.of(new Coords(13, 0), new Coords(14, 0)));
            int total = scoutAmount[layout] + demoAmount[layout];
            for (int i = 0; i < scoutAmount[layout]; i++ ) {
                spawn(state, bestLoc, UnitType.Scout);
            }
            for (int i = 0; i < demoAmount[layout]; i++ ) {
                spawn(state, bestLoc, UnitType.Demolisher);
            }

        }
    }  


    private void spawn(GameState state, Coords c, UnitType unit) {
        state.attemptSpawn(c, unit);
    }

    //TODO: Account for shields gained from supports
    private Coords leastDamageSpawnLocation(GameState move, List<Coords> locations) {
        List<Float> damages = new ArrayList<>();

        for (Coords location : locations) {
            List<Coords> path = move.pathfind(location, MapBounds.getEdgeFromStart(location));
            float totalDamage = 0;
            for (Coords dmgLoc : path) {
                List<Unit> attackers = move.getAttackers(dmgLoc);
                for (Unit unit : attackers) {
                    totalDamage += unit.unitInformation.attackDamageWalker.orElse(0);
                }
            }
            GameIO.debug().println("Got dmg:" + totalDamage + " for " + location);
            damages.add(totalDamage);
        }

        int minIndex = 0;
        float minDamage = 9999999;
        for (int i = 0; i < damages.size(); i++) {
            if (damages.get(i) <= minDamage) {
                minDamage = damages.get(i);
                minIndex = i;
            }
        }
        return locations.get(minIndex);
    }
}