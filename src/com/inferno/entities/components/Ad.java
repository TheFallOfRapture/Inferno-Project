package com.inferno.entities.components;

import com.morph.engine.entities.Component;

/**
 * Created by Fernando on 3/6/2017.
 */
public class Ad extends Component {
    @Override
    public Component clone() {
        return new Ad();
    }
}
