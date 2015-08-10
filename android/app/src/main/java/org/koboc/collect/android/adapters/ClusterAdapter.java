package org.koboc.collect.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.koboc.collect.android.R;
import org.koboc.collect.android.model.ClustersVM;

import java.util.List;

/**
 * Created by User on 08-07-2015.
 */
public class ClusterAdapter extends BaseAdapter {
    private List<ClustersVM> clustersVMList;
    private LayoutInflater inflater;
    private Context mContext;
    private TextView clusterText;

    public ClusterAdapter(Context mContext, List<ClustersVM> clustersVMList) {
        this.mContext = mContext;
        this.clustersVMList = clustersVMList;
    }

    @Override
    public int getCount() {
        return clustersVMList.size();
    }

    @Override
    public ClustersVM getItem(int i) {
        return clustersVMList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater == null)
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = inflater.inflate(R.layout.cluster_spinner_item, null);

        clusterText= (TextView) view.findViewById(R.id.clusterText);

        ClustersVM item=clustersVMList.get(i);


        clusterText.setText(item.getName());

        return view;
    }
}
