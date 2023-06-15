package cn.example.foods;


import com.example.datastore.SettingsViewModel;

import javax.inject.Inject;

public class HiltTest {
    @Inject
    SettingsViewModel settingsViewModel;

    @Inject
    public HiltTest() {

    }
}
