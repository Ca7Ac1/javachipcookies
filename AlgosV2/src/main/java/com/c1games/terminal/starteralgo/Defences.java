package com.c1games.terminal.starteralgo;

import com.c1games.terminal.algo.*;
import com.c1games.terminal.algo.io.GameLoop;
import com.c1games.terminal.algo.io.GameLoopDriver;
import com.c1games.terminal.algo.map.GameState;
import com.c1games.terminal.algo.map.MapBounds;
import com.c1games.terminal.algo.map.Unit;
import com.c1games.terminal.algo.units.UnitType;

import java.util.*;

public class Defences {

    private final int LAYOUTS = 12;

    // Excess points we have for deciding if we should upgrade
    // dummy values for now
    private final int UPGRADE_SUPPORTS = 15;
    private final int UPGRADE_TURRETS = 12;
    private final int UPGRADE_LAYOUT = 25;

    private ArrayList<Coords> mainTurrets;
    private ArrayList<Coords> mainSupports;

    private ArrayList<ArrayList<Coords>> wallLayout;
    private ArrayList<ArrayList<Coords>> turretLayout;
    private ArrayList<ArrayList<Coords>> supportLayout;

    private int[] score;
    private int[] cost;
    public int[] attack;

    private ArrayList<Coords> current;
    public int currentLayout;

    private void initMain() {
        mainTurrets.add(new Coords(4, 12));
        mainTurrets.add(new Coords(23, 12));
        mainTurrets.add(new Coords(11, 12));
        mainTurrets.add(new Coords(16, 12));

        mainSupports.add(new Coords(13, 1));
        mainSupports.add(new Coords(14, 1));
    }

    private void initLayouts() {
        // STAGE 0 //
        // CENTER WALL FUNNEL //
        for (int i = 0; i < 7; i++) {
            wallLayout.get(0).add(new Coords(i, 13));
            wallLayout.get(0).add(new Coords(27 - i, 13));
        }

        score[0] = 35;
        cost[0] = 14;
        attack[0] = 10;

        // STAGE 1 //
        // STAGE 0 + TURRETS //
        for (int i = 0; i < 7; i++) {
            wallLayout.get(1).add(new Coords(i, 13));
            wallLayout.get(1).add(new Coords(27 - i, 13));
        }

        turretLayout.get(1).add(new Coords(7, 13));
        turretLayout.get(1).add(new Coords(20, 13));

        score[1] = 40;
        cost[1] = 18;
        attack[1] = 15;

        // STAGE 2 //
        // LEFT WALLS //
        for (int i = 0; i < 12; i++) {
            wallLayout.get(2).add(new Coords(i, 13));
        }

        score[2] = 30;
        cost[2] = 12;
        attack[2] = 10;

        // STAGE 3 //
        // RIGHT WALLS //
        for (int i = 0; i < 12; i++) {
            wallLayout.get(3).add(new Coords(27 - i, 13));
        }

        score[3] = 30;
        cost[3] = 12;
        attack[3] = 10;

        // STAGE 4 //
        // COVER //
        for (int i = 0; i < 28; i++) {
            wallLayout.get(4).add(new Coords(i, 13));
        }

        score[4] = 35;
        cost[4] = 28;
        attack[4] = -35;

        // STAGE 5 //
        // ALL OUT DEFENSE //
        for (int i = 0; i < 28; i++) {
            wallLayout.get(5).add(new Coords(i, 13));
        }

        for (int i = 0; i < 26; i++) {
            wallLayout.get(5).add(new Coords(i + 1, 12));
        }

        score[5] = 45;
        cost[5] = 54;
        attack[5] = -35;

        // STAGE 6 //
        // STACK LEFT WALL + RIGHT TURRET //
        for (int i = 0; i < 16; i++) {
            wallLayout.get(6).add(new Coords(i, 13));
        }

        for (int i = 0; i < 15; i++) {
            wallLayout.get(6).add(new Coords(i + 1, 12));
        }

        turretLayout.get(6).add(new Coords(16, 12));
        turretLayout.get(6).add(new Coords(20, 12));
        turretLayout.get(6).add(new Coords(24, 12));

        score[6] = 42;
        cost[6] = 60;
        attack[6] = 15;

        // STAGE 7 //
        // STACK LEFT WALL + RIGHT TURRET //
        for (int i = 0; i < 16; i++) {
            wallLayout.get(7).add(new Coords(27 - i, 13));
        }

        for (int i = 0; i < 15; i++) {
            wallLayout.get(7).add(new Coords(26 - i, 12));
        }

        turretLayout.get(7).add(new Coords(16, 12));
        turretLayout.get(7).add(new Coords(20, 12));
        turretLayout.get(7).add(new Coords(24, 12));

        score[7] = 42;
        cost[7] = 60;
        attack[7] = 15;

        // STAGE 8 //
        // CLUSTERED TURRETS//
        for (int i = 0; i < 7; i++) {
            wallLayout.get(8).add(new Coords(i, 13));
            wallLayout.get(8).add(new Coords(27 - i, 13));
        }
        for (int i = 0; i < 18; i++) {
            wallLayout.get(8).add(new Coords(22 - i, 12));
        }

        turretLayout.get(8).add(new Coords(7, 13));
        turretLayout.get(8).add(new Coords(20, 13));
        turretLayout.get(8).add(new Coords(2, 12));
        turretLayout.get(8).add(new Coords(3, 11));
        turretLayout.get(8).add(new Coords(25, 12));
        turretLayout.get(8).add(new Coords(24, 11));

        score[8] = 45;
        cost[8] = 72;
        attack[8] = 15;

        // STAGE 9 //
        // TRENCH //
        for (int i = 0; i < 4; i++) {
            wallLayout.get(9).add(new Coords(i + 4, 6));
            wallLayout.get(9).add(new Coords(19 - i, 6));
        }
        wallLayout.get(9).add(new Coords(1, 13));
        wallLayout.get(9).add(new Coords(2, 12));
        wallLayout.get(9).add(new Coords(3, 11));
        wallLayout.get(9).add(new Coords(4, 10));
        wallLayout.get(9).add(new Coords(5, 9));
        wallLayout.get(9).add(new Coords(6, 8));
        wallLayout.get(9).add(new Coords(7, 7));

        wallLayout.get(9).add(new Coords(26, 13));
        wallLayout.get(9).add(new Coords(25, 12));
        wallLayout.get(9).add(new Coords(24, 11));
        wallLayout.get(9).add(new Coords(23, 10));
        wallLayout.get(9).add(new Coords(22, 9));
        wallLayout.get(9).add(new Coords(21, 8));
        wallLayout.get(9).add(new Coords(20, 7));

        turretLayout.get(9).add(new Coords(0, 13));
        turretLayout.get(9).add(new Coords(3, 10));
        turretLayout.get(9).add(new Coords(6, 7));
        turretLayout.get(9).add(new Coords(27, 13));
        turretLayout.get(9).add(new Coords(24, 10));

        score[9] = 40;
        cost[9] = 34;
        attack[9] = 15;

        // STAGE 10 //
        // STAIRCASE ATTACK (RIGHT) //
        for (int i = 0; i <= 6; i++) {
            wallLayout.get(11).add(new Coords(15 + i, 3 + i));
        }
        for (int i = 1; i <= 3; i++) {
            supportLayout.get(10).add(new Coords(21 + i, 9 + i));
        }
        wallLayout.get(11).add(new Coords(26, 13));
        wallLayout.get(11).add(new Coords(27, 13));
        
        wallLayout.get(10).add(new Coords(14, 2));
        wallLayout.get(10).add(new Coords(13, 2));
        wallLayout.get(10).add(new Coords(12, 3));
        wallLayout.get(10).add(new Coords(11, 4));
        wallLayout.get(10).add(new Coords(10, 4));

        score[10] = 30; 
        cost[10] = 45;
        attack[10] = 45;

        // STAGE 11 //
        // STAIRCASE ATTACK (LEFT) //

        for (int i = 0; i <= 6; i++) {
            wallLayout.get(11).add(new Coords(12 - i, 3 + i));
        }
        for (int i = 7; i <= 9; i++) {
            supportLayout.get(11).add(new Coords(12 - i, 3 + i)); 
        }
        wallLayout.get(11).add(new Coords(2, 13));
        wallLayout.get(11).add(new Coords(3, 13));

        wallLayout.get(11).add(new Coords(13, 2));
        wallLayout.get(11).add(new Coords(14, 2));
        wallLayout.get(11).add(new Coords(15, 3));
        wallLayout.get(11).add(new Coords(16, 4));
        wallLayout.get(11).add(new Coords(17, 4));

        score[11] = 30; 
        cost[11] = 45;
        attack[11] = 45;
    }

    public Defences() {
        mainTurrets = new ArrayList<Coords>();
        mainSupports = new ArrayList<Coords>();

        current = new ArrayList<Coords>();

        wallLayout = new ArrayList<ArrayList<Coords>>(LAYOUTS);
        turretLayout = new ArrayList<ArrayList<Coords>>(LAYOUTS);
        supportLayout = new ArrayList<ArrayList<Coords>>(LAYOUTS);

        score = new int[LAYOUTS];
        cost = new int[LAYOUTS];
        attack = new int[LAYOUTS];

        for (int i = 0; i < LAYOUTS; i++) {
            wallLayout.add(new ArrayList<Coords>());
            turretLayout.add(new ArrayList<Coords>());

            score[i] = 0;
            cost[i] = 0;
            attack[i] = 0;
        }

        currentLayout = 0;

        initMain();
        initLayouts();
    }

    public void startTurn(GameState state, boolean shouldAttack) {
        current.clear();
        deployMain(state);

        int best = 0;
        for (int i = 0; i < LAYOUTS; i++) {
            int prevScore = score[best];
            int currScore = score[i];

            if (shouldAttack) {
                prevScore += attack[best];
                currScore += attack[i];
            }

            if (cost[best] > state.data.p1Stats.cores && cost[i] < cost[best]) {
                best = i;
                continue;
            }

            if (cost[i] > state.data.p1Stats.cores) {
                continue;
            }

            if (currScore > prevScore || (currScore == prevScore && cost[i] < cost[best])) {
                best = i;
            }
        }
        
        currentLayout = best;
        deployLayout(state, best);

        int upgrade = 0;
        while (state.data.p1Stats.cores > UPGRADE_TURRETS && upgrade < mainTurrets.size()) {
            state.attemptUpgrade(mainTurrets.get(upgrade));

            upgrade++;
        }

        upgrade = 0;
        while (state.data.p1Stats.cores > UPGRADE_SUPPORTS && upgrade < mainSupports.size()) {
            state.attemptSpawn(mainSupports.get(upgrade), UnitType.Support);
            state.attemptUpgrade(mainSupports.get(upgrade));

            upgrade++;
        }

        upgrade = 0;
        while (state.data.p1Stats.cores > UPGRADE_LAYOUT && upgrade < current.size()) {
            state.attemptUpgrade(current.get(upgrade));

            upgrade++;
        }

        // while (state.data.p1Stats.cores > UPGRADE_LAYOUT && upgrade <
        // turretLayout.size() / 2) {
        // state.attemptUpgrade(turretLayout.get(upgrade));

        // upgrade++;
        // }

        // while (state.data.p1Stats.cores > UPGRADE_LAYOUT && upgrade <
        // wallLayout.size() / 2) {
        // state.attemptUpgrade(wallLayout.get(upgrade));

        // upgrade++;
        // }
    }

    public void endTurn(int damageTaken, int attackScore) {
        score[currentLayout] -= damageTaken;
        attack[currentLayout] += attackScore;
    }

    private void deployMain(GameState state) {
        spawn(state, mainTurrets, UnitType.Turret, false);
    }

    private void deployLayout(GameState state, int layout) {
        spawn(state, wallLayout.get(layout), UnitType.Wall, true);
        spawn(state, turretLayout.get(layout), UnitType.Turret, true);
        spawn(state, supportLayout.get(layout), UnitType.Support, true);

        refund(state);
    }

    // public int getLayouts() {
    // return LAYOUTS;
    // }

    private void spawn(GameState state, ArrayList<Coords> spawns, UnitType unit, boolean curr) {
        for (Coords c : spawns) {
            boolean spawned = state.attemptSpawn(c, unit);

            if (spawned && curr) {
                current.add(c);
            }
        }
    }

    private void refund(GameState state) {
        for (Coords c : current) {
            state.attemptRemoveStructure(c);
        }
    }
}