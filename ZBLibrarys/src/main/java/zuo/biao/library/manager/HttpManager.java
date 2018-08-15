/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.library.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.util.ImageUtils;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.MD5Util;
import zuo.biao.library.util.SDPathUtils;
import zuo.biao.library.util.StringUtil;


/**
 * HTTP请求管理类
 *
 * @author Lemon
 * @use HttpManager.getInstance().get(...)或HttpManager.getInstance().post(...)  > 在回调方法onHttpRequestSuccess和onHttpRequestError处理HTTP请求结果
 * @must 解决getToken，getResponseCode，getResponseData中的TODO
 */
public class HttpManager {
    private static final String TAG = "HttpManager";
    private BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<Runnable>();
    private ThreadPoolExecutor exec = new ThreadPoolExecutor(10, 10, 1, TimeUnit.DAYS, blockingQueue);

    /**
     * 网络请求回调接口
     */
    public interface OnHttpResponseListener {
        /**
         * @param requestCode 请求码，自定义，同一个Activity中以实现接口方式发起多个网络请求时以状态码区分各个请求
         * @param resultCode  服务器返回结果码
         * @param resultData  服务器返回的Json串
         * @param resultJson  (全)服务器返回的Json串
         */
        void onHttpRequestSuccess(int requestCode, int resultCode, String resultData, String resultJson);

        /**
         * @param requestCode 请求码，自定义，同一个Activity中以实现接口方式发起多个网络请求时以状态码区分各个请求
         * @param exception   OKHTTP中请求异常
         */
        void onHttpRequestError(int requestCode, Exception exception);
    }


    private Context context;
    private static HttpManager instance;// 单例

    private HttpManager(Context context) {
        this.context = context;
    }

    public synchronized static HttpManager getInstance() {
        if (instance == null) {
            instance = new HttpManager(BaseApplication.getInstance());
        }
        return instance;
    }


    /**
     * 列表首页页码。有些服务器设置为1，即列表页码从1开始
     */
    public static final int PAGE_NUM_0 = 0;


    /**
     * @param paramList   请求参数列表，（可以一个键对应多个值）
     * @param url         接口url
     * @param requestCode 请求码，类似onActivityResult中请求码，当同一activity中以实现接口方式<br/>
     *                    ， 发起多个网络请求时，请求结束后都会回调
     *                    {@link OnHttpResponseListener#onHttpRequestError(int, Exception)}
     *                    或 <br/>
     *                    {@link OnHttpResponseListener#onHttpRequestError(int, Exception)}
     *                    <br/>
     *                    在activity中可以以requestCode来区分各个请求，serverResultCode是服务器返回的状态码，
     *                    json是数据json，可能为 空字符串
     * @param listener
     */
    public void post(final List<Parameter> paramList, final String url,
                     final int requestCode, final OnHttpResponseListener listener) {

        new AsyncTask<Void, Void, Exception>() {

            int resultCode;
            String resultData;
            String resultJson;

            @Override
            protected Exception doInBackground(Void... params) {
                OkHttpClient client = getHttpClient(url);
                if (client == null) {
                    return new Exception("httpPost  AsyncTask.doInBackground  client == null >> return;");
                }
                FormBody.Builder fBuilder = new FormBody.Builder();

                if (paramList != null) {
                    for (Parameter p : paramList) {
                        fBuilder.add(StringUtil.getTrimedString(p.key), StringUtil.getTrimedString(p.value));
                        Log.e("Param:" + p.key, p.value + "");
                    }
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = getResponseObject(client, new Request.Builder()
                            .url(StringUtil.getNoBlankString(url))
                            .post(fBuilder.build()).build());
//					.addHeader("token", getToken(paramList))
                } catch (Exception e) {
                    Log.e(TAG, "httpPost  AsyncTask.doInBackground  try {  jsonObject = getResponseObject(..." +
                            "} catch (Exception e) {\n" + e.getMessage());
                    return e;
                }

                resultCode = getResponseCode(jsonObject);
                resultData = getResponseData(jsonObject);
                resultJson = getResponseJson(jsonObject);

                return null;
            }

            @Override
            protected void onPostExecute(Exception exception) {
                super.onPostExecute(exception);
                httpPostExecute(listener, requestCode, exception, resultCode, resultData, resultJson);
            }

        }.executeOnExecutor(exec);
    }


    /**
     * @param paramList   请求参数列表，（可以一个键对应多个值）
     * @param url         接口url
     * @param requestCode 请求码，类似onActivityResult中请求码，当同一activity中以实现接口方式<br/>
     *                    ， 发起多个网络请求时，请求结束后都会回调
     *                    {@link OnHttpResponseListener#onHttpRequestError(int, Exception)}
     *                    或 <br/>
     *                    {@link OnHttpResponseListener#onHttpRequestError(int, Exception)}
     *                    <br/>
     *                    在activity中可以以requestCode来区分各个请求，serverResultCode是服务器返回的状态码，
     *                    json是数据json，可能为 空字符串
     * @param listener
     */
    public void get(final List<Parameter> paramList, final String url,
                    final int requestCode, final OnHttpResponseListener listener) {

        new AsyncTask<Void, Void, Exception>() {

            int resultCode;
            String resultData;
            String resultJson;

            @Override
            protected Exception doInBackground(Void... params) {
                OkHttpClient client = getHttpClient(url);
                if (client == null) {
                    return new Exception("httpGet  AsyncTask.doInBackground  client == null >> return;");
                }

                StringBuffer sb = new StringBuffer();
                sb.append(StringUtil.getNoBlankString(url));
                if (paramList != null) {
                    Parameter parameter;
                    for (int i = 0; i < paramList.size(); i++) {
                        parameter = paramList.get(i);
                        sb.append(i <= 0 ? "?" : "&");
                        sb.append(StringUtil.getTrimedString(parameter.key));
                        sb.append("=");
                        sb.append(StringUtil.getTrimedString(parameter.value));
                        Log.e(TAG, "Parameter::" + StringUtil.getTrimedString(parameter.key) + "=" + StringUtil.getTrimedString(parameter.value));
                    }
                }
                Log.e(TAG, "HttpUrl::" + sb.toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = getResponseObject(client, new Request.Builder()
                            .addHeader("token", getToken(paramList))
                            .url(sb.toString()).build());
                } catch (Exception e) {
                    Log.e(TAG, "httpGet  AsyncTask.doInBackground  try {  jsonObject = getResponseObject(..." +
                            "} catch (Exception e) {\n" + e.getMessage());
                    return e;
                }

                resultCode = getResponseCode(jsonObject);
                resultData = getResponseData(jsonObject);
                resultJson = getResponseJson(jsonObject);

                return null;
            }

            @Override
            protected void onPostExecute(Exception exception) {
                super.onPostExecute(exception);
                httpPostExecute(listener, requestCode, exception, resultCode, resultData, resultJson);
            }

        }.executeOnExecutor(exec);
    }

    //上传图片upload 内调用方法 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void upload(final Context context, final List<Parameter> paramList, final String url,
                       final int requestCode, final OnHttpResponseListener listener) {
        new AsyncTask<Void, Void, Exception>() {

            int resultCode;
            String resultData;
            String resultJson;

            @Override
            protected Exception doInBackground(Void... params) {
                OkHttpClient client = getHttpClient(url);
                if (client == null) {
                    return new Exception("httpPost  AsyncTask.doInBackground  client == null >> return;");
                }

                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                if (paramList != null) {
                    for (Parameter p : paramList) {
                        if (p.value instanceof File) {
                            File file = (File) p.value;
                            android.util.Log.e("上传图片", ">>" + p.key );
                            builder.addFormDataPart(p.key , file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
                        } else {
                            builder.addFormDataPart(p.key, p.value + "");
                        }
                    }
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = getResponseObject(client, new Request.Builder()
                            .addHeader("token", getToken(paramList)).url(StringUtil.getNoBlankString(url))
                            .post(builder.build()).build());
                } catch (Exception e) {
                    Log.e(TAG, "httpPost  AsyncTask.doInBackground  try {  jsonObject = getResponseObject(..." +
                            "} catch (Exception e) {\n" + e.getMessage());
                    return e;
                }

                resultCode = getResponseCode(jsonObject);
                resultData = getResponseData(jsonObject);
                resultJson = getResponseJson(jsonObject);

                return null;
            }

            @Override
            protected void onPostExecute(Exception exception) {
                super.onPostExecute(exception);
                httpPostExecute(listener, requestCode, exception, resultCode, resultData, resultJson);
            }

        }.executeOnExecutor(exec);
    }
    //上传图片upload 内调用方法 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //httpGet/httpPost 内调用方法 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * @param url
     * @return
     */
    private OkHttpClient getHttpClient(String url) {
        Log.i(TAG, "getHttpClient  url = " + url);
        if (StringUtil.isNotEmpty(url, true) == false) {
            Log.e(TAG, "getHttpClient  StringUtil.isNotEmpty(url, true) == false >> return null;");
            return null;
        }

        OkHttpClient client = new OkHttpClient();

        return client;
    }

    /**
     * @param paramList
     * @return
     * @must demo_***改为服务器设定值
     */
    public String getToken(List<Parameter> paramList) {
        if (paramList == null) {
            return "";
        }

        String token = "";
        Parameter p;
        for (int i = 0; i < paramList.size(); i++) {
            if (i > 0) {
                token += "&";
            }
            p = paramList.get(i);
            token += (p.key + "=" + p.value);
        }
        token += "demo_***";//TODO 这里的demo_***改为你自己服务器的设定值
        return MD5Util.MD5(token);
    }

    private static final String KEY_COOKIE = "cookie";

    /**
     * @return
     */
    public String getCookie() {
        return context.getSharedPreferences(KEY_COOKIE, Context.MODE_PRIVATE).getString(KEY_COOKIE, "");
    }

    /**
     * @param value
     */
    public void saveCookie(String value) {
        context.getSharedPreferences(KEY_COOKIE, Context.MODE_PRIVATE).edit().putString(KEY_COOKIE, value).commit();
    }


    /**
     * @param client
     * @param request
     * @return
     * @throws Exception
     */
    private JSONObject getResponseObject(OkHttpClient client, Request request) throws Exception {
        if (client == null || request == null) {
            Log.e(TAG, "getResponseObject  client == null || request == null >> return null;");
            return null;
        }
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception ex) {
            Log.e(TAG, "ex::" + ex);
        }
        String result = response.body().string();
        return response.isSuccessful() ? new JSONObject(result) : null;
    }

    /**
     * @param object
     * @return
     */
    private int getResponseCode(JSONObject object) {
        try {
            return object.getInt("status");//TODO status 改为你服务器设定的key
        } catch (Exception e) {
            Log.e(TAG, "getResponseCode No value for result");
        }
        return 0;
    }

    /**
     * @param object
     * @return
     */
    private String getResponseData(JSONObject object) {
        try {
            //return object.getString("data");//TODO data 改为你服务器设定的key
            return object + "";
        } catch (Exception e) {
            Log.e(TAG, "getResponseData No value for data");
        }
        return null;
    }

    /**
     * @param object
     * @return
     */
    private String getResponseJson(JSONObject object) {
        try {
            return object + "";//TODO json
        } catch (Exception e) {
            Log.e(TAG, "getResponseJson json");
        }
        return null;
    }

    /**
     * @param listener
     * @param requestCode
     * @param exception
     * @param resultCode
     * @param resultData
     */
    private void httpPostExecute(OnHttpResponseListener listener, int requestCode
            , Exception exception, int resultCode, String resultData, String resultJson) {
        if (listener == null) {
            Log.e(TAG, "httpPostExecute  listener == null >> return;");
            return;
        }

        if (exception == null || resultCode > 0 || StringUtil.isNotEmpty(resultData, true)) {
            Log.e(TAG, "httpPostExecute\nrequestCode = "
                    + requestCode + "; \nresultCode = " + resultCode + "; \nresult = "
                    + resultData + ";\n");
            listener.onHttpRequestSuccess(requestCode, resultCode, resultData, resultJson);
        } else {
            Log.e(TAG, "httpPostExecute requestCode = "
                    + requestCode + "\n  exception = <<<<<<<<<<<<<<<<<<<< \n"
                    + exception.getMessage() + "\n>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
            listener.onHttpRequestError(requestCode, exception);
        }
    }

    //httpGet/httpPost 内调用方法 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    public class HttpHead extends CookieHandler {
        public HttpHead() {
        }

        @Override
        public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
            String cookie = getCookie();
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            map.putAll(requestHeaders);
            if (!TextUtils.isEmpty(cookie)) {
                List<String> cList = new ArrayList<String>();
                cList.add(cookie);
                map.put("Cookie", cList);
            }
            return map;
        }

        @Override
        public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
            List<String> list = responseHeaders.get("Set-Cookie");
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    String cookie = list.get(i);
                    if (cookie.startsWith("JSESSIONID")) {
                        saveCookie(list.get(i));
                        break;
                    }
                }
            }
        }
    }
}