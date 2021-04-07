package ua.cn.stu.getvariant.lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentSettings extends Fragment {
    private Spinner timeRange = null;
    private Spinner lang = null;
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        timeRange = view.findViewById(R.id.selectTimeLimit);
        lang = view.findViewById(R.id.selectLang);
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.launch_with_variants).setOnClickListener(v->{
            FragmentWithVariants testWithVariants = FragmentWithVariants.newInstance(
                    timeRange.getSelectedItem().toString(),
                    lang.getSelectedItem().toString()
            );
            replaceFragment(testWithVariants);
        });
        view.findViewById(R.id.launch_without_variants).setOnClickListener(v->{
            FragmentWithoutVariants testWithoutVariants = FragmentWithoutVariants.newInstance(
                    timeRange.getSelectedItem().toString(),
                    lang.getSelectedItem().toString()
            );
            replaceFragment(testWithoutVariants);
        });

    }

    public void replaceFragment(Fragment fragment){
        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame_container, fragment)
                .commit();
    }

}
