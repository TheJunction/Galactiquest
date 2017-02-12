/*
 * Copyright (c) 2017 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.galactiquest;

/**
 * Created by david on 10/22.
 *
 * @author david
 */
class Tutorial {
    private GPlayer player;

    Tutorial(GPlayer player) {
        this.player = player;
        run();
    }

    private void run() {
        //TODO: Create the tutorial.
        new Spaceship(player);
    }
}
