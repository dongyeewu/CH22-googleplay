package tw.careabout.yourfamily.home;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import tw.careabout.yourfamily.CircleImgView;
import tw.careabout.yourfamily.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;




public class Home_fragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private float density,w,h;
    private TableRow peopleData;
    private ImageView img;

    private HomeDbHelper home_dpHper, G_home_dpHper, U_home_dpHper;
    private String DB_Name = "";
    private int DBversion = 1;
    private String User_IMAGE;
    private TextView userTxt;
    private TabLayout home_Tabs;
    private GoogleSignInAccount account;
    private RequestOptions options;

    public Home_fragment() {
        // Required empty public constructor
    }

    public static Home_fragment newInstance(String param1, String param2) {
        Home_fragment fragment = new Home_fragment();
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
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        initDB();
        setupViewComponent(view);
        setupTabView(view);
        return view;
    }

    private void setupViewComponent(View view) {
        DisplayMetrics dm = new DisplayMetrics(); //找出使用者手機的寬高
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        w=dm.widthPixels;
        h=dm.heightPixels;

        peopleData = (TableRow)view.findViewById(R.id.home_tr);
        img = (ImageView) view.findViewById(R.id.google_icon);

        userTxt = (TextView)view.findViewById(R.id.status);

        TableRow.LayoutParams peopkeDataLP = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)h/7);
        peopleData.setLayoutParams(peopkeDataLP);


        options = new RequestOptions()
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(50)))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.NORMAL);


    }

    private void setupTabView(View view) {
        home_Tabs = view.findViewById(R.id.home_tabs);
        ViewPager2 homeViewPager = view.findViewById(R.id.home_views);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager(), getLifecycle());

        viewPagerAdapter.addFragment(new Home_bd_fragment(), getString(R.string.home_tab1));
        viewPagerAdapter.addFragment(new Home_rs_fragment(), getString(R.string.home_tab3));
        viewPagerAdapter.addFragment(new Home_elder_fragment(), getString(R.string.home_tab4));

        homeViewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(home_Tabs, homeViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(viewPagerAdapter.titles.get(position));
                    }
                }).attach();


    }

    public void updateUI() {
            ArrayList<String> userRowData = home_dpHper.getUser();
            int  a=0;
            if( Integer.valueOf(userRowData.get(0)) != 0){
                User_IMAGE = userRowData.get(5);
                userTxt.setText(
                        userRowData.get(4)+getString(R.string.title_text_login)+
                                "\n"+userRowData.get(3)
                );
                //Glide.with(getContext()).load(User_IMAGE).into(img) ;


                Glide.with(getContext())
                        .load(User_IMAGE)
                        .apply(options)
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .override(100, 100)
//                    .transition(withCrossFade())
                        .error(
                                Glide.with(getContext())
                                        .load("https://bklifetw.com/img/nopic1.jpg"))
                        .into(img);



            }
            if( Integer.valueOf(userRowData.get(0)) == 0){
                userTxt.setText(getString(R.string.signed_out));
                img.setImageResource(R.drawable.eskimo); //還原圖示
            }

    }

    private void initDB() {
        if(account!=null){
            DB_Name = "CAYF.db";
            if (U_home_dpHper == null) {
                U_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
                U_home_dpHper.createHomeTable();
            }

            home_dpHper = U_home_dpHper;
        }
        if(account==null){
            DB_Name = "guest.db";
            if (G_home_dpHper == null) {
                G_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
                G_home_dpHper.createHomeTable();
            }

            home_dpHper = G_home_dpHper;
        }

    }

    //----------------------------------------生命週期---------------------------------------------------------------------
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //----------------------------------------innerClass---------------------------------------------------------------------
    private class ViewPagerAdapter extends FragmentStateAdapter {


        private final ArrayList<Fragment> fragments;
        private final ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm, Lifecycle lifecycle) {
            super(fm, lifecycle);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }
    }
}