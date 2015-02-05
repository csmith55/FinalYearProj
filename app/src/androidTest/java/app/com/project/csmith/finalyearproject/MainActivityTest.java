package app.com.project.csmith.finalyearproject;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2 {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testOnCreate() throws Exception {
        MainActivity activity = (MainActivity) getActivity();

        assertEquals(R.id.splashFragment, activity.getFragments()[0].getId());
        assertEquals(R.id.selectionFragment,activity.getFragments()[1].getId());
        assertEquals(R.id.userSettingsFragment,activity.getFragments()[2].getId());

        assertTrue(activity.getFragments()[0].isHidden());
        assertTrue(activity.getFragments()[2].isHidden());
        assertTrue(activity.getFragments()[1].isVisible());


    }

    public void testOnPrepareOptionsMenu() throws Exception {



    }

    public void testOnOptionsItemSelected() throws Exception {

        MainActivity activity = (MainActivity) getActivity();

        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        for(int i =0; i < activity.getFragments().length; i++){
            transaction.hide(activity.getFragments()[i]);
        }




    }

    public void testOnResumeFragments() throws Exception {

    }

    public void testOnResume() throws Exception {

    }

    public void testOnPause() throws Exception {

    }

    public void testOnActivityResult() throws Exception {

    }

    public void testOnDestroy() throws Exception {

    }

    public void testOnSaveInstanceState() throws Exception {

    }

    public void testGetFragments() throws Exception {

    }

    public void testSetFragments() throws Exception {

    }

    public void testGetSettings() throws Exception {

    }

    public void testSetSettings() throws Exception {

    }

    public void testGetUiHelper() throws Exception {

    }

    public void testSetUiHelper() throws Exception {

    }
}