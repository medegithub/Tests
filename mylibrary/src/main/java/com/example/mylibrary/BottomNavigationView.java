package com.example.mylibrary;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class BottomNavigationView extends RelativeLayout {

    private OnBottomNavigationItemClickListener onBottomNavigationItemClickListener;

    private Context context;

    private final int NAVIGATION_HEIGHT = (int) getResources().getDimension(R.dimen.bottom_navigation_height);

    private final int NAVIGATION_LINE_WIDTH = (int) getResources().getDimension(R.dimen.bottom_navigation_line_width);

    private float textActiveSize;

    private float textInactiveSize;

    private List<BottomNavigationItem> bottomNavigationItems = new ArrayList<>();

    private List<View> viewList = new ArrayList<>();

    private int itemActiveColorWithoutColoredBackground = -1;

    private static int currentItem = 0;

    private int navigationWidth;

    private int shadowHeight;

    private int itemInactiveColor;

    private int itemWidth;

    private int itemHeight;

    private boolean withText;

    private boolean coloredBackground;

    private boolean disableShadow;

    private boolean isTablet;

    private boolean viewPagerSlide;

    private boolean isCustomFont = false;

    private boolean willNotRecreate = true;

    private FrameLayout container;

    private View backgroundColorTemp;

    private ViewPager mViewPager;

    private Typeface font;


    public BottomNavigationView(Context context) {
        this(context, null);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            Resources res = getResources();
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationView);
            withText = array.getBoolean(R.styleable.BottomNavigationView_bnv_with_text, true);
            coloredBackground = array.getBoolean(R.styleable.BottomNavigationView_bnv_colored_background, true);
            disableShadow = array.getBoolean(R.styleable.BottomNavigationView_bnv_shadow, false);
            isTablet = array.getBoolean(R.styleable.BottomNavigationView_bnv_tablet, false);
            viewPagerSlide = array.getBoolean(R.styleable.BottomNavigationView_bnv_viewpager_slide, true);
            itemActiveColorWithoutColoredBackground = array.getColor(R.styleable.BottomNavigationView_bnv_active_color, -1);
            textActiveSize = array.getDimensionPixelSize(R.styleable.BottomNavigationView_bnv_active_text_size, res.getDimensionPixelSize(R.dimen.bottom_navigation_text_size_active));
            textInactiveSize = array.getDimensionPixelSize(R.styleable.BottomNavigationView_bnv_inactive_text_size, res.getDimensionPixelSize(R.dimen.bottom_navigation_text_size_inactive));
            array.recycle();
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (bottomNavigationItems.size() == 0) {
            throw new NullPointerException("You need at least one item");
        }
        LayoutParams containerParams, params, lineParams;

        viewList.clear();
        if (isTablet) {
            itemWidth = LayoutParams.MATCH_PARENT;
            itemHeight = navigationWidth;
        } else {
            itemWidth = getWidth() / bottomNavigationItems.size();
            itemHeight = LayoutParams.MATCH_PARENT;
        }
        container = new FrameLayout(context);
        View shadow = new View(context);
        View line = new View(context);
        LinearLayout items = new LinearLayout(context);
        items.setOrientation(isTablet ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        LayoutParams shadowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, shadowHeight);
        if (isTablet) {
            containerParams = new LayoutParams(navigationWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            lineParams = new LayoutParams(NAVIGATION_LINE_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT);
            lineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params = new LayoutParams(navigationWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            items.setPadding(0, itemHeight / 2, 0, 0);
            addView(line, lineParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                LayoutParams backgroundLayoutParams = new LayoutParams(
                        navigationWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                backgroundLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
        } else {
            params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NAVIGATION_HEIGHT);
            containerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NAVIGATION_HEIGHT);
            shadowParams.addRule(RelativeLayout.ABOVE, container.getId());
            shadow.setBackgroundResource(R.drawable.shadow);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                LayoutParams backgroundLayoutParams = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, NAVIGATION_HEIGHT);
                backgroundLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
        }
        containerParams.addRule(isTablet ? RelativeLayout.ALIGN_PARENT_LEFT : RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(shadow, shadowParams);
        addView(container, containerParams);
        container.addView(items, params);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < bottomNavigationItems.size(); i++) {
            final int index = i;

            int textActivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_active);
            int viewInactivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_inactive);
            int viewInactivePaddingTopWithoutText = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_inactive_without_text);
            final View view = inflater.inflate(R.layout.bottom_navigation, this, false);
            ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
            TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);

            if (isCustomFont)
                title.setTypeface(font);

            if (isTablet)
                title.setVisibility(GONE);

            title.setTextColor(itemInactiveColor);
            viewList.add(view);

            if (bottomNavigationItems.get(i).getImageResourceActive() != 0) {
                if (i == currentItem)
                    icon.setImageResource(bottomNavigationItems.get(i).getImageResourceActive());
                else
                    bottomNavigationItems.get(i).getImageResource();
            } else {
                icon.setImageResource(bottomNavigationItems.get(i).getImageResource());
            }

            if (i == currentItem) {

                container.setBackgroundColor(bottomNavigationItems.get(index).getColor());

                title.setTextColor(itemActiveColorWithoutColoredBackground);
                icon.setScaleX((float) 1.1);
                icon.setScaleY((float) 1.1);
            }

            if (isTablet)
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), i == currentItem ? textActivePaddingTop : withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText,
                        view.getPaddingBottom());
            else
                view.setPadding(view.getPaddingLeft(), i == currentItem ? textActivePaddingTop : withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText, view.getPaddingRight(),
                        view.getPaddingBottom());

            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, i == currentItem ?
                    textActiveSize :
                    withText ? textInactiveSize : 0);
            title.setText(bottomNavigationItems.get(i).getTitle());
            LayoutParams itemParams = new LayoutParams(itemWidth, itemHeight);
            items.addView(view, itemParams);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBottomNavigationItemClick(index);
                }
            });

        }
    }

    private void onBottomNavigationItemClick(final int itemIndex) {

        if (currentItem == itemIndex) {
            return;
        }

        int viewActivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_active);
        int viewInactivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_inactive);
        int viewInactivePaddingTopWithoutText = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_inactive_without_text);
        for (int i = 0; i < viewList.size(); i++) {
            if (i == itemIndex) {
                View view = viewList.get(itemIndex).findViewById(R.id.bottom_navigation_container);
                final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
                BottomNavigationUtils.changeTextColor(title, itemInactiveColor, itemActiveColorWithoutColoredBackground);
                BottomNavigationUtils.changeTextSize(title, withText ? textInactiveSize : 0, textActiveSize);

                if (isTablet)
                    BottomNavigationUtils.changeRightPadding(view, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText, viewActivePaddingTop);
                else {
                    BottomNavigationUtils.changeViewTopPadding(view, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText, viewActivePaddingTop);
                }
            }
            else if (i == currentItem) {
                View view = viewList.get(i).findViewById(R.id.bottom_navigation_container);
                final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
                BottomNavigationUtils.changeTextColor(title, itemActiveColorWithoutColoredBackground, itemInactiveColor);
                BottomNavigationUtils.changeTextSize(title, textActiveSize, withText ? textInactiveSize : 0);

                if (isTablet)
                    BottomNavigationUtils.changeRightPadding(view, viewActivePaddingTop, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText);
                else
                    BottomNavigationUtils.changeViewTopPadding(view, viewActivePaddingTop, withText ? viewInactivePaddingTop : viewInactivePaddingTopWithoutText);

            }
        }

        if (mViewPager != null)
            mViewPager.setCurrentItem(itemIndex, viewPagerSlide);

        if (onBottomNavigationItemClickListener != null)
            onBottomNavigationItemClickListener.onNavigationItemClick(itemIndex);
        currentItem = itemIndex;
    }



    /**
     * Add item for BottomNavigation
     *
     * @param item item to add
     */
    public void addTab(BottomNavigationItem item) {
        bottomNavigationItems.add(item);
    }


    /**
     * Change text visibility
     *
     * @param withText disable or enable item text
     */
    public void isWithText(boolean withText) {
        this.withText = withText;
    }


    /**
     * Setup interface for item onClick
     */
    public void setOnBottomNavigationItemClickListener(OnBottomNavigationItemClickListener onBottomNavigationItemClickListener) {
        this.onBottomNavigationItemClickListener = onBottomNavigationItemClickListener;
    }

}
