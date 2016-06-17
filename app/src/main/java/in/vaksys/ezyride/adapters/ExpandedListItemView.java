package in.vaksys.ezyride.adapters;

/**
 * Created by dell980 on 4/22/2016.
 */

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.vaksys.ezyride.R;


/**
 * Created by Harsh on 22-04-2016.
 */
public class ExpandedListItemView extends RelativeLayout {

    private LinearLayout mRoot;

    private TextView mText;

    private AppCompatRadioButton mCheckbox;

    private RadioGroup mygroup;

    private View mViewSeparator;

    public ExpandedListItemView(Context context) {
        super(context);
        init();
    }

    public ExpandedListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpandedListItemView(Context context, AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.expandable_list_item_view, this);

        mRoot = (LinearLayout) findViewById(R.id.expandable_list_item_view_root);
        mText = (TextView) findViewById(R.id.expandable_list_item_view_text);
        mCheckbox = (AppCompatRadioButton) findViewById(R.id.expandable_list_item_view_checkbox);
        //mViewSeparator = findViewById(R.id.expandable_list_item_view_separator);
        mygroup = (RadioGroup) findViewById(R.id.mgroup);

        this.mRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckbox.setChecked(!mCheckbox.isChecked());
            }
        });
//
//        this.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getContext(), "" + isChecked, Toast.LENGTH_SHORT).show();
//            }
//        });
         /* Attach CheckedChangeListener to radio group */
        mygroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                AppCompatRadioButton rb = (AppCompatRadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    Toast.makeText(getContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setText(String text, boolean showCheckbox) {
        this.mText.setText(text);
        if (!showCheckbox) {
            this.mCheckbox.setVisibility(View.GONE);
            this.mViewSeparator.setVisibility(View.GONE);
        }
    }


}