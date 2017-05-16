package sm.cheongminapp.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import sm.cheongminapp.MapsActivity;
import sm.cheongminapp.R;
import sm.cheongminapp.RequestListActivity;
import sm.cheongminapp.Adapter.AbstractAdapter;
import sm.cheongminapp.Adapter.CenterAdapter;
import sm.cheongminapp.data.Center;


/**
 * A simple {@link Fragment} subclass.
 */
public class CenterFragment extends Fragment implements AdapterView.OnItemClickListener{
    public static final int reqCode = 400;

    private EditText eCenterName;
    private Button bSearch, bRequestList;
    private ListView centerListView;

    private ArrayList<Center> nearCenterList = new ArrayList<Center>();
    private ArrayList<Center> searchCenterList = new ArrayList<Center>();
    private AbstractAdapter<Center> adapter;

    public CenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center, container, false);

        eCenterName = (EditText) view.findViewById(R.id.center_name);
        bSearch = (Button) view.findViewById(R.id.btn_search);
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eCenterName.getText().toString().equals("")) return;
                // 서버에 이름 전송해서 검색 결과는 센터 클래스로 받아오기
                // 받아온 다음에는 다이얼로그 띄워서 리스트로 뿌리기
            }
        });
        bRequestList = (Button) view.findViewById(R.id.btn_request_list);
        bRequestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestListActivity.class);
                startActivity(intent);
            }
        });
        centerListView = (ListView) view.findViewById(R.id.list_center);
        nearCenterList.add(new Center("서울 강남구 수화통역센터", 12.3, 31.2, "asdf", "feww"));
        nearCenterList.add(new Center("서울 관악구 수화통역센터", 15.3, 33.2, "asdf", "feww"));
        nearCenterList.get(0).center_id = 1;

        adapter = new CenterAdapter(getActivity());
        adapter.addOrderList(nearCenterList);
        centerListView.setAdapter(adapter);
        centerListView.setOnItemClickListener(this);

        return view;

    }


    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] centerList = new String[searchCenterList.size()];
        for(int i=0; i<searchCenterList.size(); i++) {
            centerList[i] = searchCenterList.get(i).name;
        }

        builder.setTitle("검색된 통역 센터 목록입니다")        // 제목 설정
                .setItems(centerList, new DialogInterface.OnClickListener() {    // 목록 클릭시 설정
                    public void onClick(DialogInterface dialog, int index) {
                        Intent intent = new Intent(getActivity(), MapsActivity.class);
                        intent.putExtra("centerId", searchCenterList.get(index).center_id);
                        getActivity().startActivityForResult(intent, reqCode);
                        dialog.dismiss();
                    }
                })
                .setView(LayoutInflater.from(getActivity()).inflate(R.layout.dialog_centerlist, null));
        builder.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.putExtra("centerId", nearCenterList.get(position).center_id);
        getActivity().startActivityForResult(intent, reqCode);
    }
}
