package br.ufabc.gravador.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;

import br.ufabc.gravador.R;

public class DottedSeekBar extends AppCompatSeekBar {

    /**
     * Int values which corresponds to dots
     */
    private int[] mDotsPositions = null;
    /**
     * Drawable for dot
     */
    private Bitmap mDotBitmap = null;

    public DottedSeekBar ( final Context context ) {
        super(context);
        init(null);
    }

    public DottedSeekBar ( final Context context, final AttributeSet attrs ) {
        super(context, attrs);
        init(attrs);
    }

    public DottedSeekBar ( final Context context, final AttributeSet attrs, final int defStyle ) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public static Bitmap drawableToBitmap ( Drawable drawable ) {
        Bitmap bitmap;

        if ( drawable == null ) {
            return null;
        }

        if ( drawable instanceof BitmapDrawable ) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if ( bitmapDrawable.getBitmap() != null ) {
                return bitmapDrawable.getBitmap();
            }
        }

        if ( drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0 ) {
            bitmap = Bitmap.createBitmap(1, 1,
                    Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Initializes Seek bar extended attributes from xml
     *
     * @param attributeSet {@link AttributeSet}
     */
    private void init ( final AttributeSet attributeSet ) {
        final TypedArray attrsArray = getContext().obtainStyledAttributes(attributeSet,
                R.styleable.DottedSeekBar, 0, 0);

        final int dotsArrayResource = attrsArray.getResourceId(
                R.styleable.DottedSeekBar_dots_positions, 0);

        if ( 0 != dotsArrayResource ) {
            mDotsPositions = getResources().getIntArray(dotsArrayResource);
        }

        final int dotDrawableId = attrsArray.getResourceId(R.styleable.DottedSeekBar_dots_drawable,
                0);

        if ( 0 != dotDrawableId ) {
            setDotsDrawable(dotDrawableId);
        }

        attrsArray.recycle();
    }

    /**
     * @param dots to be displayed on this SeekBar
     */
    public void setDots ( final int[] dots ) {
        mDotsPositions = dots;
        invalidate();
    }

    @Override
    public synchronized void setMax ( int max ) {
        super.setMax(max);
        invalidate();
    }

    /**
     * @param dotsResource resource id to be used for dots drawing
     */
    public void setDotsDrawable ( final int dotsResource ) {
        Drawable dotDrawable = ContextCompat.getDrawable(getContext(), dotsResource);
        mDotBitmap = drawableToBitmap(dotDrawable);
        invalidate();
    }

    @Override
    protected synchronized void onDraw ( final Canvas canvas ) {
        super.onDraw(canvas);

        final float width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        final float step = width / (float) ( getMax() );

        if ( null != mDotsPositions && 0 != mDotsPositions.length && null != mDotBitmap ) {
            // draw dots if we have ones
            for ( int position : mDotsPositions ) {
                canvas.drawBitmap(mDotBitmap, getPaddingLeft() + position * step,
                        ( canvas.getHeight() - mDotBitmap.getHeight() ) / 2, null);
            }
        }
    }
}