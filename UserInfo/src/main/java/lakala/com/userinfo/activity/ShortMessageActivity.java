package lakala.com.userinfo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import lakala.com.userinfo.bean.MessageConversationBean;
import lakala.com.userinfo.dao.ShortMessageDao;
import lakala.com.userinfo.itemdecoration.HorizontalDividerItemDecoration;
import lakala.com.userinfo.ui.GifMovieView;

/**
 * 短信记录
 * Created by lvyong on 15/11/4.
 */
public class ShortMessageActivity extends BaseActivity {
    @Bind(R.id.activity_shortmessage_recyclerview) RecyclerView recyclerView;
    private GifMovieView gif;
    private View cordovaMaskView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortmessage);
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
                ShortMessageDao shortMessageDao = new ShortMessageDao();
                final List<MessageConversationBean> beanList = shortMessageDao.getMessageConversationLog(activity);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideMask();
                        recyclerView.setAdapter(new MessageLogAdapter(beanList));
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
            ViewStub viewStub = (ViewStub) findViewById(R.id.activity_shortmessage_header_mask);
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
    class MessageLogAdapter extends RecyclerView.Adapter<MessageLogAdapter.MessageLogViewHolder> {

        List<MessageConversationBean> phoneCallBeanList;

        /**
         *
         * @param list
         */
        public MessageLogAdapter (List<MessageConversationBean> list){
            this.phoneCallBeanList = list;
        }

        /**
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public MessageLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MessageLogViewHolder holder = new MessageLogViewHolder(LayoutInflater.from(ShortMessageActivity.this).
                    inflate(R.layout.activity_calllog_list_item, parent, false));
            return holder;
        }

        /**
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(MessageLogViewHolder holder, int position) {
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

        /**
         *
         */
        class MessageLogViewHolder extends  RecyclerView.ViewHolder {

            @Bind(R.id.activity_calllog_list_item_name) TextView name;
            @Bind(R.id.activity_calllog_list_item_phone) TextView phone;
            @Bind(R.id.activity_calllog_list_item_duration) TextView duration;
            @Bind(R.id.activity_calllog_list_item_time) TextView time;

            MessageConversationBean messageBean;

            public MessageLogViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    /**
                     * Called when a view has been clicked.
                     *
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShortMessageActivity.this,ShortMessageDetailActivity.class);
                        intent.putExtra(ShortMessageDetailActivity.DATA,messageBean.getId());
                        startActivity(intent);
                    }
                });
            }

            /**
             * 设置数据
             * @param messageBean
             */
            public void setData(MessageConversationBean messageBean){
                name.setText(Html.fromHtml(String.format("%s <font color=\"#83CB84\">(%d)</font>",messageBean.getPhoneNumber(),messageBean.getMessageCount())));
                phone.setText(messageBean.getSnippet());
                time.setText(messageBean.getDate());
                this.messageBean = messageBean;
            }

        }
    }
}
