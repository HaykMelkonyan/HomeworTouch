package com.example.user.homewortouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CanvasView extends View {

	private int pointerid;
	private Paint paint;

	private Random random;

	private List<Pointer> pointerList;

	public CanvasView(Context context) {
		this(context, null);
	}

	public CanvasView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		pointerList = new ArrayList<>();
		paint = new Paint();
		paint.setColor(Color.RED);
		random = new Random();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Pointer foundPointer=null;
		for (Pointer pointer : pointerList) {
			if (pointer.isEnabled() == false){
				foundPointer=pointer;}
			else
			{
				paint.setColor(pointer.color);
				canvas.drawCircle(pointer.point.x, pointer.point.y, 100, paint);}
		}
		if (foundPointer != null) {
			pointerList.remove(foundPointer);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN: {
				int actionIndex = event.getActionIndex();
				Pointer current= ContainsPoint(event.getX(),event.getY());
				if (current!=null) {
				} else {
					int randomColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
					float x = event.getX(actionIndex);
					float y = event.getY(actionIndex);
					Pointer pointer = new Pointer(pointerid++, x, y, randomColor);
					pointer.onDown=true;
					pointerList.add(pointer);
				}
				break;
			}
			case MotionEvent.ACTION_MOVE:
				Pointer current= ContainsPoint(event.getX(),event.getY());
				if (current!=null) {

					current.point.set(event.getX(), event.getY());
					current.onDown=true;
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				break;
			case MotionEvent.ACTION_UP:
				Pointer current1= ContainsPoint(event.getX(),event.getY());
				if (current1!=null ) {
					if (current1.onDown==true)
						current1.onDown=false;
					else
					    current1.Enabled=false;
				}
				break;
		}
		invalidate();
		return true;
	}


	private Pointer ContainsPoint(float x, float y){

		for (Pointer pointer : pointerList) {
			if (pointer.point.x+100>x && pointer.point.x-100<x && pointer.point.y+100>y && pointer.point.y-100<y)
			{
				return  pointer;
			}
		}
		return  null;


	}

	public static class Pointer     {
		private PointF point;
		private int color;
		private int pointerID;
		private boolean Enabled;
		private  boolean onDown;

		public Pointer(int pointerID, float x, float y, int color) {

			point = new PointF(x, y);
			this.color = color;
			this.pointerID = pointerID;
			setOnDown(true);
			Enabled=true;
		}

		public boolean isEnabled() {
			return Enabled;
		}

		public void setEnabled(boolean enabled) {
			Enabled = enabled;
		}


		public boolean isOnDown() {
			return onDown;
		}

		public void setOnDown(boolean onDown) {
			this.onDown = onDown;
		}
	}

}
