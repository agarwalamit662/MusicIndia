package com.support.android.musicindia.fragments;

/**
 * Created by amitagarwal3 on 12/7/2015.
 */
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;

import com.support.android.musicindia.helper.BrowserSong;
import com.support.android.musicindia.helper.CheckableImageButton;
import com.support.android.musicindia.helper.DividerItemDecoration;
import com.support.android.musicindia.application.MusicPlayerApplication;
import com.support.android.musicindia.R;
import com.support.android.musicindia.interfaces.YourFragmentInterface;
import com.support.android.musicindia.activities.EditTag;
import com.support.android.musicindia.activities.MainActivity;
import com.support.android.musicindia.model.PlayableItem;
import com.support.android.musicindia.services.MusicServiceTinyPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class MusicPlayerFragments extends Fragment implements SearchView.OnQueryTextListener,YourFragmentInterface {

    private ArrayList<String> MyFiles;
    private RecyclerView rv;
    static MainActivity ma;
    private MusicPlayerApplication app;
    private int cancelId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_tracks_details, container, false);
        app = (MusicPlayerApplication)this.getActivity().getApplication();
        rv = (RecyclerView) view.findViewById(R.id.rViewTracks);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL));
        rv.setAdapter(new RecyclerViewAdapterMusicPlayerFragment(this.getActivity(), BrowserSong.getSongsInDirectory("date"),MusicPlayerFragments.this));
        setHasOptionsMenu(true);
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

        if(rv != null) {
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            rv.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL));
            rv.setAdapter(new RecyclerViewAdapterMusicPlayerFragment(this.getActivity(), BrowserSong.getSongsInDirectory("date"), MusicPlayerFragments.this));

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sample_actions, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String query) {
        ArrayList<BrowserSong> sample = BrowserSong.getSongsInDirectory("date");
        final List<BrowserSong> filteredModelList = filter(sample, query);

        RecyclerViewAdapterMusicPlayerFragment adap = new RecyclerViewAdapterMusicPlayerFragment(getActivity(),
                sample,this);

        adap.animateTo(filteredModelList);
        rv.setAdapter(adap);
        rv.scrollToPosition(0);
        return true;
    }

    private List<BrowserSong> filter(List<BrowserSong> models, String query) {
        query = query.toLowerCase();

        final List<BrowserSong> filteredModelList = new ArrayList<>();
        for (BrowserSong model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }



    @Override
    public void onStart() {
        super.onStart();
        // The service is bound to this activity

    }

    @Override
    public void update() {
        (new AsyncListViewLoaderTiled()).execute("");
    }

    private class AsyncListViewLoaderTiled extends AsyncTask<String, Void, List<BrowserSong>> {

        @Override
        protected void onPostExecute(List<BrowserSong> result) {
            super.onPostExecute(result);
            onResume();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<BrowserSong> doInBackground(String... params) {
            return null;
        }
    }



    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    
    public static class RecyclerViewAdapterMusicPlayerFragment
            extends RecyclerView.Adapter<RecyclerViewAdapterMusicPlayerFragment.ViewHolder> {

        private Context mContext;
        private MusicPlayerFragments mpf;
        private List<BrowserSong> mValues;
        private ViewHolder holds;
        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{


            public final View mView;
            private ImageButton menu;
            public final CircleImageView mImage;
            public final TextView mTextView;
            public final CircleButton mPlayIcon;
            public ViewHolder(View view) {
                super(view);
                mView = view;

                mTextView = (TextView) view.findViewById(R.id.trackname);
                mPlayIcon = (CircleButton) view.findViewById(R.id.playIcon);
                mImage = (CircleImageView) view.findViewById(R.id.popImage);
                menu = (ImageButton)view.findViewById(R.id.buttonMenu);
                menu.setFocusable(false);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            }
        }

        public PlayableItem getValueAt(int position) {
            return mValues.get(position);
        }

        public RecyclerViewAdapterMusicPlayerFragment(Context context, List<BrowserSong> items,MusicPlayerFragments mpf) {

            mValues = items;
            mContext = context;
            this.mpf = mpf;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_tracks_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holds = holder;
            holds.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(mContext, v);
                    popup.getMenuInflater().inflate(R.menu.contextmenu_tracks, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_share:

                                    Uri uri = Uri.parse(mValues.get(position).getUri());
                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("audio/*");
                                    share.putExtra(Intent.EXTRA_STREAM, uri);
                                    mContext.startActivity(Intent.createChooser(share, "Share Audio File"));

                                    return true;

                                case R.id.menu_delete:

                                    AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                                    adb.setTitle("Delete this song?");
                                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            ContentResolver contentResolver = mContext.getContentResolver();
                                            contentResolver.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                                    MediaStore.Images.ImageColumns.DATA + " =? " , new String[]{ mValues.get(position).getUri().toString() });


                                            File file = new File(mValues.get(position).getUri().toString());
                                            file.delete();

                                            dialog.dismiss();

                                            removeAt(position);


                                        }
                                    });


                                    adb.show();

                                    return true;

                                case R.id.menu_editTag:

                                    Intent n = new Intent(mContext, EditTag.class);
                                    n.putExtra("FileName", mValues.get(position).getUri());
                                    mContext.startActivity(n);

                                    return true;


                            }
                            return true;
                        }
                    });

                    popup.show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (popup.getDragToOpenListener() instanceof ListPopupWindow.ForwardingListener) {
                            ListPopupWindow.ForwardingListener listener = (ListPopupWindow.ForwardingListener) popup.getDragToOpenListener();
                            listener.getPopup().setVerticalOffset(-holds.menu.getHeight());
                            listener.getPopup().show();
                        }
                    }


                }
            });
            holds.mImage.setVisibility(View.GONE);

           // Glide.with(mContext).load(mValues.get(position).getImage()).into(holds.mImage);
            holds.mTextView.setText(mValues.get(position).getTitle().toString());

            holds.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity m = (MainActivity) mContext;
                    m.playItem(mValues.get(position));
                }
            });

            holds.mPlayIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity m = (MainActivity) mContext;
                    m.playItem(mValues.get(position));
                }
            });

        }

        @Override
        public int getItemCount() {
            if(mValues != null && mValues.size() > 0)
                return mValues.size();
            else
                return 0;
        }

        public void animateTo(List<BrowserSong> models) {
            applyAndAnimateRemovals(models);
            applyAndAnimateAdditions(models);
            applyAndAnimateMovedItems(models);
        }

        private void applyAndAnimateRemovals(List<BrowserSong> newModels) {
            for (int i = mValues.size() - 1; i >= 0; i--) {
                final BrowserSong model = mValues.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }

        private void applyAndAnimateAdditions(List<BrowserSong> newModels) {
            for (int i = 0, count = newModels.size(); i < count; i++) {
                final BrowserSong model = newModels.get(i);
                if (!mValues.contains(model)) {
                    addItem(i, model);
                }
            }
        }

        private void applyAndAnimateMovedItems(List<BrowserSong> newModels) {
            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
                final BrowserSong model = newModels.get(toPosition);
                final int fromPosition = mValues.indexOf(model);
                if (fromPosition >= 0 && fromPosition != toPosition) {
                    moveItem(fromPosition, toPosition);
                }
            }
        }

        public  BrowserSong removeItem(int position) {
            final BrowserSong model = mValues.remove(position);
            notifyItemRemoved(position);
            return model;
        }

        public void addItem(int position, BrowserSong model) {
            mValues.add(position, model);
            notifyItemInserted(position);
        }

        public void moveItem(int fromPosition, int toPosition) {
            final BrowserSong model = mValues.remove(fromPosition);
            mValues.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }

        public void removeAt(int position) {
            mValues.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mValues.size());
        }
    }

}

