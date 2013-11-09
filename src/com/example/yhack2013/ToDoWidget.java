package com.example.yhack2013;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ToDoWidget extends View{
	GestureDetector gestureDetector;
	boolean expanded, movemode;
	Paint paint;
	int w,h,dx,dy;
	String text, label, day_count;
	public ToDoWidget(Context context) {
		super(context);
		expanded = false;
		movemode = false;
		// TODO Auto-generated constructor stub
		paint = new Paint();
	    gestureDetector = new GestureDetector(context, new GestureListener());
	}

    private int determineMaxTextSize(String str, double maxWidth)
    {
        int size = 0;       
        Paint _paint = new Paint();

        do {
            _paint.setTextSize(++ size);
        } while(_paint.measureText(str) < maxWidth);

        return size;
    } 
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }
	@Override
	protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
		int[] i = new int[2];
		getLocationInWindow(i);
        int size = determineMaxTextSize(label, getMeasuredWidth()*0.8);
        paint.setStrokeWidth(3);
        if (size>150) size = 150;
        paint.setTextSize(size);
        paint.setColor(Color.WHITE);;
		canvas.drawText(label, 0, h, paint);
		paint.setTextSize(36);
		TextRect tr = new TextRect(paint);
		tr.prepare(text, w-40, h-70);
		tr.draw(canvas, 20, 20);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(0, 0, w, h, paint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (e.getAction()==MotionEvent.ACTION_MOVE&&movemode){
	        int x = (int) e.getRawX();
	        int y = (int) e.getRawY();
	        LayoutParams lp = (LayoutParams) getLayoutParams();
	        
	        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
	        windowParams.x = 0 + (x - dx);
	        windowParams.y = 0 + (y - dy);
	        windowParams.height = getHeight();
	        windowParams.width = getWidth();
	        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
	                            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
	                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
	                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
	        windowParams.format = PixelFormat.TRANSLUCENT;
	                    windowParams.windowAnimations = 0;

	        invalidate();
	        return true;
		}
		if (e.getAction()==MotionEvent.ACTION_UP&&movemode){
			movemode=false;
	        return true;
		}
	    return gestureDetector.onTouchEvent(e);
	}
	private class GestureListener extends GestureDetector.SimpleOnGestureListener {

	    @Override
	    public void onLongPress(MotionEvent e) {
            dx = (int) e.getRawX();
            dy = (int) e.getRawY();
	    	movemode=true;
	    }
		@Override
	    public boolean onDown(MotionEvent e) {
	        return true;
	    }
	    // event when double tap occurs
	    @Override
	    public boolean onDoubleTapEvent(MotionEvent e) {
	    	if (e.getAction() == MotionEvent.ACTION_DOWN){
				NoteDialogFragment diag = new NoteDialogFragment(text,label,day_count);
				Activity fa = (Activity) getContext(); 
				FragmentManager fm = fa.getFragmentManager();
				diag.show(fm, "");
	    	}
	        return true;
	    }
	}
	public void setContent(String label ,String text,int duedate){
		this.label = label;
		this.text = text;
		this.day_count = "";
	}
	
}

class NoteDialogFragment extends DialogFragment {
	String text, label, day_count;
	public NoteDialogFragment(String text, String label, String day){
		this.text = text;
		this.label = label;
		this.day_count = day;
	}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(text)
               .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

/**
 * Draw text in a given rectangle and automatically wrap lines.
 * This class is designed to be used in games and therefore tries
 * to minimize allocations after instantiation because those will
 * trigger the GC more often which causes a slight but noticeable
 * delay. So, if you want to use this in a game, you should
 * make the instance a class member, allocate it once and reuse it.
 *
 * @author mf@markusfisch.de
 */
class TextRect
{
	// maximum number of lines; this is a fixed number in order
	// to use a predefined array to avoid ArrayList (or something
	// similar) because filling it does involve allocating memory
	static private int MAX_LINES = 256;

	// those members are stored per instance to minimize
	// the number of allocations to avoid triggering the
	// GC too much
	private FontMetricsInt metrics = null;
	private Paint paint = null;
	private int starts[] = new int[MAX_LINES];
	private int stops[] = new int[MAX_LINES];
	private int lines = 0;
	private int textHeight = 0;
	private Rect bounds = new Rect();
	private String text = null;
	private boolean wasCut = false;

	/**
	 * Create reusable text rectangle (use one instance per font).
	 *
	 * @param paint - paint specifying the font
	 */
	public TextRect( final Paint paint )
	{
		metrics = paint.getFontMetricsInt();
		this.paint = paint;
	}

	/**
	 * Calculate height of text block and prepare to draw it.
	 *
	 * @param text - text to draw
	 * @param width - maximum width in pixels
	 * @param height - maximum height in pixels
	 * @returns height of text in pixels
	 */
	public int prepare(
		final String text,
		final int maxWidth,
		final int maxHeight )
	{
		lines = 0;
		textHeight = 0;
		this.text = text;
		wasCut = false;

		// get maximum number of characters in one line
		paint.getTextBounds(
			"i",
			0,
			1,
			bounds );

		final int maximumInLine = maxWidth / bounds.width();
		final int length = text.length();

		if( length > 0 )
		{
			final int lineHeight = -metrics.ascent + metrics.descent;
			int start = 0;
			int stop = maximumInLine > length ? length : maximumInLine;

			for( ;; )
			{
				// skip LF and spaces
				for( ; start < length; ++start )
				{
					char ch = text.charAt( start );

					if( ch != '\n' &&
						ch != '\r' &&
						ch != '\t' &&
						ch != ' ' )
						break;
				}

				for( int o = stop + 1; stop < o && stop > start; )
				{
					o = stop;

					int lowest = text.indexOf( "\n", start );

					paint.getTextBounds(
						text,
						start,
						stop,
						bounds );

					if( (lowest >= start && lowest < stop) ||
						bounds.width() > maxWidth )
					{
						--stop;

						if( lowest < start ||
							lowest > stop )
						{
							final int blank = text.lastIndexOf( " ", stop );
							final int hyphen = text.lastIndexOf( "-", stop );

							if( blank > start &&
								(hyphen < start || blank > hyphen) )
								lowest = blank;
							else if( hyphen > start )
								lowest = hyphen;
						}

						if( lowest >= start &&
							lowest <= stop )
						{
							final char ch = text.charAt( stop );

							if( ch != '\n' &&
								ch != ' ' )
								++lowest;

							stop = lowest;
						}

						continue;
					}

					break;
				}

				if( start >= stop )
					break;

				int minus = 0;

				// cut off lf or space
				if( stop < length )
				{
					final char ch = text.charAt( stop - 1 );

					if( ch == '\n' ||
						ch == ' ' )
						minus = 1;
				}

				if( textHeight + lineHeight > maxHeight )
				{
					wasCut = true;
					break;
				}

				starts[lines] = start;
				stops[lines] = stop - minus;

				if( ++lines > MAX_LINES )
				{
					wasCut = true;
					break;
				}

				if( textHeight > 0 )
					textHeight += metrics.leading;

				textHeight += lineHeight;

				if( stop >= length )
					break;

				start = stop;
				stop = length;
			}
		}

		return textHeight;
	}

	/**
	 * Draw prepared text at given position.
	 *
	 * @param canvas - canvas to draw text into
	 * @param left - left corner
	 * @param top - top corner
	 */
	public void draw(
		final Canvas canvas,
		final int left,
		final int top )
	{
		if( textHeight == 0 )
			return;

		final int before = -metrics.ascent;
		final int after = metrics.descent + metrics.leading;
		int y = top;

		--lines;
		for( int n = 0; n <= lines; ++n )
		{
			String t;

			y += before;

			if( wasCut &&
				n == lines &&
				stops[n] - starts[n] > 3 )
				t = text.substring( starts[n], stops[n] - 3 ).concat( "..." );
			else
				t = text.substring( starts[n], stops[n] );

			canvas.drawText(
				t,
				left,
				y,
				paint );

			y += after;
		}
	}

	/** Returns true if text was cut to fit into the maximum height */
	public final boolean wasCut()
	{
		return wasCut;
	}
}
