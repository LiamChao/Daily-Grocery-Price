package tw.tcnr109a05.grocery;

import android.os.Handler;
import android.widget.ProgressBar;

import java.util.Calendar;

public class F10100_UpdateDataWork extends Thread {

    F10100 activity;
    private Handler mHandler;
    private ProgressBar mProBar;
    private int max = 100;//把max設定為100
    private int lastSec;
    private int Flag = 0;

    public F10100_UpdateDataWork(F10100 activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        super.run();
        //這裡只有進度條
        Calendar begin = Calendar.getInstance();//開始的時間點指定為變數begin
        do {
            Calendar now = Calendar.getInstance();//現在的時間點指定為變數now
            //(現在的分鐘減開始的分鐘)*60加上(現在的秒數減開始的秒數)=這個迴圈經過時間有幾秒
            final int iDiffSec = 60 * (now.get(Calendar.MINUTE) - begin.get(Calendar.MINUTE))
                    + now.get(Calendar.SECOND) - begin.get(Calendar.SECOND);     //這個迴圈經過時間有幾秒
            if (lastSec != iDiffSec)        //因為每秒會設定bar很多次，會讓手機當掉，所以如果跟前一次一樣就不執行了
            //如果還在同一秒內，不執行動作，避免系統累死==>等於一秒跑一次
            {
                lastSec = iDiffSec;//如果時間變化了，設定最後的時間為新的時間
                if (iDiffSec * max / 10 > max) {//設定max最大值，設定100在上面，max/10為每秒跑的量，等於把max切10等分
                    //這裡的意思是，當時間乘以每次跑的份量max/10，超過最大值max，就等於完整跑完這個進度了
                    mHandler.post(new Runnable() {//工作清單
                        public void run() {
                            mProBar.setProgress(max);//完整跑完進度條，就把進度條設定為max滿
                            //btnRe.setEnabled(true);
                        }
                    });
                    //System.exit(0);       //整個class結束
                    break;    //這行其實用不到，只是以防萬一
                }
                //不符合上面if跑完進度的條件的話，運行下面的
                mHandler.post(new Runnable() {
                    public void run() {
//                        mProBar.setProgress(iDiffSec * max/10);     //max/10是每秒跑的量，可以改
                        mProBar.setProgress(90);     //反正目前不會跑，就先讓他90... TODO
                        //還沒跑完，所以每次加進度到這位置iDiffSec * max/10
                    }
                });
            }
        }
        while (Flag != 1);
    }

    void setProgressBar(ProgressBar bar01) {
        mProBar = bar01;
    }

    void setHandler(Handler h) {
        mHandler = h;
    }
}
