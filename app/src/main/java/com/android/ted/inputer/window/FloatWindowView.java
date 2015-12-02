package com.android.ted.inputer.window;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.ted.inputer.R;
import com.android.ted.inputer.db.SharePreData;
import com.android.ted.inputer.util.UiUtil;
import com.cocosw.favor.FavorAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Created by Ted on 2015/12/1.
 * 圆形提示视图
 */
public class FloatWindowView extends FrameLayout {

    private Context mContext;
    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;
    private float xMoveInScreen;
    private float yMoveInScreen;
    private float xStartInScreen;
    private float yStartInScreen;
    private float xInView;
    private float yInView;
    private int statusBarHeight = 0;

    private FloatBtnType mFloatBtnType = FloatBtnType.SUGGEST;


    /**
     * 打开大悬浮窗，同时关闭小悬浮窗。
     */
    private void onClickFloatBtn() {
        if(getFloatBtnType().equals(FloatBtnType.SUGGEST)){
            Toast.makeText(mContext, "点击了按钮", Toast.LENGTH_SHORT).show();
        }
    }


    public FloatWindowView(Context context) {
        super(context);
        initBase(context);
    }

    public FloatWindowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBase(context);
    }

    public FloatWindowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBase(context);
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    public void initFloatBtnView(FloatBtnType type){
        setFloatBtnType(type);
        addView(makeFloatBtnView());
    }

    public FloatBtnType getFloatBtnType() {
        return mFloatBtnType;
    }

    public void setFloatBtnType(FloatBtnType floatBtnType) {
        mFloatBtnType = floatBtnType;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xStartInScreen = event.getRawX();
                yStartInScreen = event.getRawY() - statusBarHeight;
                xMoveInScreen = xStartInScreen;
                yMoveInScreen = yStartInScreen;
                break;
            case MotionEvent.ACTION_MOVE:
                xMoveInScreen = event.getRawX();
                yMoveInScreen = event.getRawY() - statusBarHeight;
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!UiUtil.isMoveAction(mContext, xStartInScreen, xMoveInScreen) &&
                        !UiUtil.isMoveAction(mContext, yStartInScreen, yMoveInScreen)) {
                    onClickFloatBtn();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void initBase(Context context){
        mContext = context;
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        statusBarHeight = UiUtil.getStatusBarHeight(context);
    }

    private void updateViewPosition() {
        mParams.x = (int) (xMoveInScreen - xInView);
        mParams.y = (int) (yMoveInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);

        SharePreData sharePreData = new FavorAdapter.Builder(mContext)
                .build().create(SharePreData.class);
        sharePreData.setFloatViewX(mParams.x);
        sharePreData.setFloatViewY(mParams.y);
    }

    /***
     * 构造视图
     * @return 视图对象
     */
    private FloatingActionButton makeFloatBtnView(){
        FloatingActionButton floatingActionButton = new FloatingActionButton(mContext);
        floatingActionButton.setClickable(false);
        if(getFloatBtnType().equals(FloatBtnType.SELECT)){
            floatingActionButton.setColorNormalResId(R.color.primary);
            floatingActionButton.setIcon(R.drawable.ic_done_white_24dp);
        }else if(getFloatBtnType().equals(FloatBtnType.SUGGEST)){
            floatingActionButton.setColorNormalResId(R.color.clipboard_anchor_color);
            floatingActionButton.setIcon(R.drawable.ic_menu_white_24dp);
        }else if(getFloatBtnType().equals(FloatBtnType.BACK)){
            floatingActionButton.setColorNormalResId(R.color.clipboard_anchor_color);
            floatingActionButton.setIcon(R.drawable.ic_undo_white_24dp);
        }
        return floatingActionButton;
    }

    public enum FloatBtnType {
        SUGGEST,SELECT,BACK
    }
}