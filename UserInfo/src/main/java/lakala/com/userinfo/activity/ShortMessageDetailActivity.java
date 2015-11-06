package lakala.com.userinfo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import butterknife.Bind;
import butterknife.ButterKnife;
import lakala.com.userinfo.R;
import lakala.com.userinfo.bean.MessageBean;
import lakala.com.userinfo.dao.ShortMessageDao;
import lakala.com.userinfo.util.DimenUtil;

/**
 * Created by lvyong on 15/11/4.
 */
public class ShortMessageDetailActivity extends BaseActivity {

    public final static String DATA = "DATA";

    @Bind(R.id.activity_shortmessage_detail_recyclerview) RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        initView();
        initData();
     }

    /**
     * 初始化view
     */
    private void initView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        if (null != getIntent() && getIntent().hasExtra(DATA)) {
            final Activity activity = this;
            final String id = getIntent().getStringExtra(DATA);
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    ShortMessageDao shortMessageDao = new ShortMessageDao();
                    final List<MessageBean> beanList = shortMessageDao.getMessageLog(activity, id);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(new MessageDetailAdapter(beanList));
                        }
                    });
                }
            });
            scheduledExecutorService.shutdown();
        }
    }


    /**
     * 适配器
     */
    class MessageDetailAdapter extends RecyclerView.Adapter<MessageDetailAdapter.MessageDetailViewHolder> {

        List<MessageBean> phoneCallBeanList;

        /**
         *
         * @param list
         */
        public MessageDetailAdapter (List<MessageBean> list){
            this.phoneCallBeanList = list;
        }

        /**
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public MessageDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MessageDetailViewHolder holder = new MessageDetailViewHolder(LayoutInflater.from(ShortMessageDetailActivity.this).
                    inflate(R.layout.activity_message_detail_list_item, parent, false));
            return holder;
        }

        /**
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(MessageDetailViewHolder holder, int position) {
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
        class MessageDetailViewHolder extends  RecyclerView.ViewHolder {
            @Bind(R.id.activity_message_detail_list_item_root) LinearLayout rootLayout;
            @Bind(R.id.activity_message_detail_list_item_msgbody) TextView msgTextView;
            @Bind(R.id.activity_message_detail_list_item_time) TextView timeTextView;

            public MessageDetailViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            /**
             * 设置数据
             * @param messageBean
             */
            public void setData(MessageBean messageBean) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(
                        DimenUtil.dp2px(ShortMessageDetailActivity.this, 10f),
                        DimenUtil.dp2px(ShortMessageDetailActivity.this,16f),
                        DimenUtil.dp2px(ShortMessageDetailActivity.this,10f),
                        DimenUtil.dp2px(ShortMessageDetailActivity.this,16f));

                if (messageBean.getType().equals("1")) {
                    rootLayout.setBackgroundResource(R.drawable.text_bg_left);
                    params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                    rootLayout.setLayoutParams(params);
                } else {
                    rootLayout.setBackgroundResource(R.drawable.text_bg_right);
                    params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                    rootLayout.setLayoutParams(params);
                }

                msgTextView.setText(messageBean.getSmsbody());
                timeTextView.setText(messageBean.getDate());
            }
        }
    }
}
