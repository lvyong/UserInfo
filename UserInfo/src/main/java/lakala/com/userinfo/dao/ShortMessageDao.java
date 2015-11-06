package lakala.com.userinfo.dao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lakala.com.userinfo.bean.MessageBean;
import lakala.com.userinfo.bean.MessageConversationBean;

/**
 * 短信记录数据库操作
 * Created by lvyong on 15/11/4.
 */
public class ShortMessageDao {

    //会话Uri
    private  final Uri THREADS_URI = Uri.parse("content://mms-sms/conversations?simple=true");

    //查询用户信息
    private  final Uri USER_INFO_URI = Uri.parse("content://mms-sms/canonical-addresses");

    //短信Uri
    private final  Uri smsUri = Uri.parse("content://sms/");


    /**
     * 得到手机上短信彩信会话列表
     * @param context
     * @return
     */
    public synchronized List<MessageConversationBean> getMessageConversationLog(Context context){
        List<MessageConversationBean> messageBeanList = new ArrayList<>();

        try{
            Cursor cursor = context.getContentResolver().query(THREADS_URI,null,null,null,"date desc");
            if (null != cursor) {
                cursor.moveToFirst();
                do {
                    MessageConversationBean messageConversationBean = new MessageConversationBean();
                    //id
                    messageConversationBean.setId(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

                    String recipient_ids = cursor.getString(cursor.getColumnIndexOrThrow("recipient_ids"));
                    queryUserPhoneNumber(context, recipient_ids, messageConversationBean);

                    //snippet
                    messageConversationBean.setSnippet(cursor.getString(cursor.getColumnIndexOrThrow("snippet")));
                    //通话时间
                    Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("date"))));
                    messageConversationBean.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

                    messageConversationBean.setMessageCount(cursor.getInt(cursor.getColumnIndexOrThrow("message_count")));

                    messageBeanList.add(messageConversationBean);
                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            return messageBeanList;
        }
    }


    /**
     * 获取用户设备上的短信记录
     * @param context
     * @param thread_Id
     * @return
     */
    public synchronized   List<MessageBean> getMessageLog(Context context,String thread_Id){
        List<MessageBean> messageBeanList = new ArrayList<>();

        try{
            Cursor cursor = context.getContentResolver().query(smsUri,null,"thread_id=?",
                    new String[]{thread_Id},"date desc");
            if (null != cursor) {
                cursor.moveToFirst();
                do {
                    MessageBean messageBean = new MessageBean();
                    //联系人
                    messageBean.setName(cursor.getString(cursor.getColumnIndexOrThrow("person")));
                    //手机号码
                    messageBean.setPhoneNumber(cursor.getString(cursor.getColumnIndex("address")));
                    //通话时长
                    messageBean.setSmsbody(cursor.getString(cursor.getColumnIndexOrThrow("body")));
                    //通话时间
                    Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("date"))));
                    messageBean.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

                    messageBean.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));

                    messageBeanList.add(messageBean);
                }while (cursor.moveToNext());

                cursor.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            return messageBeanList;
        }
    }


    /**
     * 查询用户的手机号码
     * @param context
     * @param id
     * @param messageConversationBean
     */
    private  synchronized void queryUserPhoneNumber(Context context,String id,MessageConversationBean messageConversationBean){
        Cursor cursor = null;
        try{
             cursor = context.getContentResolver().query(USER_INFO_URI,null,"_id="+id,null,null);
            if (null != cursor) {
                cursor.moveToFirst();
                    //手机号码
                    messageConversationBean.setPhoneNumber(cursor.getString(cursor.getColumnIndex("address")));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null != cursor) {
                cursor.close();
            }
        }
    }
}
