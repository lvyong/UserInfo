package lakala.com.userinfo.dao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lakala.com.userinfo.bean.PhoneCallBean;

/**
 * 通话记录数据库操作
 * Created by lvyong on 15/11/4.
 */
public class CallLogDao {

    //通话记录content uri
    private final Uri CALL_LOGIN_URI = CallLog.Calls.CONTENT_URI;

    /**
     * 得到用户的通话记录
     * @return
     */
    public synchronized List<PhoneCallBean>  getCallLog (Context context){
        List<PhoneCallBean> phoneCallList = new ArrayList<>();

        try{
            Cursor cursor = context.getContentResolver().query(CALL_LOGIN_URI,null,null,null,"date desc");
            if (null != cursor) {
                cursor.moveToFirst();
                do {
                    PhoneCallBean phoneCallBean = new PhoneCallBean();
                    //手机号码
                    phoneCallBean.setPhoneNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                    //通话时长
                    phoneCallBean.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION)));
                    //联系人
                    phoneCallBean.setName(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME)));
                    //通话时间
                    Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                    phoneCallBean.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

                    phoneCallList.add(phoneCallBean);
                }while (cursor.moveToNext());

                cursor.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            return phoneCallList;
        }
    }
}
