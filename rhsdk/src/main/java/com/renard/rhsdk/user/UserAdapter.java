package com.renard.rhsdk.user;

import com.renard.rhsdk.insterface.UserInterface;

/**
 * Created by Riven_rabbit on 2020/9/17
 *
 * @author suyanan
 */
public abstract class UserAdapter implements UserInterface {

    @Override
    public void login() {
        // TODO Auto-generated method stub

    }

    @Override
    public void loginCustom(String customData) {
        // TODO Auto-generated method stub

    }

    @Override
    public void switchLogin() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean showAccountCenter() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void logout() {
        // TODO Auto-generated method stub

    }

    @Override
    public void submitExtraData(UserExtraData extraData) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void postGiftCode(String code) {

    }


    @Override
    public void realNameRegister() {
        // TODO Auto-generated method stub

    }

    @Override
    public void queryAntiAddiction() {
        // TODO Auto-generated method stub

    }

    @Override
    public abstract boolean isSupportMethod(String methodName);

}
