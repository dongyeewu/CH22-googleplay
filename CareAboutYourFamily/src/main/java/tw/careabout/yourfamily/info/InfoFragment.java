package tw.careabout.yourfamily.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import tw.careabout.yourfamily.R;

public class InfoFragment extends Fragment {

    private DbHelper dpHper;
    private String DB_Name ="";
    private int DBversion = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.info_tablayout);
        ViewPager2 viewPager2 = view.findViewById(R.id.info_viewpage);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager(), getLifecycle());

        viewPagerAdapter.addFragment(new InfoPageFragment(1), getString(R.string.info_tab_text1));
        viewPagerAdapter.addFragment(new InfoPageFragment(2), getString(R.string.info_tab_text2));
        viewPagerAdapter.addFragment(new InfoPageFragment(3), getString(R.string.info_tab_text3));
        viewPager2.setAdapter(viewPagerAdapter);
//        viewPager2.setUserInputEnabled(true);

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(viewPagerAdapter.titles.get(position));
                    }
                }).attach();
    }


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