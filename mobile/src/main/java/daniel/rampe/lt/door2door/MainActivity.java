package daniel.rampe.lt.door2door;

import android.os.Bundle;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class MainActivity extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle bundle) {
        MaterialSection home = newSection("Home", MainActivityFragment_.builder().build());
        addSection(home);
    }
}
