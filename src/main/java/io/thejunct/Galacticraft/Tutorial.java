/*
 * Copyright (c) 2016 The Junction Network. All Rights Reserved.
 * Created by PantherMan594.
 */

package io.thejunct.Galacticraft;

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
