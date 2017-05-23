package sm.cheongminapp.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm.cheongminapp.MapsActivity;
import sm.cheongminapp.R;
import sm.cheongminapp.RequestListActivity;
import sm.cheongminapp.data.Center;
import sm.cheongminapp.network.ApiServiceHelper;
import sm.cheongminapp.network.IApiService;
import sm.cheongminapp.view.adapter.AbstractAdapter;
import sm.cheongminapp.view.adapter.CenterAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CenterFragment extends Fragment implements AdapterView.OnItemClickListener{
    public static final int reqCode = 400;

    private EditText eCenterName;
    private Button bSearch, bRequestList;
    private ListView centerListView;

    private ArrayList<Center> nearCenterList;
    private ArrayList<Center> searchCenterList;
    private ArrayList<Center> entireCenterList;
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

        entireCenterList = new ArrayList<Center>();
        IApiService apiService = ApiServiceHelper.getInstance().ApiService;

        apiService.GetCenters().enqueue(new Callback<List<sm.cheongminapp.model.Center>>() {
            @Override
            public void onResponse(Call<List<sm.cheongminapp.model.Center>> call, Response<List<sm.cheongminapp.model.Center>> response) {
                Log.i("사이즈", Integer.toString(response.body().size()));
                for(int i=0; i<response.body().size(); i++) {
                    sm.cheongminapp.model.Center center = response.body().get(i);
                    entireCenterList.add(new Center(center.Name, center.Location.lat,
                            center.Location.lng, center.Infomation, center.Tel));

                    Log.i("센터들", center.Name);
                }
            }

            @Override
            public void onFailure(Call<List<sm.cheongminapp.model.Center>> call, Throwable t) {
                Log.d("센터 실패", "ㄹㅇ");
            }
        });

        adapter = new CenterAdapter(getActivity());
        adapter.addOrderList(entireCenterList);
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
