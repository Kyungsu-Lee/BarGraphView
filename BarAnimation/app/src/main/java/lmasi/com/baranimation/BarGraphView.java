package lmasi.com.baranimation;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lmasi.com.baranimation.R;



public class BarGraphView extends LinearLayout {

    private         TypedArray              attr;
    private         RelativeLayout          border_bg;
    private         RelativeLayout          content_bg;
    private         RelativeLayout          main_bg;

    private         TextView                left_text;
    private         TextView                center_text;
    private         TextView                right_text;

    private         LinearLayout            linearLayout;

    private final   int                     MINWIDTH                = 600;
    private final   int                     MINHEIGHT               = 105;
    private         int                     DEFAULT_TEXT_SIZE       = 10;
    private         int                     DEFAULT_BORDER_SIZE     = 10;
    private         int                     DEFAULT_COUNT           = 5;
    private         int                     DEFAULT_TIME            = 1000;
    private         int                     DEFAULT_STEP            = 200;
    private         int                     BORDER_COLOR            ;
    private         int                     BG_COLOR                ;
    private         int                     BAR_COLOR               ;

    private         boolean                 ONCE_FLAG               = true;
    private         boolean                 ON_ANIMATION            = false;

    private         int                     level;

    private         int                     parent_width;
    private         int                     parent_height;


    public BarGraphView(Context context) {
        super(context);
        initializeViews(context, null);
    }


    public BarGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public BarGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BarGraphView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context, attrs);
    }


    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bar_graph_view, this);

        if (attrs != null) {
            //attrs.xml에 정의한 스타일을 가져온다
            this.attr = context.obtainStyledAttributes(attrs, R.styleable.BarGraphView);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        main_bg = findViewById(R.id.bar_graph_main_bg);
        content_bg = findViewById(R.id.bar_graph_content_bg);
        border_bg = findViewById(R.id.bar_graph_border_bg);

         left_text = findViewById(R.id.bar_graph_left_text);
         center_text= findViewById(R.id.bar_graph_mid_text);
         right_text= findViewById(R.id.bar_graph_right_text);

        defaultSetting();
    }

    private void defaultSetting()
    {
        /*
        <attr name="text_size" format="integer"/>
        <attr name="border" format="integer"/>
        <attr name="count" format="integer"/>
        <attr name="step" format="integer"/>
        <attr name="time" format="integer"/>
        <attr name="border_color" format="color"/>
        <attr name="background_color" format="color"/>
        <attr name="bar_color" format="color"/>

        <attr name="level" format="integer"/>
        */

        DEFAULT_BORDER_SIZE         = attr.getInteger(R.styleable.BarGraphView_border, DEFAULT_BORDER_SIZE);
        DEFAULT_COUNT               = attr.getInteger(R.styleable.BarGraphView_count, DEFAULT_COUNT);
        DEFAULT_STEP                = Math.min(DEFAULT_TIME, attr.getInteger(R.styleable.BarGraphView_step, DEFAULT_STEP));
        DEFAULT_TIME                = attr.getInteger(R.styleable.BarGraphView_time, DEFAULT_TIME);
        BORDER_COLOR                = attr.getColor(R.styleable.BarGraphView_border_color, Color.BLACK);
        BG_COLOR                    = attr.getColor(R.styleable.BarGraphView_background_color, Color.WHITE);
        BAR_COLOR                   = attr.getColor(R.styleable.BarGraphView_bar_color, Color.RED);

        CharSequence left           = attr.getText(R.styleable.BarGraphView_left_text);
        CharSequence center         = attr.getText(R.styleable.BarGraphView_center_text);
        CharSequence right          = attr.getText(R.styleable.BarGraphView_right_text);

        left_text.setText(left != null ? left.toString(): "left");
        center_text.setText(center != null ? center.toString() : "center");
        right_text.setText(right != null ? right.toString() : "right");
        setTextSize(attr.getInteger(R.styleable.BarGraphView_text_size, DEFAULT_TEXT_SIZE));


        level = attr.getInteger(R.styleable.BarGraphView_level, 0);

        attr.recycle();
    }

    public void setTextSize(int textSize) {
        this.DEFAULT_TEXT_SIZE = textSize;
        left_text.setTextSize(textSize);
        right_text.setTextSize(textSize);
        center_text.setTextSize(textSize);
    }

    public void setBorder(int border) {
        this.DEFAULT_BORDER_SIZE = border;
    }

    public void setCount(int count) {
        this.DEFAULT_COUNT = count;
    }

    public void setAnimationTime(int time) {
        this.DEFAULT_TIME = time;
    }

    public void setStep(int step) {
        this.DEFAULT_STEP = step;
    }

    public void setBorderColor(int borderColor) {
        this.BORDER_COLOR = borderColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.BG_COLOR = backgroundColor;
    }

    public void setBarColor(int barColor) {
        this.BAR_COLOR = barColor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST://wrap_content
                parent_width = MINWIDTH;
                break;
            case MeasureSpec.UNSPECIFIED://unspecified
                parent_width = widthMeasureSpec;
                break;
            case MeasureSpec.EXACTLY://match_parent
                parent_width = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                parent_height = MINHEIGHT;
                break;
            case MeasureSpec.UNSPECIFIED:
                parent_height = heightMeasureSpec;
                break;
            case MeasureSpec.EXACTLY:
                parent_height = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }


        setMeasuredDimension(parent_width, parent_height);

        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY && ONCE_FLAG) {
            ONCE_FLAG = false;

            main_bg.getLayoutParams().height = parent_height;
            main_bg.getLayoutParams().width = parent_width;

            RelativeLayout.LayoutParams content_bg_param = (RelativeLayout.LayoutParams) content_bg.getLayoutParams();
            content_bg_param.setMargins(DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE);
            content_bg.setLayoutParams(content_bg_param);

            center_text.setTextSize(DEFAULT_TEXT_SIZE);
            right_text.setTextSize(DEFAULT_TEXT_SIZE);
            left_text.setTextSize(DEFAULT_TEXT_SIZE);


            RelativeLayout[] lines = new RelativeLayout[DEFAULT_COUNT];
            for (int i = 0; i < lines.length; i++) {
                lines[i] = new RelativeLayout(getContext());
               // int line_width = Math.min(parent_width < 100 ? 1 : parent_width / 100, DEFAULT_BORDER_SIZE/10);
                int line_width = DEFAULT_BORDER_SIZE;
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(line_width, parent_height);
                layoutParams.setMargins(parent_width / lines.length * (i + 1), 0, 0, 0);
                lines[i].setLayoutParams(layoutParams);
                lines[i].setBackgroundColor(BORDER_COLOR);
                border_bg.addView(lines[i]);
            }

            linearLayout = new LinearLayout(getContext());
            setLevel(level);
            content_bg.addView(linearLayout);

            border_bg.setBackgroundColor(BORDER_COLOR);
            content_bg.setBackgroundColor(BG_COLOR);
            linearLayout.setBackgroundColor(BAR_COLOR);
        }
    }

    public void setLevelAnimation(int level)
    {
        if(level == 0)
        {
            setLevel(0);
            return;
        }
        if(!ON_ANIMATION) {
            setLevel(0);
            final int final_level = Math.min(level, DEFAULT_COUNT);

            final Handler hd = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    setLevel(final_level * algorithm(msg.what, DEFAULT_STEP));
                }

            };

            AsyncTask.execute(new Runnable() {

                int current_index = 0;

                @Override
                public void run() {

                    double time_step = (double)DEFAULT_TIME / (double)DEFAULT_STEP;

                    while(current_index <= DEFAULT_STEP) {
                        ON_ANIMATION = true;
                        hd.sendEmptyMessage(current_index++);
                        try {
                            Thread.sleep((long) time_step);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    ON_ANIMATION = false;

                }
            });


        }
    }

    private double algorithm(int current_index, int max_count)
    {
        return Math.sqrt((double)(current_index)/ (double)max_count);
    }

    public void setLevel(double level)
    {
        if(level > DEFAULT_COUNT) level = DEFAULT_COUNT;
        if(level < 0) level = 0;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(content_bg.getMeasuredWidth() * 1.0 * level/DEFAULT_COUNT - DEFAULT_BORDER_SIZE),  content_bg.getMeasuredHeight() *2 / 3);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        linearLayout.setLayoutParams(params);
    }

}