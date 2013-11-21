package com.aehooo.tdsqn.entity;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.aehooo.tdsqn.resources.ImageAlligator3000;

public class ThisGameAnimatedSprite extends AnimatedSprite {

	private ITouchHandler touchHandler;

	public ThisGameAnimatedSprite(final float pX, final float pY,
			final ITiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion, ImageAlligator3000
				.getVertexBufferObjectManager());
	}

	public void removeTouchHandler() {
		this.touchHandler = null;
	}

	public void setTouchHandler(final ITouchHandler touchHandler) {
		this.touchHandler = touchHandler;
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if (this.touchHandler != null) {
			return this.touchHandler.onAreaTouched(pSceneTouchEvent,
					pTouchAreaLocalX, pTouchAreaLocalY);
		}
		return false;
	}
}
