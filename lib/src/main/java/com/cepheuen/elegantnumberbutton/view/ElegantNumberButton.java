package com.cepheuen.elegantnumberbutton.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.R;

/**
 * Created by Ashik Vetrivelu on 10/08/16.
 */
public class ElegantNumberButton extends RelativeLayout {
    private Context context;
    private AttributeSet attrs;
    private int styleAttr;
    private OnClickListener mListener;
    private int initialNumber;
    private int lastNumber;
    private int currentNumber;
    private int finalNumber;
    private int stepNumber;
    private String postfixText;
    private TextView textView;
    private View view;
    private OnValueChangeListener mOnValueChangeListener;
    public ElegantNumberButton(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ElegantNumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }

    public ElegantNumberButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView()
    {
        this.view = this;
        inflate(context,R.layout.layout,this);
        final Resources res = getResources();
        final int defaultColor = res.getColor(R.color.colorPrimary);
        final int defaultTextColor = res.getColor(R.color.colorText);
        final Drawable defaultDrawable = res.getDrawable(R.drawable.background);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ElegantNumberButton,styleAttr,0);

        initialNumber = a.getInt(R.styleable.ElegantNumberButton_initialNumber,0);
        finalNumber = a.getInt(R.styleable.ElegantNumberButton_finalNumber,Integer.MAX_VALUE);
        stepNumber = a.getInt(R.styleable.ElegantNumberButton_stepNumber,1);
        postfixText = a.getString(R.styleable.ElegantNumberButton_postfixText);
        if(postfixText == null){
            postfixText = "";
        }

        float textSize = a.getDimension(R.styleable.ElegantNumberButton_textSize,13);
        int color = a.getColor(R.styleable.ElegantNumberButton_backGroundColor,defaultColor);
        int textColor = a.getColor(R.styleable.ElegantNumberButton_textColor,defaultTextColor);
        Drawable drawable = a.getDrawable(R.styleable.ElegantNumberButton_backgroundDrawable);

        Button button1 = (Button) findViewById(R.id.subtract_btn);
        Button button2 = (Button) findViewById(R.id.add_btn);
        textView = (TextView) findViewById(R.id.number_counter);
        LinearLayout mLayout = (LinearLayout) findViewById(R.id.layout);

        button1.setTextColor(textColor);
        button2.setTextColor(textColor);
        textView.setTextColor(textColor);
        button1.setTextSize(textSize);
        button2.setTextSize(textSize);
        textView.setTextSize(textSize);

        if(drawable == null)
        {
            drawable = defaultDrawable;
        }
        assert drawable != null;
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
        if(Build.VERSION.SDK_INT > 16)
            mLayout.setBackground(drawable);
        else
            mLayout.setBackgroundDrawable(drawable);

        textView.setText(initialNumber + postfixText);

        currentNumber = initialNumber;
        lastNumber = initialNumber;

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                int num = Integer.valueOf(textView.getText().toString().replace(postfixText,""));
                setNumber(String.valueOf(num-stepNumber), true);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                int num = Integer.valueOf(textView.getText().toString().replace(postfixText,""));
                setNumber(String.valueOf(num+stepNumber), true);
            }
        });
        a.recycle();
    }

    private void callListener(View view)
    {
        if(mListener!=null)
        {
            mListener.onClick(view);
        }

        if(mOnValueChangeListener != null)
        {
            if(lastNumber != currentNumber)
            {
                mOnValueChangeListener.onValueChange(this, lastNumber, currentNumber);
            }
        }
    }

    public String getNumber()
    {
        return String.valueOf(currentNumber);
    }
    public void setNumber(String number)
    {
        lastNumber = currentNumber;
        this.currentNumber = Integer.parseInt(number);
        if(this.currentNumber > finalNumber)
        {
            this.currentNumber = finalNumber;
        }
        if(this.currentNumber < initialNumber)
        {
            this.currentNumber = initialNumber;
        }
        textView.setText(currentNumber + postfixText);
    }
    public void setNumber(String number, boolean notifyListener){
        setNumber(number);
        if(notifyListener)
        {
            callListener(this);
        }
    }
    public void setOnClickListener(OnClickListener onClickListener)
    {
        this.mListener = onClickListener;
    }
    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener){
        mOnValueChangeListener = onValueChangeListener;
    }
    public interface OnClickListener {

        void onClick(View view);

    }
    public interface OnValueChangeListener {
        void onValueChange(ElegantNumberButton view, int oldValue, int newValue);
    }
    public void setRange(Integer startingNumber,Integer endingNumber)
    {
        this.initialNumber = startingNumber;
        this.finalNumber = endingNumber;
    }
}
