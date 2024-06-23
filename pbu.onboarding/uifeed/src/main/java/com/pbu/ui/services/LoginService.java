package com.pbu.ui.services;

import com.pbu.ui.models.LoginModel;
import com.pbu.ui.models.SettingsModel;

import java.util.List;

public interface LoginService {
    List<SettingsModel> getLoginSettings();
    LoginModel LoginUser(String username);
}

