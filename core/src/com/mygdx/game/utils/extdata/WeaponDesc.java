package com.mygdx.game.utils.extdata;

import com.mygdx.game.content.objects.items.Item;
import com.mygdx.game.content.objects.items.Weapon;

public class WeaponDesc extends EchipDesc {
    //************
    int dmg;
    boolean melee, onehand;
    //*******************

    public WeaponDesc() {}

    @Override
    public void to(Item it) {
        super.to(it);

        Weapon wit= (Weapon) it;
        wit.dmg= dmg;
        wit.melee= melee;
        wit.onehand= onehand;
    }
}
