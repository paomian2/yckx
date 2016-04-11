package com.shizhefei.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

/**
 * <h1>鎳掑姞杞紽ragment</h1> 鍙湁鍒涘缓骞舵樉绀虹殑鏃跺�欐墠浼氳皟鐢╫nCreateViewLazy鏂规硶<br>
 * <br>
 * 
 * 鎳掑姞杞界殑鍘熺悊onCreateView鐨勬椂鍊橣ragment鏈夊彲鑳芥病鏈夋樉绀哄嚭鏉ャ��<br>
 * 浣嗘槸璋冪敤鍒皊etUserVisibleHint(boolean isVisibleToUser),isVisibleToUser =
 * true鐨勬椂鍊欏氨璇存槑鏈夋樉绀哄嚭鏉�<br>
 * 浣嗘槸瑕佽�冭檻onCreateView鍜宻etUserVisibleHint鐨勫厛鍚庨棶棰樻墍浠ユ墠鏈変簡涓嬮潰鐨勪唬鐮�
 * 
 * 娉ㄦ剰锛�<br>
 * 銆�1銆嬪師鍏堢殑Fragment鐨勫洖璋冩柟娉曞悕瀛楀悗闈㈣鍔犱釜Lazy锛屾瘮濡侳ragment鐨刼nCreateView鏂规硶锛� 灏卞啓鎴恛nCreateViewLazy <br>
 * 銆�2銆嬩娇鐢ㄨLazyFragment浼氬鑷村涓�灞傚竷灞�娣卞害
 * 
 * @author LuckyJayce
 *
 */
public class LazyFragment extends BaseFragment {
	private boolean isInit = false;
	private Bundle savedInstanceState;
	public static final String INTENT_BOOLEAN_LAZYLOAD = "intent_boolean_lazyLoad";
	private boolean isLazyLoad = true;
	public FrameLayout layout;
	public View useView;

	@Deprecated
	protected final void onCreateView(Bundle savedInstanceState) {
		super.onCreateView(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			isLazyLoad = bundle.getBoolean(INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
		}
		if (isLazyLoad) {
			if (getUserVisibleHint() && !isInit) {
				isInit = true;
				this.savedInstanceState = savedInstanceState;
				onCreateViewLazy(savedInstanceState);
			} else {
				layout = new FrameLayout(getApplicationContext());
				layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				super.setContentView(layout);
			}
		} else {
			isInit = true;
			onCreateViewLazy(savedInstanceState);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && !isInit && getContentView() != null) {
			isInit = true;
			onCreateViewLazy(savedInstanceState);
			onResumeLazy();
		}
		if (isInit && getContentView() != null) {
			if (isVisibleToUser) {
				isStart = true;
				onFragmentStartLazy();
			} else {
				isStart = false;
				onFragmentStopLazy();
			}
		}
	}

	@Deprecated
	@Override
	public final void onStart() {
		super.onStart();
		if (isInit && !isStart && getUserVisibleHint()) {
			isStart = true;
			onFragmentStartLazy();
		}
	}

	@Deprecated
	@Override
	public final void onStop() {
		super.onStop();
		if (isInit && isStart && getUserVisibleHint()) {
			isStart = false;
			onFragmentStopLazy();
		}
	}

	private boolean isStart = false;

	protected void onFragmentStartLazy() {

	}

	protected void onFragmentStopLazy() {

	}

	protected void onCreateViewLazy(Bundle savedInstanceState) {

	}

	protected void onResumeLazy() {

	}

	protected void onPauseLazy() {

	}

	protected void onDestroyViewLazy() {

	}

	@Override
	public void setContentView(int layoutResID) {
		if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
			layout.removeAllViews();
			View view = inflater.inflate(layoutResID, layout, false);
			useView=view;
			layout.addView(view);
		} else {
			super.setContentView(layoutResID);
		}
	}

	@Override
	public void setContentView(View view) {
		if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
			layout.removeAllViews();
			layout.addView(view);
		} else {
			super.setContentView(view);
		}
	}

	@Override
	@Deprecated
	public final void onResume() {
		super.onResume();
		if (isInit) {
			onResumeLazy();
		}
	}

	@Override
	@Deprecated
	public final void onPause() {
		super.onPause();
		if (isInit) {
			onPauseLazy();
		}
	}

	@Override
	@Deprecated
	public final void onDestroyView() {
		super.onDestroyView();
		if (isInit) {
			onDestroyViewLazy();
		}
		isInit = false;
	}
}
