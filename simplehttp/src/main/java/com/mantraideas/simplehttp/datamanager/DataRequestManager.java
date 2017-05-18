package com.mantraideas.simplehttp.datamanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;
import com.mantraideas.simplehttp.datamanager.error.DataManagerException;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yubraj on 1/27/17.
 */

public class DataRequestManager<T extends Object> {

    private Context context;
    private ServerRequestHandler serverRequestHandler;
    private DataRequest request;
    private SharedPreferences preference;
    private OnDataRecievedListener recivedListener;
    private OnDataRecievedProgressListener progressListener;
    T clazzz;

    private DataRequestManager(Context context, Class<?> mClass) {
        try {
            this.clazzz = (T) mClass.newInstance();
            this.context = context;
            if (context == null) {
                throw new DataManagerException("context cannot be null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        preference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void addOnDataRecieveListner(OnDataRecievedListener listener) {
        this.recivedListener = listener;
    }

    public void addOnDataRecieveListner(OnDataRecievedProgressListener listener) {
        this.progressListener = listener;
    }

    public void addOnDataRecieveListner(OnDataRecievedListener recivedListener, OnDataRecievedProgressListener progressListener) {
        this.recivedListener = recivedListener;
        this.progressListener = progressListener;
    }

    public static DataRequestManager getInstance(Context context, Class<?> mClass) {
        return new DataRequestManager(context, mClass);
    }

    public DataRequestManager addRequestBody(DataRequest request) {
        this.request = request;
        return this;
    }

    public void sync() {
        // first check internet connection
        // check the request is null or not
        // check the flag force is on or off
        // if the force flag in on then check the minimum servercall time
        Response response = canRequest();
        if (response == Response.OK) {
            new mAsync().execute();
        } else {
            exitFromDataManager(response, null);
        }
    }

    private void exitFromDataManager(Response response, Object object) {
        if (recivedListener != null) {
            recivedListener.onDataRecieved(response, object);
        }
    }

    private Response canRequest() {
        if (request == null) {
            return Response.NULL_REQUEST;
        }
        if (!DmUtilities.isNetworkConnected(context)) {
            return Response.NO_INTERNET;
        }
        if (!request.isForce() && preference.contains(clazzz.getClass().getSimpleName()) &&
                (System.currentTimeMillis() < (preference.getLong(clazzz.getClass().getSimpleName(), System.currentTimeMillis()) + request.getMinimumServerCallTime()))) {
            return Response.SERVER_CALL_TIME_NOT_VALID;
        }
        return Response.OK;
    }

    class mAsync extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {
            serverRequestHandler = new ServerRequestHandler(request, progressListener);
            if(request.getMethod() == null){
                throw new DataManagerException("Please provide the valid method for eg GET, POST, PUT or DELETE");
            }
            try {
                if (!request.getMethod().equals("GET") && progressListener != null) {
                    Log.d("SimpleHttp", "Currently OnDataRecieved Progress listener is available only in the GET request method. for more queries please contact to author");
                }
                if (request.getMethod() == Method.GET) {
                    return serverRequestHandler.get();
                } else if (request.getMethod() == Method.PUT) {
                    return serverRequestHandler.put();
                } else if (request.getMethod() == Method.POST) {
                    return serverRequestHandler.post();
                } else if (request.getMethod() == Method.DELETE) {
                    return serverRequestHandler.delete();
                } else {
                    return "{}";
                }
            }catch (DataManagerException e){
                e.printStackTrace();
                return "{}";
            }
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            try {
                Object json = new JSONTokener(string).nextValue();
                boolean success = json instanceof JSONArray || json instanceof JSONObject;
                DmUtilities.trace("DataRequestManager, request Successed = " + success);
                if (success) {
                    if (clazzz instanceof String) {
                        DmUtilities.trace("DataRequestManager, is String true");
                        exitFromDataManager(Response.OK, string);
                    } else if (json instanceof JSONObject) {
                        Constructor<?> constructor = clazzz.getClass().getConstructor(JSONObject.class);
                        exitFromDataManager(Response.OK, constructor.newInstance(json));
                    } else if (json instanceof JSONArray) {
                        Constructor<?> constructor = clazzz.getClass().getConstructor(JSONObject.class);
                        JSONArray jsonArray = (JSONArray) json;
                        List<T> modelList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            modelList.add((T) constructor.newInstance(jsonArray.optJSONObject(i)));
                        }
                        exitFromDataManager(Response.OK, modelList);
                    }
                } else {
                    exitFromDataManager(Response.NULL_DATA, string);
                }
                if (request.getMinimumServerCallTime() > 0) {
                    setLastServerCallTime();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                exitFromDataManager(Response.ERROR, string);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                exitFromDataManager(Response.ERROR, string);
            } catch (InstantiationException e) {
                e.printStackTrace();
                exitFromDataManager(Response.ERROR, string);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                exitFromDataManager(Response.ERROR, string);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                exitFromDataManager(Response.ERROR, string);
            }
        }

    }

    private void setLastServerCallTime() {
        String key = clazzz.getClass().getSimpleName();
        DmUtilities.trace("DataRequestmanager,  serverCall time saved in key = " + key);
        preference.edit().putLong(clazzz.getClass().getSimpleName(), System.currentTimeMillis()).commit();
    }

}
