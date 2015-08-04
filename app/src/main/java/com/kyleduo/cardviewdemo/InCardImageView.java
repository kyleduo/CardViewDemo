package com.kyleduo.cardviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * cardview中用到的ImageView，上边有圆角效果
 * Created by kyle on 15/8/3.
 */
public class InCardImageView extends ImageView {
	private float mCornerRadius;
	private RectF mCornerBounds;

	private Path mMaskPath;
	private Paint mMaskPaint;

	public InCardImageView(Context context) {
		super(context);
		init();
	}

	public InCardImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InCardImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		setLayerType(LAYER_TYPE_SOFTWARE, null);

		mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		mMaskPath = new Path();
		mMaskPath.setFillType(Path.FillType.INVERSE_WINDING);
		mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

		mCornerRadius = getResources().getDimension(R.dimen.card_corners);
		mCornerBounds = new RectF(0, 0, mCornerRadius * 2F, mCornerRadius * 2F);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != oldw || h != oldh) {
			this.mMaskPath.reset();
			this.mMaskPath.moveTo(0, h);
			this.mMaskPath.lineTo(0, mCornerRadius);
			this.mMaskPath.arcTo(mCornerBounds, 180, 90, false);
			this.mMaskPath.rLineTo(w - mCornerRadius * 2, 0);
			mCornerBounds.set(w - mCornerRadius * 2F, 0, w, mCornerRadius * 2F);
			this.mMaskPath.arcTo(mCornerBounds, 270, 90, false);
			this.mMaskPath.rLineTo(0, h - mCornerRadius);
			this.mMaskPath.close();
			mMaskPath.setFillType(Path.FillType.INVERSE_WINDING);
		}
	}

	@Override
	protected void onDraw(@NonNull Canvas canvas) {
		// 保存当前layer的透明橡树到离屏缓冲区。并新创建一个透明度爲255的新layer
		int saveCount = canvas.saveLayerAlpha(0.0F, 0.0F, canvas.getWidth(), canvas.getHeight(),
				255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);

		super.onDraw(canvas);
		if (this.mMaskPath != null) {
			canvas.drawPath(this.mMaskPath, this.mMaskPaint);
		}

		canvas.restoreToCount(saveCount);
	}
}
