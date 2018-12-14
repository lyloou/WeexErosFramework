package com.eros.framework.proxy;

import android.app.Activity;

import com.eros.framework.constant.Constant;
import com.eros.framework.constant.WXEventCenter;
import com.eros.framework.manager.ManagerFactory;
import com.eros.framework.manager.impl.ParseManager;
import com.eros.framework.manager.impl.dispatcher.DispatchEventManager;
import com.eros.framework.model.RouterModel;
import com.eros.framework.model.WeexEventBean;
import com.renrenyoupin.activity.h5.api.listener.OnNavigateListener;

import org.json.JSONException;
import org.json.JSONObject;

public class NavigateListenerProxy implements OnNavigateListener {

    private Activity mActivity;

    public NavigateListenerProxy(Activity activity) {
        mActivity = activity;
    }

    @Override
    public boolean consume(Object requestData) {
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(requestData));
            String url = jsonObject.optString("url");
            String title = jsonObject.optString("title");
            boolean finishCurrentPage = jsonObject.optBoolean("finishCurrentPage");
            JSONObject param = jsonObject.optJSONObject("param");

            RouterModel router = new RouterModel(url, Constant.ACTIVITIES_ANIMATION
                .ANIMATION_PUSH, param, title, false, null);
            DispatchEventManager dispatchEventManager = ManagerFactory.getManagerService
                (DispatchEventManager.class);
            WeexEventBean eventBean = new WeexEventBean();
            eventBean.setKey(WXEventCenter.EVENT_OPEN);
            eventBean.setJsParams(ManagerFactory.getManagerService(ParseManager.class)
                .toJsonString(router));
            eventBean.setContext(mActivity);
            dispatchEventManager.getBus().post(eventBean);
            if (finishCurrentPage) {
                mActivity.finish();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }
}
