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

package zuo.biao.library.base;

import java.util.List;

import zuo.biao.library.interfaces.OnReachViewBorderListener;
import zuo.biao.library.interfaces.OnStopLoadListener;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.ui.xlistview.XListView;
import zuo.biao.library.ui.xlistview.XListView.IXListViewListener;
import android.view.View;
import android.widget.BaseAdapter;

/**基础http获取列表的Fragment
 * @author Lemon
 * @param <T> 数据模型(model/JavaBean)类
 * @param <BA> 管理XListView的Adapter
 * @use extends BaseHttpListFragment 并在子类onCreateView中调用lvBaseList.onRefresh();, 具体参考 .UserListFragment
 */
public abstract class BaseHttpListFragment<T, BA extends BaseAdapter> extends BaseListFragment<T, XListView, BA> 
implements HttpManager.OnHttpResponseListener, IXListViewListener, OnStopLoadListener {
	private static final String TAG = "BaseHttpListFragment";




	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initView() {
		super.initView();

		setAdapter(null);//ListView需要设置adapter才能显示header和footer
	}

	/**设置列表适配器
	 * @param adapter if (adapter != null && adapter instanceof BaseHttpAdapter) >> 预加载
	 */
	@SuppressWarnings("unchecked")
	public void setAdapter(BA adapter) {
		super.setAdapter(adapter);
		lvBaseList.showFooter(adapter != null);

		if (adapter != null && adapter instanceof zuo.biao.library.base.BaseAdapter) {
			((zuo.biao.library.base.BaseAdapter<T>) adapter).setOnReachViewBorderListener(new OnReachViewBorderListener(){
				@Override
				public void onReach(int type, View v) {
					if (type == TYPE_BOTTOM) {
						lvBaseList.onLoadMore();
					}
				}
			});
		}
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {
		super.initData();

	}

	/**
	 * 将Json串转为List（已在非UI线程中）
	 * *直接Json.parseArray(json, getCacheClass());可以省去这个方法，但由于可能json不完全符合parseArray条件，所以还是要保留。
	 * *比如json只有其中一部分能作为parseArray的字符串时，必须先提取出这段字符串再parseArray
	 */
	public abstract List<T> parseArray(String json);


	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	// Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {// 必须调用
		super.initEvent();
		setOnStopLoadListener(this);

		lvBaseList.setXListViewListener(this);
	}

	@Override
	public void onStopRefresh() {
		runUiThread(new Runnable() {

			@Override
			public void run() {
				lvBaseList.stopRefresh();
			}
		});
	}
	@Override
	public void onStopLoadMore(final boolean isHaveMore) {
		runUiThread(new Runnable() {

			@Override
			public void run() {
				lvBaseList.stopLoadMore(isHaveMore);
			}
		});
	}
	/**
	 * @param requestCode 请求码，自定义，同一个Activity中以实现接口方式发起多个网络请求时以状态码区分各个请求
	 * @param resultCode 服务器返回结果码
	 * @param json 服务器返回的Json串，用parseArray方法解析
	 */
	@Override
	public void onHttpRequestSuccess(int requestCode, int resultCode, final String json, final String resultJson) {
		runThread(TAG + "onHttpRequestSuccess", new Runnable() {

			@Override
			public void run() {
				onLoadSucceed(parseArray(json));
			}
		});
	}
	/**里面只有stopLoadData();showShortToast(R.string.get_failed); 不能满足需求时可重写该方法
	 * @param requestCode 请求码，自定义，同一个Activity中以实现接口方式发起多个网络请求时以状态码区分各个请求
	 * @param e OKHTTP中请求异常
	 */
	@Override
	public void onHttpRequestError(int requestCode, Exception e) {
		onLoadFailed(e);
	}
	

	// 系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// 类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// 类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	// Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	// 内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}