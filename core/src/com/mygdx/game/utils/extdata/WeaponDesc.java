package com.mygdx.game.utils.extdata;

import com.mygdx.game.content.objects.items.Item;
import com.mygdx.game.content.objects.items.Weapon;

public class WeaponDesc extends EchipDesc {
    //************
//    int dmg; // inclus in mods
    boolean melee, onehand;
    int atkcost;
    //*******************

    public WeaponDesc() {}

    @Override
    public void to(Item it) {
        super.to(it);

        Weapon wit= (Weapon) it;
//        wit.dmg= dmg;
        wit.melee= melee;
        wit.onehanded = onehand;
        wit.atkcost =atkcost;
    }
}
