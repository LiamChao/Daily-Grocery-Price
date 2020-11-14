package tw.tcnr109a05.grocery;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link F10501#newInstance} factory method to
 * create an instance of this fragment.
 */


public class F10501 extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DB_FILE = "friends.db";
    //--------------------------
    private static ContentResolver mContRes;
    List<Map<String, Object>> mList;
    int tcount;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button b001;
    private Spinner s001;
    private Intent intent = new Intent();
    private ListView list001;
    private FriendDbHelper dbHper;
    private int DBver = 1;
    private ArrayList<String> recSet;
    private TextView t001;
    private TextView t002;
    private String fldSet;
    private String fldSet1;
    private int opne = 0;
    private View v;
    private String[] MYCOLUMN = new String[]{"id", "name", "grp", "address"};
    // ------------------
    private ListView.OnItemClickListener listviewOnItemClkLis = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (opne > 0) {
                t001.setVisibility(View.GONE);
                t002.setVisibility(View.GONE);
            }
            opne++;

            t001 = (TextView) view.findViewById(R.id.f10500_t003);
            t002 = (TextView) view.findViewById(R.id.f10500_t004);

            t001.setVisibility(View.VISIBLE);
            t002.setVisibility(View.VISIBLE);
        }
    };

    public F10501() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static F10501 newInstance(String param1, String param2) {
        F10501 fragment = new F10501();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.f10500, container, false);
        // Inflate the layout for this fragment
        setupViewcomponent();


        return v;
    }

    private void setupViewcomponent() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int newscrollheight = displayMetrics.heightPixels * 95 / 100; // 設定ScrollView使用尺寸的4/5
        //
        b001 = (Button) v.findViewById(R.id.f10500_b001);
        s001 = (Spinner) v.findViewById(R.id.f10500_s001);
        b001.setVisibility(View.GONE);
        s001.setVisibility(View.GONE);
        list001 = (ListView) v.findViewById(R.id.listView1);
        list001.getLayoutParams().height = newscrollheight;
        list001.setLayoutParams(list001.getLayoutParams()); // 重定ScrollView大小


        b001.setOnClickListener(this);
        initDB();


        mList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < recSet.size(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            String[] fld = recSet.get(i).split("♨");
            int cc = 123;
            String[] S = fld[3].split("#");
            String[] S1 = fld[4].split("#");
            item.put("txtView", "食譜名稱:" + fld[1]);
            item.put("txtView1", "簡介:" + fld[2]);
            fldSet = "";
            for (int j = 0; j < S.length; j = j + 2) {
                fldSet += "\n食材:" + S[j] + "  份量:" + S[j + 1];
            }
            item.put("txtView2", fldSet);
            fldSet1 = "";
            for (int j = 0; j < S1.length; j++) {
                fldSet1 += "\n步驟" + (j + 1) + ":" + S1[j];
            }
            item.put("txtView3", fldSet1);
            mList.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(
                getContext(),
                mList,
                R.layout.list_f10500,
                new String[]{"txtView", "txtView1", "txtView2", "txtView3"},
                new int[]{R.id.f10500_t001, R.id.f10500_t002, R.id.f10500_t003, R.id.f10500_t004});

        list001.setAdapter(adapter);
        list001.setOnItemClickListener(listviewOnItemClkLis);

    }

    private void initDB() {
        if (dbHper == null) {
            dbHper = new FriendDbHelper(getContext(), DB_FILE, null, DBver);
            recSet = dbHper.getRecSet();
        }
    }

    @Override
    public void onClick(View v) {

    }
}