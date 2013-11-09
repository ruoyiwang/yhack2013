package com.jake.quiltview;
import java.util.ArrayList;
import java.util.Collections;

import com.jake.quiltview.QuiltViewPatch.Size;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;


public class QuiltView extends FrameLayout implements OnGlobalLayoutListener {

	public QuiltViewBase quilt;
	public ViewGroup scroll;
	public int padding = 5;
	public boolean isVertical = false;
	public ArrayList<View> views;
	private Adapter adapter;
	
	public QuiltView(Context context,boolean isVertical) {
		super(context);
		this.isVertical = isVertical;
		setup();
	}
	
	public QuiltView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
			    R.styleable.QuiltView);
			 
		String orientation = a.getString(R.styleable.QuiltView_scrollOrientation);
		if(orientation != null){
			if(orientation.equals("vertical")){
				isVertical = true;
			} else {
				isVertical = false;
			}
		}
		setup();
	}
	
	public void setup(){
		views = new ArrayList<View>();
		
		if(isVertical){
			scroll = new ScrollView(this.getContext());
		} else {
			scroll = new HorizontalScrollView(this.getContext());
		}
		quilt = new QuiltViewBase(getContext(), isVertical);
		scroll.addView(quilt);
		this.addView(scroll);
		
	}
	
	private DataSetObserver adapterObserver = new DataSetObserver(){
		public void onChanged(){
			super.onChanged();
			onDataChanged();
		}
		
		public void onInvalidated(){
			super.onInvalidated();
			onDataChanged();
		}
		
		public void onDataChanged(){
			setViewsFromAdapter(adapter);
		}
	};
	
	public void setAdapter(Adapter adapter){
		this.adapter = adapter;
		adapter.registerDataSetObserver(adapterObserver);
		setViewsFromAdapter(adapter);
	}

	private void setViewsFromAdapter(Adapter adapter) {
		this.removeAllViews();
		for(int i = 0; i < adapter.getCount(); i++){
			quilt.addPatch(adapter.getView(i, null, quilt));
		}
	}
	
	public void addPatchImages(ArrayList<ImageView> images){
		
		for(ImageView image: images){
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
			image.setLayoutParams(params);
			
			LinearLayout wrapper = new LinearLayout(this.getContext());
			wrapper.setPadding(padding, padding, padding, padding);
			wrapper.addView(image);
			quilt.addPatch(wrapper);
		}
	}
	
	public void addPatchViews(ArrayList<View> views_a){
		for(View view: views_a){
			quilt.addPatch(view);
		}
	}
	
	public void addPatchYViews(ArrayList<View> views_a, ArrayList<QuiltViewPatch.Size> sizes_a) {

		int tmp;
		while ((tmp = sizes_a.lastIndexOf(QuiltViewPatch.Size.Huge)) != -1) {
			QuiltViewPatch qvp = QuiltViewPatch.create(QuiltViewPatch.Size.Huge);
			View v = views_a.get(tmp);
			
			quilt.addPatch(v, qvp);
			views_a.remove(tmp);
			sizes_a.remove(tmp);
		}
		while ((tmp = sizes_a.lastIndexOf(QuiltViewPatch.Size.Big)) != -1) {
			QuiltViewPatch qvp = QuiltViewPatch.create(QuiltViewPatch.Size.Big);
			View v = views_a.get(tmp);
			QuiltViewPatch qvplast = qvp;
			View vlast = v;
			
			//quilt.addPatch(v, qvp);
			views_a.remove(tmp);
			sizes_a.remove(tmp);
			
			if ((tmp = sizes_a.lastIndexOf(QuiltViewPatch.Size.Tall)) != -1) {
				qvp = QuiltViewPatch.create(QuiltViewPatch.Size.Tall);
				v = views_a.get(tmp);
				
				views_a.remove(tmp);
				sizes_a.remove(tmp);

				double rand = Math.random() * 10;
				
				if (rand < 5) {
					quilt.addPatch(v, qvp);
					quilt.addPatch(vlast, qvplast);
				}
				else {
					quilt.addPatch(vlast, qvplast);	
					quilt.addPatch(v, qvp);
				}
				
				continue;
			}
			
			quilt.addPatch(v, qvp);
			
			for (int i = 0; i < 2; i++) {
				if ((tmp = sizes_a.lastIndexOf(QuiltViewPatch.Size.Small)) != -1) {
					qvp = QuiltViewPatch.create(QuiltViewPatch.Size.Small);
					v = views_a.get(tmp);
					
					quilt.addPatch(v, qvp);
					views_a.remove(tmp);
					sizes_a.remove(tmp);
				}
			}	
		}

		while ((tmp = sizes_a.lastIndexOf(QuiltViewPatch.Size.Tall)) != -1) {
			int numWides = 0;
			QuiltViewPatch qvp = QuiltViewPatch.create(QuiltViewPatch.Size.Tall);
			View v = views_a.get(tmp);
			
			quilt.addPatch(v, qvp);
			views_a.remove(tmp);
			sizes_a.remove(tmp);

			for (int i = 0; i < 2; i++) {
				if ((tmp = sizes_a.lastIndexOf(QuiltViewPatch.Size.Wide)) != -1) {
					qvp = QuiltViewPatch.create(QuiltViewPatch.Size.Wide);
					v = views_a.get(tmp);
					
					quilt.addPatch(v, qvp);
					views_a.remove(tmp);
					sizes_a.remove(tmp);
					
					numWides++;					
				}
			}
			
			for (int i = 0; i < (4 - numWides*2); i++) {
				if ((tmp = sizes_a.lastIndexOf(QuiltViewPatch.Size.Small)) != -1) {
					qvp = QuiltViewPatch.create(QuiltViewPatch.Size.Small);
					v = views_a.get(tmp);
					
					quilt.addPatch(v, qvp);
					views_a.remove(tmp);
					sizes_a.remove(tmp);
				}				
			}
		}
		
		while ((tmp = sizes_a.lastIndexOf(QuiltViewPatch.Size.Wide)) != -1) {
			int numWides = 0;
			QuiltViewPatch qvp = QuiltViewPatch.create(QuiltViewPatch.Size.Wide);
			View v = views_a.get(tmp);
			
			quilt.addPatch(v, qvp);
			views_a.remove(tmp);
			sizes_a.remove(tmp);
			
			for (int i = 0; i < 1; i++) {
				if ((tmp = sizes_a.lastIndexOf(QuiltViewPatch.Size.Small)) != -1) {
					qvp = QuiltViewPatch.create(QuiltViewPatch.Size.Small);
					v = views_a.get(tmp);
					
					quilt.addPatch(v, qvp);
					views_a.remove(tmp);
					sizes_a.remove(tmp);
				}				
			}
			
			quilt.swapLastTwo(2);
		}
		
		while ((tmp = sizes_a.lastIndexOf(QuiltViewPatch.Size.Small)) != -1) {
			QuiltViewPatch qvp = QuiltViewPatch.create(QuiltViewPatch.Size.Small);
			View v = views_a.get(tmp);
			
			quilt.addPatch(v, qvp);
			views_a.remove(tmp);
			sizes_a.remove(tmp);
		}
	}
	
	public void addPatchesOnLayout(){
		for(View view: views){
			quilt.addPatch(view);
		}
	}
	
	public void removeQuilt(View view){
		quilt.removeView(view);
	}
	
	public void setChildPadding(int padding){
		this.padding = padding;
	}
	
	public void refresh(){
		quilt.refresh();
	}
	
	public void setOrientation(boolean isVertical){
		this.isVertical = isVertical;
	}

	
	@Override
	public void onGlobalLayout() {
		//addPatchesOnLayout();
	}
}
