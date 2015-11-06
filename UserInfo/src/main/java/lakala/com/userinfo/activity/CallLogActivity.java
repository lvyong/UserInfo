package lakala.com.userinfo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import butterknife.Bind;
import butterknife.ButterKnife;
import lakala.com.userinfo.R;
import lakala.com.userinfo.bean.PhoneCallBean;
import lakala.com.userinfo.dao.CallLogDao;
import lakala.com.userinfo.itemdecoration.HorizontalDividerItemDecoration;
import lakala.com.userinfo.ui.GifMovieView;

/**
 * 通话记录
 *
 * Created by lvyong on 15/11/4.
 */
public class CallLogActivity extends BaseActivity {

    @Bind(R.id.activity_calllog_recyclerview) RecyclerView recyclerView;

    private GifMovieView gif;
    private View cordovaMaskView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calllog);
        initView();
        initData();
    }

    /**
     * 初始化view
     */
    private void initView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
    }

    /**
     *  设置数据
     */
    private void initData(){
        showMask();
        final Activity activity = this;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                CallLogDao callLogDao = new CallLogDao();
                final List<PhoneCallBean> beanList = callLogDao.getCallLog(activity);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideMask();
                        recyclerView.setAdapter(new CallLogAdapter(beanList));
                    }
                });
            }
        });
        scheduledExecutorService.shutdown();
    }


    /**
     * 显示遮罩
     *
     */
    protected void showMask() {
        getMaskView().setVisibility(View.VISIBLE);
        getGifInstance().setMovieResource(R.drawable.tam_loadpage);

        if (getGifInstance().isPaused()) {
            getGifInstance().setPaused(false);
        }
    }

    /**
     * 隐藏遮罩 View
     */
    protected void hideMask() {
        getMaskView().setVisibility(View.GONE);

        if (!getGifInstance().isPaused()) {
            getGifInstance().setPaused(true);
        }
    }

    /**
     * 获取遮罩 View
     *
     * @return View
     */
    private View getMaskView() {
        if (cordovaMaskView == null) {
            ViewStub viewStub = (ViewStub) findViewById(R.id.activity_calllog_header_mask);
            cordovaMaskView = viewStub.inflate();
        }
        return cordovaMaskView;
    }

    /**
     * 获取 GIF 引用
     *
     * @return GIF
     */
    private GifMovieView getGifInstance() {
        if (gif == null) {
            gif = (GifMovieView) getMaskView().findViewById(R.id.xlistview_header_mask_gifview);
        }
        return gif;
    }

    /**
     * 适配器
     */
     class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder> {

         List<PhoneCallBean> phoneCallBeanList;

        /**
         *
         * @param list
         */
        public CallLogAdapter (List<PhoneCallBean> list){
            this.phoneCallBeanList = list;
        }

        /**
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public CallLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CallLogViewHolder holder = new CallLogViewHolder(LayoutInflater.from(CallLogActivity.this).
                    inflate(R.layout.activity_calllog_list_item, parent, false));
            return holder;
        }

        /**
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(CallLogViewHolder holder, int position) {
            holder.setData(phoneCallBeanList.get(position));
        }

        /**
         * Returns the total number of items in the data set hold by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount() {
           if (null == phoneCallBeanList) {
               return 0;
           }
           return phoneCallBeanList.size();
        }


       class CallLogViewHolder extends  RecyclerView.ViewHolder {

            @Bind(R.id.activity_calllog_list_item_name) TextView name;
            @Bind(R.id.activity_calllog_list_item_phone) TextView phone;
            @Bind(R.id.activity_calllog_list_item_duration) TextView duration;
            @Bind(R.id.activity_calllog_list_item_time) TextView time;

            public CallLogViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }

            /**
             * 设置数据
             * @param phoneCallBean
             */
            public void setData(PhoneCallBean phoneCallBean){
                name.setText(phoneCallBean.getName());
                phone.setText(phoneCallBean.getPhoneNumber());
                duration.setText(phoneCallBean.getDuration()+"s");
                time.setText(phoneCallBean.getTime());
            }
        }
    }

}
